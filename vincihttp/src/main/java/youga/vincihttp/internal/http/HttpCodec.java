package youga.vincihttp.internal.http;

import java.io.IOException;

import okio.Sink;
import youga.vincihttp.Request;
import youga.vincihttp.Response;
import youga.vincihttp.ResponseBody;

/**
 * Created by Youga on 2018/3/27.
 */

public interface HttpCodec {

    /**
     * The timeout to use while discarding a stream of input data. Since this is used for connection
     * reuse, this timeout should be significantly less than the time it takes to establish a new
     * connection.
     */
    int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

    /** Returns an output stream where the request body can be streamed. */
    Sink createRequestBody(Request request, long contentLength);

    /** This should update the HTTP engine's sentRequestMillis field. */
    void writeRequestHeaders(Request request) throws IOException;

    /** Flush the request to the underlying socket. */
    void flushRequest() throws IOException;

    /** Flush the request to the underlying socket and signal no more bytes will be transmitted. */
    void finishRequest() throws IOException;

    /**
     * Parses bytes of a response header from an HTTP transport.
     *
     * @param expectContinue true to return null if this is an intermediate response with a "100"
     *     response code. Otherwise this method never returns null.
     */
    Response.Builder readResponseHeaders(boolean expectContinue) throws IOException;

    /** Returns a stream that reads the response body. */
    ResponseBody openResponseBody(Response response) throws IOException;

    /**
     * Cancel this stream. Resources held by this stream will be cleaned up, though not synchronously.
     * That may happen later by the connection pool thread.
     */
    void cancel();
}
