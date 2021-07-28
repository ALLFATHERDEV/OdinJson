package com.odinallfather.json.version;

import com.google.gson.JsonObject;

public interface Deserializer<T> extends Coder {

    /**
     * Deserialize a type T
     *
     * @param jsonObject JsonObject to deserialize
     */
    T deserialize(JsonObject jsonObject);
}
