package io.apicurio.registry.systemtests.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Artifact {
    private String artifactId;
    private String artifactType;
    private String name;
    private Date createdOn;
    private String createdBy;
    private String type;
    private ArtifactState state;
    private Date modifiedOn;
    private String modifiedBy;
    private String groupId;
    private String description;
    private String owner;
    private Map<String, String> labels;
    private String version;
    private Long globalId;
    private Map<String, String> properties;
    private Long contentId;
    private List<ArtifactReference> references;

    public static String getArtifact(
            ArtifactType artifactType,
            String artifactId,
            String artifactContent,
            String contentType
    ) {
        return  "{" +
            "\"artifactId\": \"" + artifactId + "\"," +
            "\"artifactType\": \"" + artifactType + "\"," +
            "\"firstVersion\": " + "{" +
                "\"content\": " + "{" +
                    "\"content\": " + artifactContent +"," +
                    "\"contentType\":" + "\"" + contentType + "\"" +
                "}" +
            "}" +
        "}";
    }
}
