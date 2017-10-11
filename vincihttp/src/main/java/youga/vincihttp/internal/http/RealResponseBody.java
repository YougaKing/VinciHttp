package youga.vincihttp.internal.http;

import okio.BufferedSource;
import youga.vincihttp.MediaType;
import youga.vincihttp.ResponseBody;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/11 0011-10:10
 */

public class RealResponseBody extends ResponseBody {
    /**
     * Use a string to avoid parsing the content type until needed. This also defers problems caused
     * by malformed content types.
     */
    private final String contentTypeString;
    private final long contentLength;
    private final BufferedSource source;

    public RealResponseBody(String contentTypeString, long contentLength, BufferedSource source) {
        this.contentTypeString = contentTypeString;
        this.contentLength = contentLength;
        this.source = source;
    }

    @Override
    public MediaType contentType() {
        return contentTypeString != null ? MediaType.parse(contentTypeString) : null;
    }

    @Override
    public long contentLength() {
        return contentLength;
    }

    @Override
    public BufferedSource source() {
        return source;
    }
}
