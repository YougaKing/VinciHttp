package youga.vincihttp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import okio.BufferedSource;
import youga.vincihttp.internal.Util;

import static youga.vincihttp.internal.Util.UTF_8;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-15:51
 */

public abstract class ResponseBody {


    public abstract MediaType contentType();


    public abstract long contentLength();

    public final InputStream byteStream() {
        return source().inputStream();
    }

    public abstract BufferedSource source();


    public final String string() throws IOException {
        BufferedSource source = source();
        try {
            Charset charset = Util.bomAwareCharset(source, charset());
            return source.readString(charset);
        } finally {
            Util.closeQuietly(source);
        }
    }

    private Charset charset() {
        MediaType contentType = contentType();
        return contentType != null ? contentType.charset(UTF_8) : UTF_8;
    }
}
