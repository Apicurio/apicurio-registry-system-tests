package io.apicurio.registry.systemtests.api.features;

import io.apicur.registry.v1.ApicurioRegistry;
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

public class CreateReadUpdateDelete {
    protected static Logger LOGGER = LoggerUtils.getLogger();

    public static void testCreateReadUpdateDeleteAvro(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.AVRO, false);
    }

    public static void testCreateReadUpdateDeleteProtobuf(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.PROTOBUF, false);
    }

    public static void testCreateReadUpdateDeleteJson(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.JSON, false);
    }

    public static void testCreateReadUpdateDeleteOpenapi(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.OPENAPI, false);
    }

    public static void testCreateReadUpdateDeleteAsyncapi(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.ASYNCAPI, false);
    }

    public static void testCreateReadUpdateDeleteGraphql(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.GRAPHQL, false);
    }

    public static void testCreateReadUpdateDeleteKconnect(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.KCONNECT, false);
    }

    public static void testCreateReadUpdateDeleteWsdl(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.WSDL, false);
    }

    public static void testCreateReadUpdateDeleteXsd(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.XSD, false);
    }

    public static void testCreateReadUpdateDeleteXml(ApicurioRegistry apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.XML, false);
    }

    public static void testCreateReadUpdateDelete(
            ApicurioRegistry apicurioRegistry,
            String username,
            String password,
            ArtifactType artifactType,
            boolean useToken
    ) {
        LOGGER.info("Testing CRUD of {} artifact...", artifactType.name());

        // Wait for readiness of Apicurio Registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));

        // Prepare necessary variables
        String artifactGroupId = "registry-" + UUID.randomUUID();
        String artifactId = "registry-" + UUID.randomUUID();
        String artifactContent = ArtifactContent.ARTIFACTS_BY_TYPE.get(artifactType);
        String updatedArtifactContent = ArtifactContent.UPDATED_ARTIFACTS_BY_TYPE.get(artifactType);
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

        // Update artifact
        Assertions.assertTrue(client.updateArtifact(artifactGroupId, artifactId, updatedArtifactContent));
        // Check update of artifact
        Assertions.assertEquals(client.readArtifactContent(artifactGroupId, artifactId), updatedArtifactContent);

        // Delete artifact
        Assertions.assertTrue(client.deleteArtifact(artifactGroupId, artifactId));

        // List artifacts
        artifactList = client.listArtifacts();
        Assertions.assertNotNull(artifactList);
        // Check deletion of artifact
        Assertions.assertFalse(artifactList.contains(artifactGroupId, artifactId));

        // If we use token
        if (useToken) {
            // Check unauthorized API call
            Assertions.assertTrue(client.checkUnauthorizedFake());
        }
    }
}
