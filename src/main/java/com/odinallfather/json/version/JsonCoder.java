package com.odinallfather.json.version;

abstract class JsonCoder {

    protected String pathToDeserializer;

    /**
     * @param pathToDeserializer Path to where the deserializer is located
     */
    JsonCoder(String pathToDeserializer) {
        this.pathToDeserializer = pathToDeserializer;
    }
}
