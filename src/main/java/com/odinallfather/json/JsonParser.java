package com.odinallfather.json;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class JsonParser implements Closeable {

    private int peekedCharcode = -1;
    private Reader in;

    public JsonParser(String in) {
        this(new StringReader(in));
    }

    public JsonParser(InputStream in) {
        this(in, Charset.defaultCharset());
    }

    public JsonParser(InputStream in, Charset charset) {
        this(new InputStreamReader(in, charset));
    }

    public JsonParser(Reader in) {
        this.in = Objects.requireNonNull(in, "in may not be null");
    }

    public static JsonValue parse(String in) throws IOException {
        try (JsonParser parser = new JsonParser(in)) {
            return parser.readValue();
        }
    }

    public static JsonValue parse(InputStream in) throws IOException {
        try (JsonParser parser = new JsonParser(in)) {
            return parser.readValue();
        }
    }

    public static JsonValue parse(InputStream in, Charset charset) throws IOException {
        try (JsonParser parser = new JsonParser(in, charset)) {
            return parser.readValue();
        }
    }

    public static JsonValue parse(Reader in) throws IOException {
        try (JsonParser parser = new JsonParser(in)) {
            return parser.readValue();
        }
    }

    public JsonValue readValue() throws IOException {
        int chr = skipWhitespaces();

        switch (chr) {
            // @formatter:off
            case '\"':
                return new JsonValue(readRemainingString());
            case '{':
                return new JsonValue(readRemainingObject(new JsonObject()));
            case '[':
                return new JsonValue(readRemainingArray(new JsonArray()));
            // @formatter:on
            case 't':
                readRemainingTrue();
                return new JsonValue(true);
            case 'f':
                readRemainingFalse();
                return new JsonValue(false);
            case 'n':
                readRemainingNull();
                return new JsonValue();
            default:
                if (chr == '-' || (chr >= '0' && chr <= '9')) {
                    return new JsonValue(readRemainingNumber(chr));
                }
                return null;
        }
    }

    public String readName() throws IOException {
        String name = readString();

        if (skipWhitespaces() != ':') {
            throw new JsonException("Missing colon");
        }

        return name;
    }

    public String readString() throws IOException {
        if (skipWhitespaces() != '\"') {
            throw new JsonException("Invalid string");
        }

        return readRemainingString();
    }

    private String readRemainingString() throws IOException {
        StringBuilder builder = new StringBuilder(64);

        int chr;
        while ((chr = next()) != '\"') {
            if (chr == '\\') {
                switch (chr = next()) {
                    case '\"':
                    case '\\':
                    case '/':
                        builder.appendCodePoint(chr);
                        break;
                    case 'b':
                        builder.append('\b');
                        break;
                    case 'f':
                        builder.append('\f');
                        break;
                    case 'n':
                        builder.append('\n');
                        break;
                    case 'r':
                        builder.append('\r');
                        break;
                    case 't':
                        builder.append('\t');
                        break;
                    case 'u':
                        int charcode = 0;
                        for (int n = 0; n < 4; n++) {
                            int digit = hexDigit(chr);
                            if (digit == -1) {
                                throw new JsonException("Invalid unicode escape sequence");
                            }
                            charcode = charcode * 16 + digit; // value = value * radix + digit
                        }
                        builder.appendCodePoint(charcode);
                        break;
                    default:
                        throw new JsonException("Invalid escape sequence");
                }
            } else {
                if (Character.isISOControl(chr)) {
                    throw new JsonException("Invalid character");
                }
                builder.appendCodePoint(chr);
            }
        }

        return builder.toString();
    }

    public JsonObject readObject() throws IOException {
        return readObject(new JsonObject());
    }

    public <T extends Map<String, JsonValue>> T readObject(T dest) throws IOException {
        if (skipWhitespaces() != '{') {
            throw new JsonException("Invalid object");
        }

        return readRemainingObject(dest);
    }

    private <T extends Map<String, JsonValue>> T readRemainingObject(T object) throws IOException {
        if (peekSkipWhitespaces() == '}') {
            nextUnchecked();
            return object;
        }

        for (; ; ) {
            object.put(readName(), readValue());
            int chr = skipWhitespaces();
            if (chr == '}') {
                break;
            }
            if (chr != ',') {
                throw new JsonException("Expected right curly bracket or comma");
            }
        }

        return object;
    }

    public JsonArray readArray() throws IOException {
        return readArray(new JsonArray());
    }

    public <T extends Collection<JsonValue>> T readArray(T dest) throws IOException {
        if (skipWhitespaces() != '[') {
            throw new JsonException("Invalid array");
        }

        return readRemainingArray(dest);
    }

    private <T extends Collection<JsonValue>> T readRemainingArray(T array) throws IOException {
        if (peekSkipWhitespaces() == ']') {
            nextUnchecked();
            return array;
        }

        for (; ; ) {
            array.add(readValue());
            int chr = skipWhitespaces();
            if (chr == ']') {
                break;
            }
            if (chr != ',') {
                throw new JsonException("Expected right square bracket or comma");
            }
        }

        return array;
    }

    public boolean readBoolean() throws IOException {
        int chr = next();

        if (chr == 't') {
            readRemainingTrue();
            return true;
        } else if (chr == 'f') {
            readRemainingFalse();
            return false;
        }

        throw new JsonException("Invalid boolean");
    }

    private void readRemainingTrue() throws IOException {
        if (next() != 'r' || next() != 'u' || next() != 'e') {
            throw new JsonException("Invalid value");
        }
    }

    private void readRemainingFalse() throws IOException {
        if (next() != 'a' || next() != 'l' || next() != 's' || next() != 'e') {
            throw new JsonException("Invalid value");
        }
    }

    private void readRemainingNull() throws IOException {
        if (next() != 'u' || next() != 'l' || next() != 'l') {
            throw new JsonException("Invalid value");
        }
    }

    public Number readNumber() throws IOException {
        return readRemainingNumber(skipWhitespaces());
    }

    private Number readRemainingNumber(int chr) throws IOException {
        boolean negative;
        double exponent = 0;
        long fraction;

        // parse sign part
        if (chr == '-') {
            negative = true;
            chr = next();
        } else {
            negative = false;
        }

        // Parse fraction and exponent part
        fraction = decDigit(chr);
        if (fraction == -1) {
            throw new JsonException("Invalid number");
        }

        for (; ; ) {
            chr = peekUnchecked();

            if (chr == '.') {
                if (exponent == 0) {
                    exponent = 0.1;
                } else {
                    throw new JsonException("Invalid number");
                }
            } else {
                exponent *= 10;

                int digit = decDigit(chr);
                if (digit == -1) {
                    break;
                }
                fraction = fraction * 10 + digit;
            }

            next();
        }

        // Parse additional exponent part
        chr = peekUnchecked();
        if (chr == 'e' || chr == 'E') {
            next();

            boolean expNegative;

            chr = next();
            if (chr == '+') {
                expNegative = false;
                next();
            } else {
                expNegative = true;
                if (chr == '-') {
                    next();
                }
            }

            int expVal = decDigit(chr);
            if (expVal == -1) {
                throw new JsonException("Invalid number");
            }
            int digit;
            while ((digit = decDigit(peekUnchecked())) != -1) {
                next();
                expVal = expVal * 10 + digit;
            }

            if (expNegative) {
                expVal = -expVal;
            }

            if (exponent == 0) {
                exponent = Math.pow(10, expVal);
            } else {
                exponent *= Math.pow(10, expVal);
            }
        }

        if (negative) {
            fraction = -fraction;
        }

        // calculate and return final number
        if (exponent > 0) {
            return fraction / exponent;
        }

        return fraction;
    }

    private static int decDigit(int chr) {
        return chr >= 48 && chr <= 57 ? chr - 48 : -1;
    }

    private static int hexDigit(int chr) {
        if (chr >= '0') {
            if (chr <= '9') {
                return chr - '0';
            } else if (chr >= 'A') {
                if (chr <= 'F') {
                    return chr - 'A';
                } else if (chr >= 'a' && chr <= 'f') {
                    return chr - 'a';
                }
            }
        }
        return -1;
    }

    private int next() throws IOException {
        int charcode = nextUnchecked();

        if (charcode == -1) {
            throw new EOFException();
        }

        return charcode;
    }

    private int nextUnchecked() throws IOException {
        if (peekedCharcode != -1) {
            int charcode = peekedCharcode;
            peekedCharcode = -1;
            return charcode;
        }

        return in.read();
    }

    private int peekUnchecked() throws IOException {
        if (peekedCharcode == -1) {
            peekedCharcode = nextUnchecked();
        }

        return peekedCharcode;
    }

    private int peekSkipWhitespaces() throws IOException {
        if (peekedCharcode == -1) {
            peekedCharcode = skipWhitespaces();
        }

        return peekedCharcode;
    }

    public int skipWhitespaces() throws IOException {
        int chr;

        do {
            chr = next();
        } while (Character.isWhitespace(chr));

        return chr;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

}