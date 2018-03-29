package youga.vincihttp.internal.connection;

import java.io.IOException;

import youga.vincihttp.Interceptor;
import youga.vincihttp.Request;
import youga.vincihttp.Response;
import youga.vincihttp.VinciHttpClient;
import youga.vincihttp.internal.http.HttpCodec;
import youga.vincihttp.internal.http.RealInterceptorChain;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-16:47
 */

public class ConnectInterceptor implements Interceptor {
    private final VinciHttpClient mHttpClient;

    public ConnectInterceptor(VinciHttpClient httpClient) {
        mHttpClient = httpClient;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        Request request = realChain.request();
        StreamAllocation streamAllocation = realChain.streamAllocation();

        // We need the network to satisfy this request. Possibly for validating a conditional GET.
        boolean doExtensiveHealthChecks = !request.method().equals("GET");
        HttpCodec httpCodec = streamAllocation.newStream(mHttpClient, doExtensiveHealthChecks);
        RealConnection connection = streamAllocation.connection();

        return realChain.proceed(request, streamAllocation, httpCodec, connection);
    }
}
