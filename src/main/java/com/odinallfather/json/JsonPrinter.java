package com.odinallfather.json;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class JsonPrinter implements Closeable {

    private static final char[] NULL_CHARS = new char[]{'n', 'u', 'l', 'l'};

    private boolean prettyPrint;
    private char indentChar = ' ';
    private Writer out;

    private char[] lineBreakChars;
    private int indent;
    private int indentRecord;

    public JsonPrinter(OutputStream out) {
        this(out, Charset.defaultCharset());
    }

    public JsonPrinter(OutputStream out, Charset charset) {
        this(new OutputStreamWriter(out, charset));
    }

    public JsonPrinter(Writer out) {
        this(out, false);
    }

    public JsonPrinter(Writer out, boolean prettyPrint) {
        this.out = Objects.requireNonNull(out, "out may not be null");
        this.prettyPrint = prettyPrint;
    }

    public static void print(JsonValue json, OutputStream out) throws IOException {
        try (JsonPrinter printer = new JsonPrinter(out)) {
            printer.print(json);
        }
    }

    public static void print(JsonValue json, OutputStream out, Charset charset) throws IOException {
        try (JsonPrinter printer = new JsonPrinter(out, charset)) {
            printer.print(json);
        }
    }

    public static void print(JsonValue json, Writer out) throws IOException {
        try (JsonPrinter printer = new JsonPrinter(out)) {
            printer.print(json);
        }
    }

    public static void print(JsonValue json, Writer out, boolean prettyPrint) throws IOException {
        try (JsonPrinter printer = new JsonPrinter(out, prettyPrint)) {
            printer.print(json);
        }
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    private void increaseIndent() {
        if (lineBreakChars == null) {
            lineBreakChars = new char[8];
            lineBreakChars[0] = '\n';
            lineBreakChars[1] = indentChar;
            lineBreakChars[2] = indentChar;
            indent = 2;
        } else if ((indent += 2) >= lineBreakChars.length) {
            char[] newIndentChars = new char[lineBreakChars.length << 1];
            System.arraycopy(lineBreakChars, 0, newIndentChars, 0, lineBreakChars.length);
            lineBreakChars = newIndentChars;
            indentRecord = indent;
            lineBreakChars[indent - 1] = indentChar;
            lineBreakChars[indent] = indentChar;
        } else if (indent > indentRecord) {
            indentRecord = indent;
            lineBreakChars[indent - 1] = indentChar;
            lineBreakChars[indent] = indentChar;
        }
    }

    private void decreaseIndent() {
        indent -= 2;
        assert indent > -1;
    }

    private void lineBreak() throws IOException {
        out.write(lineBreakChars, 0, indent + 1);
    }

    public void print(JsonValue value) throws IOException {
        switch (value.getType()) {
            // @formatter:off
            case JsonValue.TYPE_NULL:
                printNull();
                break;
            case JsonValue.TYPE_OBJECT:
                print((JsonObject) value.getValue());
                break;
            case JsonValue.TYPE_ARRAY:
                print((JsonArray) value.getValue());
                break;
            case JsonValue.TYPE_STRING:
                print((String) value.getValue());
                break;
            case JsonValue.TYPE_NUMBER:
                print((Number) value.getValue());
                break;
            case JsonValue.TYPE_BOOLEAN:
                print((Boolean) value.getValue());
                break;
            // @formatter:on
        }
    }

    public void printNull() throws IOException {
        out.write(NULL_CHARS);
    }

    public void print(JsonObject value) throws IOException {
        if (value == null) {
            printNull();
        } else if (prettyPrint) {
            out.write("{");
            Iterator<Map.Entry<String, JsonValue>> itr = value.entrySet().iterator();
            if (itr.hasNext()) {
                increaseIndent();
                lineBreak();

                Map.Entry<String, JsonValue> entry = itr.next();

                // First entry
                out.write('\"');
                out.write(escapeString(entry.getKey()));
                out.write("\": ");
                print(entry.getValue());

                // Remaining entries
                while (itr.hasNext()) {
                    entry = itr.next();

                    out.write(',');
                    lineBreak();
                    out.write('\"');
                    out.write(escapeString(entry.getKey()));
                    out.write("\": ");
                    print(entry.getValue());
                }

                decreaseIndent();
                lineBreak();
            }
            out.write("}");
        } else {
            out.write('{');

            Iterator<Map.Entry<String, JsonValue>> itr = value.entrySet().iterator();
            if (itr.hasNext()) {
                Map.Entry<String, JsonValue> entry = itr.next();

                // First entry
                out.write('\"');
                out.write(escapeString(entry.getKey()));
                out.write("\":");
                print(entry.getValue());

                // Remaining entries
                while (itr.hasNext()) {
                    entry = itr.next();

                    out.write(",\"");
                    out.write(escapeString(entry.getKey()));
                    out.write("\":");
                    print(entry.getValue());
                }
            }

            out.write('}');
        }
    }

    public void print(JsonArray value) throws IOException {
        if (value == null) {
            printNull();
        } else if (prettyPrint) {
            out.write('[');

            Iterator<JsonValue> itr = value.iterator();
            if (itr.hasNext()) {
                increaseIndent();
                lineBreak();
                print(itr.next());
                while (itr.hasNext()) {
                    out.write(',');
                    lineBreak();
                    print(itr.next());
                }
                decreaseIndent();
                lineBreak();
            }

            out.write(']');
        } else {
            out.write('[');

            Iterator<JsonValue> itr = value.iterator();
            if (itr.hasNext()) {
                print(itr.next());
                while (itr.hasNext()) {
                    out.write(',');
                    print(itr.next());
                }
            }

            out.write(']');
        }
    }

    public void print(String value) throws IOException {
        if (value == null) {
            printNull();
        } else {
            out.write('\"');
            out.write(escapeString(value));
            out.write('\"');
        }
    }

    public void print(Number value) throws IOException {
        if (value == null) {
            printNull();
        } else {
            out.write(value.toString());
        }
    }

    public void print(Boolean value) throws IOException {
        if (value == null) {
            printNull();
        } else {
            out.write(value.toString());
        }
    }

    static String escapeString(String str) {
        int len = str.length();
        StringBuilder escapedString = new StringBuilder(len + (len >>> 1));

        int prev = 0;
        boolean unicode = false;
        for (int i = 0; i < len; i++) {
            char chr = str.charAt(i);
            switch (chr) {
                case '\"':
                case '\\':
                case '/':
                    break;
                case '\b':
                    chr = 'b';
                    break;
                case '\f':
                    chr = 'f';
                    break;
                case '\n':
                    chr = 'n';
                    break;
                case '\r':
                    chr = 'r';
                    break;
                case '\t':
                    chr = 't';
                    break;
                default:
                    if (chr < ' ' || (chr >= '\u0080' && chr < '\u00a0') || (chr >= '\u2000' && chr < '\u2100')) {
                        unicode = true;
                        break;
                    }
                    continue;
            }

            escapedString.append(str, prev, i); // Append the characters before the escaped character
            // Append escaped character
            escapedString.append('\\');
            if (unicode) {
                String hex = Integer.toHexString(chr);
                escapedString.append("u0000", 0, 5 - hex.length()).append(hex);
                unicode = false;
            } else {
                escapedString.append(chr);
            }
            prev = i + 1;
        }

        return escapedString.length() == 0 ? str : escapedString.append(str, prev, len).toString();
    }

    public void setUseTabs() {
        indentChar = '\t';
    }

    public void setUseSpaces() {
        indentChar = ' ';
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

}
