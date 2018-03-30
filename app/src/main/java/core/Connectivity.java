package core;

import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/03/30 15:55
 * description:
 */
public class Connectivity {


    public static int dnsTime(String host) {
        if (!TextUtils.isEmpty(host)) {
            try {
                long start = System.currentTimeMillis();
                InetAddress.getAllByName(host);
                int end = (int) (System.currentTimeMillis() - start);
                DataMap.add(host, end);
                return end;
            } catch (UnknownHostException var4) {
                return 0;
            }
        } else {
            return 0;
        }
    }

}
