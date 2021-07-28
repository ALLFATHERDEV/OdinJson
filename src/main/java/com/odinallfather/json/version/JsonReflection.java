package com.odinallfather.json.version;

public class JsonReflection {

    /**
     * Gets the deserializer according to the version
     *
     * @param pathToDeserializer Path to where the deserializer is located
     * @param version Version the deserializer can deserialize
     * @return deserializer based on the version
     */
    public static Deserializer getDeserializer(String pathToDeserializer, String version) {
        return (Deserializer) getCoder(pathToDeserializer, version);
    }

    /**
     * Gets the serializer according to the version
     *
     * @param pathToSerializer Path to where the serializer is located
     * @param version Version the serializer can serialize
     * @return serializer based on the version
     */
    public static Serializer getSerializer(String pathToSerializer, String version) {
        return (Serializer) getCoder(pathToSerializer, version);
    }

    /**
     * Gets the coder according to the version
     *
     * @param pathToCoder Path to where the coder is located
     * @param version Version the coder can decode or encode
     * @return coder based on the version
     */
    private static Coder getCoder(String pathToCoder, String version) {
        Coder coder;

        try {
            coder = Class.forName(pathToCoder.replace("{version}", version)).asSubclass(Deserializer.class).newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();

            return null;
        }

        return coder;
    }
}
