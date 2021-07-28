package com.odinallfather.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.*;

public class JsonObject implements Map<String, JsonValue> {

    private Map<String, JsonValue> data = new LinkedHashMap<>();

    public JsonObject() {
    }

    @SuppressWarnings("resource")
    public JsonObject(String in) throws IOException {
        new JsonParser(in).readObject(this);
    }

    @SuppressWarnings("resource")
    public JsonObject(Reader in) throws IOException {
        new JsonParser(in).readObject(this);
    }

    @SuppressWarnings("resource")
    public JsonObject(InputStream in) throws IOException {
        new JsonParser(in).readObject(this);
    }

    @SuppressWarnings("resource")
    public JsonObject(InputStream in, Charset charset) throws IOException {
        new JsonParser(in, charset).readObject(this);
    }

    public static JsonObject copyOf(Map<String, ?> map) {
        JsonObject object = new JsonObject();

        map.forEach((key, value) -> object.data.put(key, new JsonValue(value)));

        return object;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public JsonValue get(Object key) {
        return data.get(key);
    }

    @Override
    public JsonValue put(String key, JsonValue value) {
        return data.put(key, value);
    }

    @Override
    public JsonValue remove(Object key) {
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends JsonValue> m) {
        data.putAll(m);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<JsonValue> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return data.entrySet();
    }

    public JsonObject put(String key, Object value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, String value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, JsonArray value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, JsonObject value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, byte value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, short value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, int value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, long value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, float value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, double value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public JsonObject put(String key, boolean value) {
        data.put(key, new JsonValue(value));

        return this;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public byte getByte(String key) {
        return getByte(key, (byte) 0);
    }

    public short getShort(String key) {
        return getShort(key, (short) 0);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public JsonObject getObject(String key) {
        return getObject(key, null);
    }

    public JsonObject getObject(String key, JsonObject fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asObject();
    }

    public JsonObject getObjectOrNull(String key) {
        JsonValue obj = data.get(key);

        return obj == null || obj.isNull() ? null : obj.asObject();
    }

    public JsonObject getObjectElseNew(String key) {
        JsonValue obj = data.get(key);

        return obj != null ? obj.asObject() : new JsonObject();
    }

    public JsonArray getArray(String key) {
        return getArray(key, null);
    }

    public JsonArray getArray(String key, JsonArray fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asArray();
    }

    public JsonArray getArrayOrNull(String key) {
        JsonValue obj = data.get(key);

        return obj == null || obj.isNull() ? null : obj.asArray();
    }

    public JsonArray getArrayElseNew(String key) {
        JsonValue obj = data.get(key);

        return obj != null ? obj.asArray() : new JsonArray();
    }

    public String getString(String key, String fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asString();
    }

    public byte getByte(String key, byte fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asByte();
    }

    public short getShort(String key, short fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asShort();
    }

    public int getInt(String key, int fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asInt();
    }

    public long getLong(String key, long fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asLong();
    }

    public float getFloat(String key, float fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asFloat();
    }

    public double getDouble(String key, double fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asDouble();
    }

    public boolean getBoolean(String key, boolean fallback) {
        JsonValue value = data.get(key);

        return value == null ? fallback : value.asBoolean();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");

        for (Entry<String, JsonValue> entry : data.entrySet()) {
            joiner.add("\"" + JsonPrinter.escapeString(entry.getKey()) + "\":" + entry.getValue());
        }

        return joiner.toString();
    }

}