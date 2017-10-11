package youga.vincihttp.internal.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import youga.vincihttp.Call;
import youga.vincihttp.EventListener;
import youga.vincihttp.Interceptor;
import youga.vincihttp.Request;
import youga.vincihttp.Response;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-18:18
 */

public class RealInterceptorChain implements Interceptor.Chain {

    private final List<Interceptor> mInterceptors;
    private final Request mRequest;
    private final Call mCall;
    private final EventListener mEventListener;
    private final int mConnectTimeout;
    private final int mReadTimeout;
    private final int mWriteTimeout;
    private final int mIndex;
    private final HttpURLConnection mConnection;

    public RealInterceptorChain(List<Interceptor> interceptors, int index, HttpURLConnection connection,
                                Request request, Call call,
                                EventListener eventListener, int connectTimeout, int readTimeout, int writeTimeout) {
        mInterceptors = interceptors;
        mIndex = index;
        mConnection = connection;
        mRequest = request;
        mCall = call;
        mEventListener = eventListener;
        mConnectTimeout = connectTimeout;
        mReadTimeout = readTimeout;
        mWriteTimeout = writeTimeout;
    }

    @Override
    public Request request() {
        return mRequest;
    }

    @Override
    public Response proceed(Request request) throws IOException {
        return proceed(request, mConnection);
    }

    public Response proceed(Request request, HttpURLConnection connection) throws IOException {

        if (mIndex >= mInterceptors.size()) throw new AssertionError();


        RealInterceptorChain next = new RealInterceptorChain(mInterceptors, mIndex + 1, connection,
                request, mCall, mEventListener, mConnectTimeout, mReadTimeout,
                mWriteTimeout);

        Interceptor interceptor = mInterceptors.get(mIndex);
        Response response = interceptor.intercept(next);

        if (response == null) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }

        if (response.body() == null) {
            throw new IllegalStateException(
                    "interceptor " + interceptor + " returned a response with no body");
        }
        return response;
    }


    public int connectTimeoutMillis() {
        return mConnectTimeout;
    }

    public int readTimeoutMillis() {
        return mReadTimeout;
    }

    int writeTimeoutMillis() {
        return mWriteTimeout;
    }

    public HttpURLConnection connection() {
        return mConnection;
    }
}
