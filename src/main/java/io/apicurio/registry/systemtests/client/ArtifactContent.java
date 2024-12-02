package io.apicurio.registry.systemtests.client;

import io.apicurio.registry.systemtests.framework.Constants;

public final class ArtifactContent {
    public static final String DEFAULT_AVRO = "{" +
            "\"artifactType\":\"AVRO\"," +
            "\"artifactId\":\"" + Constants.AVRO_ARTIFACT_ID_PLACEHOLDER + "\"," +
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
