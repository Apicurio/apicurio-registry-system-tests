package io.apicurio.registry.systemtests.client;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "UTC")
    private Date createdOn;
    private String createdBy;
    private String type;
    private ArtifactState state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "UTC")
    private Date modifiedOn;
    private String modifiedBy;
    private String groupId;
    private String description;
    private List<String> labels;
    private String version;
    private Long globalId;
    private Map<String, String> properties;
    private Long contentId;
    private List<ArtifactReference> references;

}
