package core;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/03/30 15:53
 * description:
 */
public class DavinciInterceptor implements Interceptor {

    private final AtomicInteger mAtomicInteger = new AtomicInteger(0);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Connectivity.dnsTime(request.url().url().getHost());


        String var3 = String.valueOf(mAtomicInteger.getAndIncrement());

        TransactionState transactionState = new TransactionState();

        transactionState.setAppPhase(Core.mAtomicInteger.intValue());
//        transactionState.setNetworkInPhase(com.networkbench.agent.impl.c.a.d.o);


        return null;
    }
}
