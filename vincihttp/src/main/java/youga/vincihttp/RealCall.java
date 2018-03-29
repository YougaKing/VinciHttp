package youga.vincihttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import youga.vincihttp.internal.NamedRunnable;
import youga.vincihttp.internal.connection.ConnectInterceptor;
import youga.vincihttp.internal.http.BridgeInterceptor;
import youga.vincihttp.internal.http.CallServerInterceptor;
import youga.vincihttp.internal.http.RealInterceptorChain;
import youga.vincihttp.internal.http.RetryAndFollowUpInterceptor;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-17:17
 */

public class RealCall implements Call {


    private final VinciHttpClient mClient;
    private final Request mOriginalRequest;
    private EventListener mEventListener;
    private boolean mExecuted;
    private final RetryAndFollowUpInterceptor mRetryAndFollowUpInterceptor;

    static RealCall newRealCall(VinciHttpClient client, Request originalRequest) {
        return new RealCall(client, originalRequest);
    }


    public RealCall(VinciHttpClient client, Request originalRequest) {

        final EventListener.Factory eventListenerFactory = client.eventListenerFactory();

        mClient = client;
        mOriginalRequest = originalRequest;
        mRetryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(mClient);

        mEventListener = eventListenerFactory.create(this);
    }

    @Override
    public Request request() {
        return mOriginalRequest;
    }

    @Override
    public Response execute() throws IOException {
        synchronized (this) {
            if (mExecuted) throw new IllegalArgumentException("Already Executed");
            mExecuted = true;
        }
        mEventListener.callStart(this);
        mClient.dispatcher().executed(this);
        Response result = getResponseWithInterceptorChain();
        if (result == null) throw new IOException("Canceled");
        return result;
    }

    @Override
    public void enqueue(Callback callback) {
        synchronized (this) {
            if (mExecuted) throw new IllegalArgumentException("Already Executed");
            mExecuted = true;
        }
        mEventListener.callStart(this);
        mClient.dispatcher().enqueue(new AsyncCall(callback));
    }


    final class AsyncCall extends NamedRunnable {

        private final Callback mResponseCallback;

        AsyncCall(Callback responseCallback) {
            super("OkHttp %s", redactedUrl());
            this.mResponseCallback = responseCallback;
        }

        String host() {
            return mOriginalRequest.url().host();
        }

        @Override
        protected void execute() {
            try {
                Response response = getResponseWithInterceptorChain();
                mResponseCallback.onResponse(RealCall.this, response);
            } catch (IOException e) {
                mEventListener.callFailed(RealCall.this, e);
                mResponseCallback.onFailure(RealCall.this, e);
            } finally {
                mClient.dispatcher().finished(this);
            }
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isExecuted() {
        return mExecuted;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }


    String redactedUrl() {
//        return mOriginalRequest.url().redact();
        // TODO: 2017/10/10 0010
        return "";
    }

    private Response getResponseWithInterceptorChain() throws IOException {
        List<Interceptor> interceptors = new ArrayList<>();

        interceptors.addAll(mClient.interceptors());
        interceptors.add(mRetryAndFollowUpInterceptor);
        interceptors.add(new BridgeInterceptor());
        interceptors.add(new ConnectInterceptor(mClient));
        interceptors.add(new CallServerInterceptor());


        Interceptor.Chain chain = new RealInterceptorChain(interceptors, null, null,
                null, 0, mOriginalRequest);

        return chain.proceed(mOriginalRequest);
    }
}
