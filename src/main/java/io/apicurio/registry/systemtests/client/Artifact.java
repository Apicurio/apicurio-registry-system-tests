package io.apicurio.registry.systemtests.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setState(ArtifactState state) {
        this.state = state;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public void setReferences(List<ArtifactReference> references) {
        this.references = references;
    }
}
