package youga.vincihttp.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import okio.BufferedSink;
import okio.Okio;
import youga.vincihttp.Headers;
import youga.vincihttp.Interceptor;
import youga.vincihttp.Request;
import youga.vincihttp.Response;
import youga.vincihttp.ResponseBody;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-11:08
 */

public class CallServerInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        Request request = realChain.request();

        HttpURLConnection connection = realChain.connection();

        Headers headers = request.headers();
        for (int i = 0, size = headers.size(); i < size; i++) {
            connection.setRequestProperty(headers.name(i), headers.value(i));
        }

        if (request.body() != null) {
            OutputStream out = connection.getOutputStream();
            BufferedSink sink = Okio.buffer(Okio.sink(out));
            request.body().writeTo(sink);
            sink.close();
        }

        InputStream in = connection.getInputStream();


        Response.Builder responseBuilder = new Response.Builder()
                .code(connection.getResponseCode())
                .message(connection.getResponseMessage());


        ResponseBody body =  new RealResponseBody(null, 0, Okio.buffer(Okio.source(in)));

        Response response = responseBuilder
                .request(request)
                .body(body)
                .build();
        return response;
    }

}
