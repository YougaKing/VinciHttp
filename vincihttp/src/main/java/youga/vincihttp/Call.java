package youga.vincihttp;

import java.io.IOException;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-17:03
 */

public interface Call {

    Request request();

    Response execute() throws IOException;

    void enqueue(Callback callback);


    void cancel();

    boolean isExecuted();

    boolean isCanceled();


    interface Factory {
        Call newCall(Request request);
    }

}
