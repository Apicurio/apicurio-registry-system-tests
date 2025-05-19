package io.apicurio.registry.systemtests.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtifactList {
    private int count;
    private List<Artifact> artifacts;

    public boolean contains(String artifactGroupId, String artifactId) {
        if (artifacts == null) {
            return false;
        }

        for (Artifact a : artifacts) {
            if (a.getGroupId().equals(artifactGroupId) && a.getArtifactId().equals(artifactId)) {
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

            for (Artifact a : artifacts) {
                logger.info("{}/{}", a.getGroupId(), a.getArtifactId());
            }

            logger.info("======================");
        }
    }
}
