package youga.vincihttp.internal.http;

import java.io.IOException;
import java.util.List;

import youga.vincihttp.Connection;
import youga.vincihttp.Interceptor;
import youga.vincihttp.Request;
import youga.vincihttp.Response;
import youga.vincihttp.internal.connection.RealConnection;
import youga.vincihttp.internal.connection.StreamAllocation;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-18:18
 */

public class RealInterceptorChain implements Interceptor.Chain {

    private final List<Interceptor> mInterceptors;
    private final StreamAllocation mStreamAllocation;
    private final HttpCodec mHttpCodec;
    private final RealConnection mConnection;
    private final int mIndex;
    private final Request mRequest;

    public RealInterceptorChain(List<Interceptor> interceptors, StreamAllocation streamAllocation,
                                HttpCodec httpCodec, RealConnection connection, int index, Request request) {
        mInterceptors = interceptors;
        mStreamAllocation = streamAllocation;
        mHttpCodec = httpCodec;
        mConnection = connection;
        mIndex = index;
        mRequest = request;
    }

    @Override
    public Request request() {
        return mRequest;
    }

    public StreamAllocation streamAllocation() {
        return mStreamAllocation;
    }

    public HttpCodec httpStream() {
        return mHttpCodec;
    }

    public Connection connection() {
        return mConnection;
    }

    @Override
    public Response proceed(Request request) throws IOException {
        return proceed(request, mStreamAllocation, mHttpCodec, mConnection);
    }

    public Response proceed(Request request, StreamAllocation streamAllocation, HttpCodec httpCodec,
                            RealConnection connection) throws IOException {

        if (mIndex >= mInterceptors.size()) throw new AssertionError();


        RealInterceptorChain next = new RealInterceptorChain(mInterceptors, streamAllocation, httpCodec,
                connection, mIndex + 1, request);

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
}
