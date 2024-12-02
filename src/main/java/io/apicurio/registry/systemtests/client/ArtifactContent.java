package io.apicurio.registry.systemtests.client;

public final class ArtifactContent {
    public static final String DEFAULT_AVRO = "{" +
            "\"artifactType\":\"AVRO\"," +
            "\"artifactId\":\"<artifact_id>\"," +
            "\"name\":\"price\"," +
            "\"namespace\":\"com.example\"," +
            "\"type\":\"record\"," +
            "\"fields\":" + "[" +
            "{\"name\":\"symbol\"," + "\"type\":\"string\"}" +
            "," +
            "{\"name\":\"price\"," + "\"type\":\"string\"}" +
            "]" +
            "}";
}
