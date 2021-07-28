package com.odinallfather.json.version;

import com.google.gson.*;

import java.lang.reflect.Type;

public class JsonDeserializer<T> extends JsonCoder implements com.google.gson.JsonDeserializer {

    public JsonDeserializer(String pathToDeserializer) {
        super(pathToDeserializer);
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String version = "v" + jsonObject.get("version").getAsString().replace(".", "_");

        return (T) JsonReflection.getDeserializer(pathToDeserializer, version).deserialize(jsonObject);
    }
}
