package youga.davinci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import youga.vincihttp.Call;
import youga.vincihttp.Callback;
import youga.vincihttp.Request;
import youga.vincihttp.Response;
import youga.vincihttp.VinciHttpClient;

public class MainActivity extends AppCompatActivity {


    private static final String ENDPOINT = "https://api.github.com/repos/square/okhttp/contributors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        VinciHttpClient client = new VinciHttpClient.Builder()
                .build();

        final Request request = new Request.Builder()
                .url(ENDPOINT)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("onResponse", response.body().string());
            }
        });
    }
}
