package com.odinallfather.json.version;

import com.google.gson.JsonElement;

public interface Serializer<T> extends Coder {

    /**
     * Serializes a scoreboard or an inventory menu
     *
     * @param type type to serialize
     */
    JsonElement serialize(T type);
}
