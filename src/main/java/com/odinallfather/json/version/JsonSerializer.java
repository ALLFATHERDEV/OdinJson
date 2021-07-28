package com.odinallfather.json.version;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

public class JsonSerializer<T extends Versionable> extends JsonCoder implements com.google.gson.JsonSerializer<T> {

    /**
     * @param pathToDeserializer Path to where the deserializer is located
     */
    public JsonSerializer(String pathToDeserializer) {
        super(pathToDeserializer);
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonReflection.getSerializer(pathToDeserializer, src.getVersion()).serialize(src);
    }
}
