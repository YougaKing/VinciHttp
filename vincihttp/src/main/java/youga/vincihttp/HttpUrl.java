package youga.vincihttp;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import static youga.vincihttp.internal.Util.skipLeadingAsciiWhitespace;
import static youga.vincihttp.internal.Util.skipTrailingAsciiWhitespace;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-9:59
 */

public class HttpUrl {

    final String mScheme;
    final String mHost;


    public URL url() {
        try {
            return new URL(mScheme + mHost);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpUrl(Builder builder) {
        mScheme = builder.mScheme;
        mHost = builder.mHost;
    }

    public static HttpUrl parse(String url) {
        Builder builder = new Builder();
        Builder.ParseResult result = builder.parse(null, url);
        return result == Builder.ParseResult.SUCCESS ? builder.build() : null;
    }

    public static HttpUrl get(URL url) {
        return parse(url.toString());
    }

    public String host() {
        return mHost;
    }


    public static final class Builder {
        String mScheme;
        String mHost;
        int mPort = -1;

        public Builder() {
        }

        public Builder scheme(String scheme) {
            if (scheme == null) {
                throw new NullPointerException("scheme == null");
            } else if (scheme.equalsIgnoreCase("http")) {
                this.mScheme = "http";
            } else if (scheme.equalsIgnoreCase("https")) {
                this.mScheme = "https";
            } else {
                throw new IllegalArgumentException("unexpected scheme: " + scheme);
            }
            return this;
        }


        public Builder host(String host) {
            if (host == null) throw new NullPointerException("host == null");
            String encoded = canonicalizeHost(host, 0, host.length());
            if (encoded == null) throw new IllegalArgumentException("unexpected host: " + host);
            this.mHost = encoded;
            return this;
        }

        public Builder port(int port) {
            if (port <= 0 || port > 65535)
                throw new IllegalArgumentException("unexpected port: " + port);
            this.mPort = port;
            return this;
        }

        private static String canonicalizeHost(String input, int pos, int limit) {
            // Start by percent decoding the host. The WHATWG spec suggests doing this only after we've
            // checked for IPv6 square braces. But Chrome does it first, and that's more lenient.
            String percentDecoded = percentDecode(input, pos, limit, false);
//            return Util.canonicalizeHost(percentDecoded);
            return percentDecoded;
        }


        public HttpUrl build() {
            if (mScheme == null) throw new IllegalStateException("scheme == null");
            if (mHost == null) throw new IllegalStateException("host == null");
            return new HttpUrl(this);
        }

        public ParseResult parse(HttpUrl base, String input) {
            int pos = skipLeadingAsciiWhitespace(input, 0, input.length());
            int limit = skipTrailingAsciiWhitespace(input, pos, input.length());
            int schemeDelimiterOffset = schemeDelimiterOffset(input, pos, limit);
            if (schemeDelimiterOffset != -1) {
                if (input.regionMatches(true, pos, "https:", 0, 6)) {
                    this.mScheme = "https";
                    pos += "https:".length();
                } else if (input.regionMatches(true, pos, "http:", 0, 5)) {
                    this.mScheme = "http";
                    pos += "http:".length();
                } else {
                    return ParseResult.UNSUPPORTED_SCHEME; // Not an HTTP scheme.
                }
            } else if (base != null) {
                this.mScheme = base.mScheme;
            } else {
                return ParseResult.MISSING_SCHEME; // No scheme.
            }
            // TODO: 2017/10/10 0010

            mHost = input.substring(mScheme.length());

            return ParseResult.SUCCESS;
        }

        enum ParseResult {
            SUCCESS,
            MISSING_SCHEME,
            UNSUPPORTED_SCHEME,
            INVALID_PORT,
            INVALID_HOST,
        }
    }

    static String percentDecode(String encoded, int pos, int limit, boolean plusIsSpace) {
        for (int i = pos; i < limit; i++) {
            char c = encoded.charAt(i);
            if (c == '%' || (c == '+' && plusIsSpace)) {
                // TODO: 2017/10/10 0010

            }
        }

        // Fast path: no characters in [pos..limit) required decoding.
        return encoded.substring(pos, limit);
    }

    private static int schemeDelimiterOffset(String input, int pos, int limit) {
        if (limit - pos < 2) return -1;

        char c0 = input.charAt(pos);
        if ((c0 < 'a' || c0 > 'z') && (c0 < 'A' || c0 > 'Z')) return -1; // Not a scheme start char.

        for (int i = pos + 1; i < limit; i++) {
            char c = input.charAt(i);

            if ((c >= 'a' && c <= 'z')
                    || (c >= 'A' && c <= 'Z')
                    || (c >= '0' && c <= '9')
                    || c == '+'
                    || c == '-'
                    || c == '.') {
                continue; // Scheme character. Keep going.
            } else if (c == ':') {
                return i; // Scheme prefix!
            } else {
                return -1; // Non-scheme character before the first ':'.
            }
        }

        return -1; // No ':'; doesn't start with a scheme.
    }
}
