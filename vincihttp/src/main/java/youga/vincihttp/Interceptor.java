package youga.vincihttp;

import java.io.IOException;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-17:56
 */

public interface Interceptor {


    Response intercept(Chain chain) throws IOException;

    interface Chain {

        Request request();

        Response proceed(Request request) throws IOException;

    }

}
