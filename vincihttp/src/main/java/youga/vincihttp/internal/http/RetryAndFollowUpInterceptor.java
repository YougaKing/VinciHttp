package youga.vincihttp.internal.http;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import youga.vincihttp.Address;
import youga.vincihttp.Interceptor;
import youga.vincihttp.Request;
import youga.vincihttp.Response;
import youga.vincihttp.VinciHttpClient;
import youga.vincihttp.internal.connection.StreamAllocation;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/03/28 9:51
 * description:
 */
public class RetryAndFollowUpInterceptor implements Interceptor {

    private VinciHttpClient mClient;
    private StreamAllocation mStreamAllocation;

    public RetryAndFollowUpInterceptor(VinciHttpClient client) {
        mClient = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
//
//        mStreamAllocation = new StreamAllocation(
//                mClient.connectionPool(), createAddress(request.url()), callStackTrace);


        return null;
    }


//    private Address createAddress(HttpUrl url) {
//        SSLSocketFactory sslSocketFactory = null;
//        HostnameVerifier hostnameVerifier = null;
//        CertificatePinner certificatePinner = null;
//        if (url.isHttps()) {
//            sslSocketFactory = client.sslSocketFactory();
//            hostnameVerifier = client.hostnameVerifier();
//            certificatePinner = client.certificatePinner();
//        }
//
//        return new Address(url.host(), url.port(), client.dns(), client.socketFactory(),
//                sslSocketFactory, hostnameVerifier, certificatePinner, client.proxyAuthenticator(),
//                client.proxy(), client.protocols(), client.connectionSpecs(), client.proxySelector());
//    }
}
