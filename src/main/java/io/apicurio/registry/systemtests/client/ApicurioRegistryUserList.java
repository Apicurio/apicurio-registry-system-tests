package io.apicurio.registry.systemtests.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApicurioRegistryUserList {
    private int count;
    private List<ApicurioRegistryUser> roleMappings;

    public boolean contains(String principalId, String principalName, ApicurioRegistryUserRole role) {
        if (roleMappings == null) {
            return false;
        }

        for (ApicurioRegistryUser u : roleMappings) {
            if (
                    u.getPrincipalId().equals(principalId)
                            && u.getPrincipalName().equals(principalName)
                            && u.getRole().equals(role)
            ) {
                return true;
            }
        }

        return false;
    }

    public void printApicurioRegistryUserList(Logger logger) {
        if (roleMappings == null) {
            logger.warn("No ApicurioRegistryUsers to list.");
        } else {
            logger.info("=== ApicurioRegistryUsers list ===");
            logger.info("Size: {}", count);
            logger.info("----------------------");

            for (ApicurioRegistryUser u : roleMappings) {
                logger.info("{}/{}/{}", u.getPrincipalId(), u.getPrincipalName(), u.getRole());
            }

            logger.info("======================");
        }
    }

    public boolean isEmpty() {
        return count == 0;
    }
}
