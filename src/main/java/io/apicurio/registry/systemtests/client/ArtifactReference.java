package io.apicurio.registry.systemtests.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtifactReference {
    private String groupId;
    private String artifactId;
    private String version;
    private String name;

}
