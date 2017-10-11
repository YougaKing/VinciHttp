package youga.vincihttp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import youga.vincihttp.internal.Util;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-13:45
 */

public class Headers {

    private final String[] mNamesAndValues;

    public Headers(Builder builder) {
        mNamesAndValues = builder.mNamesAndValues.toArray(new String[builder.mNamesAndValues.size()]);
    }

    public Builder newBuilder() {
        Builder result = new Builder();
        Collections.addAll(result.mNamesAndValues, mNamesAndValues);
        return result;
    }


    public int size() {
        return mNamesAndValues.length / 2;
    }

    public String name(int index) {
        return mNamesAndValues[index * 2];
    }

    public String value(int index) {
        return mNamesAndValues[index * 2 + 1];
    }

    public Set<String> names() {
        TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0, size = size(); i < size; i++) {
            result.add(name(i));
        }
        return Collections.unmodifiableSet(result);
    }

    public List<String> values(String name) {
        List<String> result = null;
        for (int i = 0, size = size(); i < size; i++) {
            if (name.equalsIgnoreCase(name(i))) {
                if (result == null) result = new ArrayList<>(2);
                result.add(value(i));
            }
        }
        return result != null
                ? Collections.unmodifiableList(result)
                : Collections.<String>emptyList();
    }


    public static final class Builder{

        final List<String> mNamesAndValues = new ArrayList<>(20);

        public Headers build() {
            return new Headers(this);
        }


        public Builder add(String name, String value) {
            checkNameAndValue(name, value);
            return addLenient(name, value);
        }

        public Builder set(String name, String value) {
            checkNameAndValue(name, value);
            removeAll(name);
            addLenient(name, value);
            return this;
        }

        private void checkNameAndValue(String name, String value) {
            if (name == null) throw new NullPointerException("name == null");
            if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
            for (int i = 0, length = name.length(); i < length; i++) {
                char c = name.charAt(i);
                if (c <= '\u0020' || c >= '\u007f') {
                    throw new IllegalArgumentException(Util.format(
                            "Unexpected char %#04x at %d in header name: %s", (int) c, i, name));
                }
            }
            if (value == null) throw new NullPointerException("value for name " + name + " == null");
            for (int i = 0, length = value.length(); i < length; i++) {
                char c = value.charAt(i);
                if ((c <= '\u001f' && c != '\t') || c >= '\u007f') {
                    throw new IllegalArgumentException(Util.format(
                            "Unexpected char %#04x at %d in %s value: %s", (int) c, i, name, value));
                }
            }
        }

        public Builder removeAll(String name) {
            for (int i = 0; i < mNamesAndValues.size(); i += 2) {
                if (name.equalsIgnoreCase(mNamesAndValues.get(i))) {
                    mNamesAndValues.remove(i); // name
                    mNamesAndValues.remove(i); // value
                    i -= 2;
                }
            }
            return this;
        }

        Builder addLenient(String name, String value) {
            mNamesAndValues.add(name);
            mNamesAndValues.add(value.trim());
            return this;
        }
    }
}
