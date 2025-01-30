package io.apicurio.registry.systemtests.persistence.features;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicur.registry.v1.apicurioregistryspec.configuration.Env;
import io.apicurio.registry.systemtests.client.ApicurioRegistryApiClient;
import io.apicurio.registry.systemtests.client.ArtifactContent;
import io.apicurio.registry.systemtests.client.ArtifactList;
import io.apicurio.registry.systemtests.client.ArtifactType;
import io.apicurio.registry.systemtests.client.AuthMethod;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.util.UUID;

public class CreateReadRestartReadDelete {
    protected static Logger LOGGER = LoggerUtils.getLogger();

    public static void testCreateReadRestartReadDelete(
            ApicurioRegistry apicurioRegistry,
            String username,
            String password,
            ArtifactType artifactType,
            boolean useToken
    ) {
        // Wait for readiness of Apicurio Registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));

        // Prepare necessary variables
        String artifactGroupId = "registry-" + UUID.randomUUID();
        String artifactId = "registry-" + UUID.randomUUID();
        String artifactContent = ArtifactContent.ARTIFACTS_BY_TYPE.get(artifactType);
        String hostname = ApicurioRegistryUtils.getApicurioRegistryHostname(apicurioRegistry);

        // Get API client
        ApicurioRegistryApiClient client = new ApicurioRegistryApiClient(hostname);

        // If we want to use access token
        if (useToken) {
            // Update API client with token
            client.setToken(KeycloakUtils.getAccessToken(apicurioRegistry, username, password));
            // Set authentication method
            client.setAuthMethod(AuthMethod.TOKEN);
        }

        // Wait for readiness of API
        Assertions.assertTrue(client.waitServiceAvailable());

        // List artifacts
        ArtifactList artifactList = client.listArtifacts();
        Assertions.assertNotNull(artifactList);

        // Check that artifact does not exist yet
        Assertions.assertFalse(artifactList.contains(artifactGroupId, artifactId));

        // Create artifact
        Assertions.assertTrue(client.createArtifact(artifactGroupId, artifactId, artifactType, artifactContent));

        // List artifacts
        artifactList = client.listArtifacts();
        Assertions.assertNotNull(artifactList);

        // Check creation of artifact
        Assertions.assertTrue(artifactList.contains(artifactGroupId, artifactId));
        Assertions.assertEquals(client.readArtifactContent(artifactGroupId, artifactId), artifactContent);

        // SETTING OF ENVIRONMENT VARIABLE SHOULD RESTART REGISTRY POD
        // Set environment variable RESTART_POD of registry to true
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, new Env() {{
            setName("RESTART_POD");
            setValue("true");
        }});

        // Wait for API availability
        Assertions.assertTrue(client.waitServiceAvailable());

        // List artifacts
        artifactList = client.listArtifacts();
        Assertions.assertNotNull(artifactList);

        // Check presence of artifact
        Assertions.assertTrue(artifactList.contains(artifactGroupId, artifactId));
        Assertions.assertEquals(client.readArtifactContent(artifactGroupId, artifactId), artifactContent);

        // Delete artifact
        Assertions.assertTrue(client.deleteArtifact(artifactGroupId, artifactId));

        // List artifacts
        artifactList = client.listArtifacts();
        Assertions.assertNotNull(artifactList);

        // Check deletion of artifact
        Assertions.assertFalse(artifactList.contains(artifactGroupId, artifactId));
    }
}
