package io.apicurio.registry.systemtests.persistence.features;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.client.ArtifactType;
import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.slf4j.Logger;

public class CreateReadRestartReadDelete {
    protected static Logger LOGGER = LoggerUtils.getLogger();

    public static void testCreateReadRestartReadDelete(
            ApicurioRegistry apicurioRegistry,
            String username,
            String password,
            ArtifactType artifactType, // TODO: Remove type?
            boolean useToken
    ) {
        LOGGER.info(
                "ApicurioRegistry: {}/{}",
                apicurioRegistry.getMetadata().getName(),
                apicurioRegistry.getMetadata().getNamespace()
        );
        LOGGER.info("Username: {}", username);
        LOGGER.info("Password: {}", password);
        LOGGER.info("ArtifactType: {}", artifactType.name());
        LOGGER.info("Use Keycloak: {}", useToken);

        LOGGER.warn("Test is not implemented yet!");
    }
}
