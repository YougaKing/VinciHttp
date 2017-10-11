package youga.vincihttp;

import java.io.IOException;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-17:22
 */

public abstract class EventListener {

    public static final EventListener NONE = new EventListener() {
    };

    static EventListener.Factory factory(final EventListener listener) {
        return new Factory() {
            @Override
            public EventListener create(Call call) {
                return listener;
            }
        };
    }

    public void callStart(Call call) {

    }

    public  void callFailed(RealCall realCall, IOException e){

    }

    public interface Factory {
        EventListener create(Call call);
    }
}
