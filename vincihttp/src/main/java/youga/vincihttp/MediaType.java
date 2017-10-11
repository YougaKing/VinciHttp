package youga.vincihttp;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-11:12
 */

public class MediaType {

    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final Pattern TYPE_SUBTYPE = Pattern.compile(TOKEN + "/" + TOKEN);
    private static final Pattern PARAMETER = Pattern.compile(
            ";\\s*(?:" + TOKEN + "=(?:" + TOKEN + "|" + QUOTED + "))?");

    private final String mMediaType;
    private final String mType;
    private final String mSubtype;
    private final String mCharset;

    private MediaType(String mediaType, String type, String subtype, String charset) {
        this.mMediaType = mediaType;
        this.mType = type;
        this.mSubtype = subtype;
        this.mCharset = charset;
    }


    public static MediaType parse(String string) {
        Matcher typeSubtype = TYPE_SUBTYPE.matcher(string);
        if (!typeSubtype.lookingAt()) return null;
        String type = typeSubtype.group(1).toLowerCase(Locale.US);
        String subtype = typeSubtype.group(2).toLowerCase(Locale.US);

        String charset = null;
        Matcher parameter = PARAMETER.matcher(string);
        for (int s = typeSubtype.end(); s < string.length(); s = parameter.end()) {
            parameter.region(s, string.length());
            if (!parameter.lookingAt()) return null; // This is not a well-formed media type.

            String name = parameter.group(1);
            if (name == null || !name.equalsIgnoreCase("charset")) continue;
            String charsetParameter;
            String token = parameter.group(2);
            if (token != null) {
                // If the token is 'single-quoted' it's invalid! But we're lenient and strip the quotes.
                charsetParameter = (token.startsWith("'") && token.endsWith("'") && token.length() > 2)
                        ? token.substring(1, token.length() - 1)
                        : token;
            } else {
                // Value is "double-quoted". That's valid and our regex group already strips the quotes.
                charsetParameter = parameter.group(3);
            }
            if (charset != null && !charsetParameter.equalsIgnoreCase(charset)) {
                return null; // Multiple different charsets!
            }
            charset = charsetParameter;
        }

        return new MediaType(string, type, subtype, charset);
    }


    public Charset charset() {
        return charset(null);
    }

    public Charset charset(Charset defaultValue) {
        try {
            return mCharset != null ? Charset.forName(mCharset) : defaultValue;
        } catch (IllegalArgumentException e) {
            return defaultValue; // This charset is invalid or unsupported. Give up.
        }
    }
}
