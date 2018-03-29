package youga.vincihttp.internal.connection;

import java.io.IOException;

import youga.vincihttp.VinciHttpClient;
import youga.vincihttp.internal.http.HttpCodec;

/**
 * Created by Youga on 2018/3/27.
 */

public class StreamAllocation {


    public HttpCodec newStream(VinciHttpClient client, boolean doExtensiveHealthChecks) {
//        int connectTimeout = client.connectTimeoutMillis();
//        int readTimeout = client.readTimeoutMillis();
//        int writeTimeout = client.writeTimeoutMillis();
//        boolean connectionRetryEnabled = client.retryOnConnectionFailure();
//
//        try {
//            RealConnection resultConnection = findHealthyConnection(connectTimeout, readTimeout,
//                    writeTimeout, connectionRetryEnabled, doExtensiveHealthChecks);
//            HttpCodec resultCodec = resultConnection.newCodec(client, this);
//
//            synchronized (connectionPool) {
//                codec = resultCodec;
//                return resultCodec;
//            }
//        } catch (IOException e) {
//            throw new RouteException(e);
//        }
        return null;
    }

    public synchronized RealConnection connection() {
        return null;
    }
}
