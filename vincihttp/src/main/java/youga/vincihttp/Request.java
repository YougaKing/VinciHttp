package youga.vincihttp;

import java.net.URL;

import youga.vincihttp.internal.http.HttpMethod;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-17:03
 */

public final class Request {

    private final HttpUrl mUrl;
    private final String mMethod;
    private final Headers mHeaders;
    private final RequestBody mBody;
    private final Object mTag;

    public HttpUrl url() {
        return mUrl;
    }

    private Request(Builder builder) {
        mUrl = builder.mUrl;
        mMethod = builder.mMethod;
        mHeaders = builder.mHeaders.build();
        mBody = builder.mBody;
        mTag = builder.mTag == null ? this : builder.mTag;
    }

    public String method() {
        return mMethod;
    }

    public Headers headers() {
        return mHeaders;
    }

    public RequestBody body() {
        return mBody;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        HttpUrl mUrl;
        String mMethod;
        Headers.Builder mHeaders;
        RequestBody mBody;
        Object mTag;

        public Builder() {
            mMethod = "GET";
            mHeaders = new Headers.Builder();
        }

        Builder(Request request) {
            mUrl = request.mUrl;
            mMethod = request.mMethod;
            mBody = request.mBody;
            mTag = request.mTag;
            mHeaders = request.mHeaders.newBuilder();
        }

        Builder url(HttpUrl url) {
            if (url == null) throw new NullPointerException("url == null");
            this.mUrl = url;
            return this;
        }


        public Builder url(String url) {
            if (url == null) throw new NullPointerException("url == null");

            // Silently replace web socket URLs with HTTP URLs.
            if (url.regionMatches(true, 0, "ws:", 0, 3)) {
                url = "http:" + url.substring(3);
            } else if (url.regionMatches(true, 0, "wss:", 0, 4)) {
                url = "https:" + url.substring(4);
            }

            HttpUrl parsed = HttpUrl.parse(url);
            if (parsed == null) throw new IllegalArgumentException("unexpected url: " + url);
            return url(parsed);
        }

        public Builder url(URL url) {
            if (url == null) throw new NullPointerException("url == null");
            HttpUrl parsed = HttpUrl.get(url);
            if (parsed == null) throw new IllegalArgumentException("unexpected url: " + url);
            return url(parsed);
        }

        public Builder header(String name, String value) {
            mHeaders.set(name, value);
            return this;
        }

        public Builder addHeader(String name, String value) {
            mHeaders.add(name, value);
            return this;
        }


        public Builder get() {
            return method("GET", null);
        }

        public Builder post(RequestBody body) {
            return method("POST", body);
        }

        public Builder method(String method, RequestBody body) {
            if (method == null) throw new NullPointerException("method == null");
            if (method.length() == 0) throw new IllegalArgumentException("method.length() == 0");
            if (body != null && !HttpMethod.permitsRequestBody(method)) {
                throw new IllegalArgumentException("method " + method + " must not have a request body.");
            }
            if (body == null && HttpMethod.requiresRequestBody(method)) {
                throw new IllegalArgumentException("method " + method + " must have a request body.");
            }
            this.mMethod = method;
            this.mBody = body;
            return this;
        }

        public Builder tag(Object tag) {
            this.mTag = tag;
            return this;
        }

        public Request build() {
            if (mUrl == null) throw new IllegalStateException("url == null");
            return new Request(this);
        }
    }
}
