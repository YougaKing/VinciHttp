package core;

import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/03/30 15:58
 * description:
 */
public class DataMap {
    //
    private static final ConcurrentHashMap<String, State> mTimeMap = new ConcurrentHashMap<>();
//    public static final ConcurrentHashMap<String, q.b> a = new ConcurrentHashMap<>();
//    public static final ConcurrentHashMap<String, Integer> mTimeMap = new ConcurrentHashMap<>();


    public static void add(String key, int time) {
        if (!TextUtils.isEmpty(key) && time >= 0) {
            if (mTimeMap.get(key) == null) {
                mTimeMap.put(key, new State(time));
            }

        }
    }

    static class State {
        int time;
        boolean status;

        public State(int time) {
            this.time = time;
            this.status = false;
        }
    }

    public static class b {
        public int a;
        public boolean b;

        public b(int var1) {
            this.a = var1;
            this.b = false;
        }
    }
}
