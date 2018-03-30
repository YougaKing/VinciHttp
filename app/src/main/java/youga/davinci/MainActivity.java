package youga.davinci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.newrelic.agent.android.instrumentation.okhttp3.OkHttp3Instrumentation;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private static final String ENDPOINT = "https://o2o.dailyyoga.com.cn/620mirror/base/clientconfig/thirdConfig?sid=459e2ab32830badcbfcbb630eb39fa21&uid=97072933&deviceId=354360070253067358618666388243fdcbf1f2e714aaf0&type=1&channel=100001&channels=100001&version=6.4.6&time=1522374725&timezone=8.0&sign=45d6426f862e2ffbe1edf1b06626954f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {

        OkHttpClient client = new OkHttpClient
                .Builder()
                .build();
        Request.Builder builder = new Request
                .Builder()
                .url(ENDPOINT)
                .get();

        Request request = OkHttp3Instrumentation.build(builder);
        OkHttp3Instrumentation.newCall(client, request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("onResponse", response.body().string());
            }
        });


        String state = NetUtils.getNetworkState(this);
        String operatorName = NetUtils.getOperatorName(this);
        boolean connected = NetUtils.isNetConnected(this);
        boolean isWifiConnected = NetUtils.isWifiConnected(this);

        Log.d("MainActivity", "\n" + state + "\n" + operatorName + "\n" + connected + "\n" + isWifiConnected);
    }
}
