package io.apicurio.registry.systemtests.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import io.apicurio.registry.rest.v2.beans.ArtifactMetaData;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtifactList {
    private int count;
    private List<ArtifactMetaData> artifacts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ArtifactMetaData> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<ArtifactMetaData> artifacts) {
        this.artifacts = artifacts;
    }

    public boolean contains(String artifactGroupId, String artifactId) {
        if (artifacts == null) {
            return false;
        }

        for (ArtifactMetaData a : artifacts) {
            if (a.getGroupId().equals(artifactGroupId) && a.getId().equals(artifactId)) {
                return true;
            }
        }

        return false;
    }

    public void printArtifactList(Logger logger) {
        if (artifacts == null) {
            logger.warn("No Artifacts to list.");
        } else {
            logger.info("=== Artifacts list ===");
            logger.info("Size: {}", count);
            logger.info("----------------------");

            for (ArtifactMetaData a : artifacts) {
                logger.info("{}/{}", a.getGroupId(), a.getId());
            }

            logger.info("======================");
        }
    }
}
