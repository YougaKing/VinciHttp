package youga.davinci;

import android.app.Application;

import com.newrelic.agent.android.FeatureFlag;
import com.newrelic.agent.android.NewRelic;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/03/29 18:38
 * description:
 */
public class DavinciApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NewRelic.enableFeature(FeatureFlag.DistributedTracing);

        NewRelic.withApplicationToken("GENERATED_TOKEN")
                .usingSsl(true)
                .withLoggingEnabled(true)
                .withHttpResponseBodyCaptureEnabled(true)
                .withInteractionTracing(true)
                .start(this);

    }
}
