package youga.vincihttp.internal.http;

import java.io.IOException;

import youga.vincihttp.Interceptor;
import youga.vincihttp.Request;
import youga.vincihttp.Response;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-11:10
 */

public class BridgeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request userRequest = chain.request();
        Request.Builder requestBuilder = userRequest.newBuilder();



        Response networkResponse = chain.proceed(requestBuilder.build());


        Response.Builder responseBuilder = networkResponse.newBuilder()
                .request(userRequest);

        return responseBuilder.build();
    }
}
