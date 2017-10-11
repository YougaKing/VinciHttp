package youga.vincihttp.internal.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

import youga.vincihttp.Interceptor;
import youga.vincihttp.Request;
import youga.vincihttp.Response;
import youga.vincihttp.internal.http.RealInterceptorChain;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-16:47
 */

public class ConnectInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        RealInterceptorChain realChain = (RealInterceptorChain) chain;

        Request request = realChain.request();

        HttpURLConnection connection = (HttpURLConnection) request.url().url().openConnection();

        try {
            connection.setRequestMethod(request.method());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setConnectTimeout(realChain.connectTimeoutMillis());
        connection.setReadTimeout(realChain.readTimeoutMillis());

        return realChain.proceed(request, connection);
    }
}
