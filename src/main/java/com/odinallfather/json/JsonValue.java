package com.odinallfather.json;

public class JsonValue {

    private static String[] names = new String[]{"string", "number", "object", "array", "boolean", "null"};

    public static final byte TYPE_STRING = 0;
    public static final byte TYPE_NUMBER = 1;
    public static final byte TYPE_OBJECT = 2;
    public static final byte TYPE_ARRAY = 3;
    public static final byte TYPE_BOOLEAN = 4;
    public static final byte TYPE_NULL = 5;

    private byte type;
    private Object value;

    public JsonValue() {
        type = TYPE_NULL;
    }

    public JsonValue(Object value) {
        setValue(value);
    }

    public JsonValue(JsonObject value) {
        this(TYPE_OBJECT, value);
    }

    public JsonValue(JsonArray value) {
        this(TYPE_ARRAY, value);
    }

    public JsonValue(String value) {
        this(TYPE_STRING, value);
    }

    public JsonValue(Number value) {
        this(TYPE_NUMBER, value);
    }

    public JsonValue(Boolean value) {
        this(TYPE_BOOLEAN, value);
    }

    private JsonValue(byte type, Object value) {
        if (value == null) {
            this.type = TYPE_NULL;
        } else {
            this.type = type;
            this.value = value;
        }
    }

    public byte getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setNull() {
        type = TYPE_NULL;
        value = null;
    }

    public void setValue(String value) {
        setTypeAndValue(TYPE_STRING, value);
    }

    public void setValue(JsonArray value) {
        setTypeAndValue(TYPE_ARRAY, value);
    }

    public void setValue(JsonObject value) {
        setTypeAndValue(TYPE_OBJECT, value);
    }

    public void setValue(byte value) {
        type = TYPE_NUMBER;
        this.value = value;
    }

    public void setValue(short value) {
        type = TYPE_NUMBER;
        this.value = value;
    }

    public void setValue(int value) {
        type = TYPE_NUMBER;
        this.value = value;
    }

    public void setValue(long value) {
        type = TYPE_NUMBER;
        this.value = value;
    }

    public void setValue(float value) {
        type = TYPE_NUMBER;
        this.value = value;
    }

    public void setValue(double value) {
        type = TYPE_NUMBER;
        this.value = value;
    }

    public void setValue(boolean value) {
        type = TYPE_BOOLEAN;
        this.value = value;
    }

    public void setValue(Object value) {
        byte type = typeOf(value);

        if (type == -1) {
            throw new IllegalArgumentException("Unknown Type: " + (value == null ? "null" : value.getClass().getName()));
        }

        this.type = type;
        this.value = value;
    }

    private void setTypeAndValue(byte type, Object value) {
        if (value == null) {
            this.type = TYPE_NULL;
            this.value = null;
        } else {
            this.type = type;
            this.value = value;
        }
    }

    private byte typeOf(Object obj) {
        if (obj == null) {
            return TYPE_NULL;
        }

        if (obj instanceof String) {
            return TYPE_STRING;
        }

        if (obj instanceof Number) {
            return TYPE_NUMBER;
        }

        if (obj instanceof JsonObject) {
            return TYPE_OBJECT;
        }

        if (obj instanceof JsonArray) {
            return TYPE_ARRAY;
        }

        if (obj instanceof Boolean) {
            return TYPE_BOOLEAN;
        }

        return -1;
    }

    public boolean isString() {
        return type == TYPE_STRING;
    }

    public boolean isNumber() {
        return type == TYPE_NUMBER;
    }

    public boolean isObject() {
        return type == TYPE_OBJECT;
    }

    public boolean isArray() {
        return type == TYPE_ARRAY;
    }

    public boolean isBoolean() {
        return type == TYPE_BOOLEAN;
    }

    public boolean isNull() {
        return type == TYPE_NULL;
    }

    public String asString() {
        checkType(TYPE_STRING);

        return (String) value;
    }

    public JsonArray asArray() {
        checkType(TYPE_ARRAY);

        return (JsonArray) value;
    }

    public JsonObject asObject() {
        checkType(TYPE_OBJECT);

        return (JsonObject) value;
    }

    public byte asByte() {
        checkType(TYPE_NUMBER);

        return ((Number) value).byteValue();
    }

    public short asShort() {
        checkType(TYPE_NUMBER);

        return ((Number) value).shortValue();
    }

    public int asInt() {
        checkType(TYPE_NUMBER);

        return ((Number) value).intValue();
    }

    public long asLong() {
        checkType(TYPE_NUMBER);

        return ((Number) value).longValue();
    }

    public float asFloat() {
        checkType(TYPE_NUMBER);

        return ((Number) value).floatValue();
    }

    public double asDouble() {
        checkType(TYPE_NUMBER);

        return ((Number) value).doubleValue();
    }

    public boolean asBoolean() {
        checkType(TYPE_BOOLEAN);

        return (boolean) value;
    }

    @Override
    public String toString() {
        return type == TYPE_STRING ? '\"' + asEscapedString() + '\"' : String.valueOf(value);
    }

    public String asEscapedString() {
        return JsonPrinter.escapeString(asString());
    }

    @Override
    public int hashCode() {
        return type == TYPE_NULL ? 0 : value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonValue) {
            JsonValue jsonValue = (JsonValue) obj;
            return type == jsonValue.type && (type == TYPE_NULL || obj.equals(jsonValue.value));
        }

        // Ignoring "bad practice" and let e.g. this.equals(5) be true if this.value is also 5
        return type == TYPE_NULL ? obj == null : value.equals(obj);
    }

    public String getTypeName() {
        return names[type];
    }

    private void checkType(byte expectedType) {
        if (type != expectedType) {
            throw new IllegalStateException("Invalid value type (expected=" + names[expectedType] + " current=" + getTypeName()
                    + ")");
        }
    }

}
