package media;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/04/02 15:52
 * description:
 */
public class OnlineMedia {

    //https://ypyvideo.dailyyoga.com.cn/Hip_Tension_Relief_EN1515644357679
    private static final String TAG = OnlineMedia.class.getSimpleName();

    public static void play(OkHttpClient client, String url) {
        Request.Builder builder = new Request
                .Builder()
                .url(url)
                .get();
        client.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream stream = response.body().byteStream();



            }
        });
    }
}
