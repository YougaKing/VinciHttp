package youga.vincihttp;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-17:12
 */

public class Response {

    final ResponseBody mBody;
    final Request mRequest;
    final int mCode;
    final String mMessage;
    final Headers.Builder mHeaders;

    Response(Builder builder) {
        mBody = builder.mBody;
        mRequest = builder.mRequest;
        mCode = builder.mCode;
        mMessage = builder.mMessage;
        mHeaders = builder.mHeaders;
    }


    public ResponseBody body() {
        return mBody;
    }


    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        ResponseBody mBody;
        Request mRequest;
        int mCode;
        String mMessage;
        Headers.Builder mHeaders;

        public Builder() {
            mHeaders = new Headers.Builder();
        }

        public Builder(Response response) {
            mBody = response.mBody;
            mRequest = response.mRequest;
            mCode = response.mCode;
            mMessage = response.mMessage;
            mHeaders = response.mHeaders;
        }

        public Builder code(int code) {
            mCode = code;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder headers(Headers headers) {
            this.mHeaders = headers.newBuilder();
            return this;
        }

        public Builder request(Request userRequest) {
            mRequest = userRequest;
            return this;
        }

        public Builder body(ResponseBody body) {
            mBody = body;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
