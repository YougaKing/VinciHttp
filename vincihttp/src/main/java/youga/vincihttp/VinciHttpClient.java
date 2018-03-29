package youga.vincihttp;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import youga.vincihttp.internal.Util;

import static youga.vincihttp.internal.Util.checkDuration;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-16:26
 */

public class VinciHttpClient implements Call.Factory {


    private final Dispatcher mDispatcher;
    private final List<Interceptor> mInterceptors;
    private final EventListener.Factory mEventListenerFactory;
    private final boolean mRetryOnConnectionFailure;
    private final int mConnectTimeout;
    private final int mReadTimeout;
    private final int mWriteTimeout;
    private final ConnectionPool mConnectionPool;

    public VinciHttpClient() {
        this(new Builder());
    }

    private VinciHttpClient(Builder builder) {
        mDispatcher = builder.mDispatcher;
        this.mInterceptors = Util.immutableList(builder.mInterceptors);
        mRetryOnConnectionFailure = builder.mRetryOnConnectionFailure;
        mEventListenerFactory = builder.mEventListenerFactory;
        mConnectTimeout = builder.mConnectTimeout;
        mReadTimeout = builder.mReadTimeout;
        mWriteTimeout = builder.mWriteTimeout;

        mConnectionPool = builder.mConnectionPool;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }


    Dispatcher dispatcher() {
        return mDispatcher;
    }

    @Override
    public Call newCall(Request request) {
        return RealCall.newRealCall(this, request);
    }

    EventListener.Factory eventListenerFactory() {
        return mEventListenerFactory;
    }

    List<Interceptor> interceptors() {
        return mInterceptors;
    }

    int connectTimeoutMillis() {
        return mConnectTimeout;
    }

    int readTimeoutMillis() {
        return mReadTimeout;
    }

    int writeTimeoutMillis() {
        return mWriteTimeout;
    }

    public ConnectionPool connectionPool() {
        return mConnectionPool;
    }

    public static final class Builder {
        Dispatcher mDispatcher;
        List<Interceptor> mInterceptors = new ArrayList<>();
        EventListener.Factory mEventListenerFactory;
        boolean mRetryOnConnectionFailure;
        int mConnectTimeout;
        int mReadTimeout;
        int mWriteTimeout;
        ConnectionPool mConnectionPool;

        public Builder() {
            mDispatcher = new Dispatcher();
            mEventListenerFactory = EventListener.factory(EventListener.NONE);
            mRetryOnConnectionFailure = true;
            mConnectTimeout = 10_000;
            mReadTimeout = 10_000;
            mWriteTimeout = 10_000;
            mConnectionPool = new ConnectionPool();
        }

        Builder(VinciHttpClient httpClient) {
            mDispatcher = httpClient.mDispatcher;
            mInterceptors.addAll(httpClient.mInterceptors);
            mEventListenerFactory = httpClient.mEventListenerFactory;
            mRetryOnConnectionFailure = httpClient.mRetryOnConnectionFailure;
            mConnectTimeout = httpClient.mConnectTimeout;
            mReadTimeout = httpClient.mReadTimeout;
            mWriteTimeout = httpClient.mWriteTimeout;
        }

        public Builder dispatcher(Dispatcher dispatcher) {
            if (dispatcher == null) throw new IllegalArgumentException("dispatcher == null");
            mDispatcher = dispatcher;
            return this;
        }

        public Builder eventListener(EventListener eventListener) {
            if (eventListener == null) throw new NullPointerException("eventListener == null");
            mEventListenerFactory = EventListener.factory(eventListener);
            return this;
        }

        public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            mRetryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public Builder connectTimeout(long timeout, TimeUnit unit) {
            mConnectTimeout = checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit unit) {
            mReadTimeout = checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit unit) {
            mWriteTimeout = checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptor == null) throw new IllegalArgumentException("interceptor == null");
            mInterceptors.add(interceptor);
            return this;
        }

        public VinciHttpClient build() {
            return new VinciHttpClient(this);
        }
    }
}
