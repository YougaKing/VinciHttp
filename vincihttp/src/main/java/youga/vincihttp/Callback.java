package youga.vincihttp;

import java.io.IOException;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-17:13
 */

public interface Callback {

    void onFailure(Call call, IOException e);

    void onResponse(Call call, Response response) throws IOException;
}
