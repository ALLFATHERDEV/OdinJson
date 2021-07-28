package com.odinallfather.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.*;

public class JsonArray implements List<JsonValue> {

    private List<JsonValue> data;

    public JsonArray() {
        this(16);
    }

    public JsonArray(int initialSize) {
        data = new ArrayList<>(initialSize);
    }

    @SuppressWarnings("resource")
    public JsonArray(String in) throws IOException {
        this();

        new JsonParser(in).readArray(this);
    }

    @SuppressWarnings("resource")
    public JsonArray(Reader in) throws IOException {
        this();

        new JsonParser(in).readArray(this);
    }

    @SuppressWarnings("resource")
    public JsonArray(InputStream in) throws IOException {
        this();

        new JsonParser(in).readArray(this);
    }

    @SuppressWarnings("resource")
    public JsonArray(InputStream in, Charset charset) throws IOException {
        this();

        new JsonParser(in, charset).readArray(this);
    }

    public static JsonArray copyOf(Collection<?> collection) {
        JsonArray json = new JsonArray(collection.size());

        for (Object value : collection) {
            json.add(new JsonValue(value));
        }

        return json;
    }

    @SafeVarargs
    public static <T> JsonArray copyOf(T... array) {
        JsonArray json = new JsonArray(array.length);

        for (Object value : array) {
            json.add(new JsonValue(value));
        }

        return json;
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
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @Override
    public Iterator<JsonValue> iterator() {
        return data.iterator();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return data.toArray(a);
    }

    @Override
    public boolean add(JsonValue e) {
        return data.add(e == null ? new JsonValue() : e);
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends JsonValue> c) {
        for (JsonValue element : c) {
            data.add(element == null ? new JsonValue() : element);
        }

        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends JsonValue> c) {
        for (JsonValue element : c) {
            data.add(index++, element == null ? new JsonValue() : element);
        }

        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return data.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return data.retainAll(c);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public JsonValue get(int index) {
        return data.get(index);
    }

    @Override
    public JsonValue set(int index, JsonValue element) {
        return data.set(index, element == null ? new JsonValue() : element);
    }

    @Override
    public void add(int index, JsonValue element) {
        data.add(index, element == null ? new JsonValue() : element);
    }

    @Override
    public JsonValue remove(int index) {
        return data.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public ListIterator<JsonValue> listIterator() {
        return data.listIterator();
    }

    @Override
    public ListIterator<JsonValue> listIterator(int index) {
        return data.listIterator(index);
    }

    @Override
    public List<JsonValue> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }

    public String getString(int index) {
        return data.get(index).asString();
    }

    public JsonArray getArray(int index) {
        return data.get(index).asArray();
    }

    public JsonObject getObject(int index) {
        return data.get(index).asObject();
    }

    public byte getByte(int index) {
        return data.get(index).asByte();
    }

    public short getShort(int index) {
        return data.get(index).asShort();
    }

    public int getInt(int index) {
        return data.get(index).asInt();
    }

    public long getLong(int index) {
        return data.get(index).asLong();
    }

    public float getFloat(int index) {
        return data.get(index).asFloat();
    }

    public double getDouble(int index) {
        return data.get(index).asDouble();
    }

    public boolean getBoolean(int index) {
        return data.get(index).asBoolean();
    }

    public JsonArray add(String value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(JsonArray value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(JsonObject value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(byte value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(short value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(int value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(long value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(float value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(double value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray add(boolean value) {
        data.add(new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, String value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, JsonArray value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, JsonObject value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, byte value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, short value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, int value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, long value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, float value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, double value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    public JsonArray set(int index, boolean value) {
        data.set(index, new JsonValue(value));

        return this;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");

        for (JsonValue entry : data) {
            joiner.add(entry.toString());
        }

        return joiner.toString();
    }

}
