package io.apicurio.registry.systemtests.client;

public final class ArtifactContent {
    public static final String DEFAULT_AVRO = "\"{" +
        "\\\"name\\\":\\\"price\\\"" + "," +
        "\\\"namespace\\\":\\\"com.example\\\"" + "," +
        "\\\"type\\\":\\\"record\\\"" + "," +
        "\\\"fields\\\": [" +
            "{\\\"name\\\":\\\"symbol\\\", \\\"type\\\":\\\"string\\\"}" + "," +
            "{\\\"name\\\":\\\"price\\\", \\\"type\\\":\\\"string\\\"}" +
        "]" +
    "}\"";
    public static final String DEFAULT_AVRO_PLAIN = "\"{" +
        "\"name\":\"price\"" + "," +
        "\"namespace\":\"com.example\"" + "," +
        "\"type\":\"record\"" + "," +
        "\"fields\": [" +
            "{\"name\":\"symbol\", \"type\":\"string\"}" + "," +
            "{\"name\":\"price\", \"type\":\"string\"}" +
        "]" +
    "}\"";
}
