package io.apicurio.registry.systemtests.api.features;

import io.apicur.registry.v1.ApicurioRegistry3;
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

    private static final HashMap<ArtifactType, String> artifacts = new HashMap<>(){{
        put(ArtifactType.AVRO, ArtifactContent.DEFAULT_AVRO);
        put(ArtifactType.PROTOBUF, ArtifactContent.DEFAULT_PROTOBUF);
        put(ArtifactType.JSON, ArtifactContent.DEFAULT_JSON);
        put(ArtifactType.OPENAPI, ArtifactContent.DEFAULT_OPENAPI);
        put(ArtifactType.ASYNCAPI, ArtifactContent.DEFAULT_ASYNCAPI);
        put(ArtifactType.GRAPHQL, ArtifactContent.DEFAULT_GRAPHQL);
        put(ArtifactType.KCONNECT, ArtifactContent.DEFAULT_KCONNECT);
        put(ArtifactType.WSDL, ArtifactContent.DEFAULT_WSDL);
        put(ArtifactType.XSD, ArtifactContent.DEFAULT_XSD);
        put(ArtifactType.XML, ArtifactContent.DEFAULT_XML);
    }};
    private static final HashMap<ArtifactType, String> artifactsPlain = new HashMap<>(){{
       put(ArtifactType.AVRO, ArtifactContent.DEFAULT_AVRO_PLAIN);
    }};
    private static final HashMap<ArtifactType, String> updatedArtifacts = new HashMap<>(){{
        put(ArtifactType.AVRO, ArtifactContent.DEFAULT_AVRO_UPDATED);
        put(ArtifactType.PROTOBUF, ArtifactContent.DEFAULT_PROTOBUF_UPDATED);
        put(ArtifactType.JSON, ArtifactContent.DEFAULT_JSON_UPDATED);
        put(ArtifactType.OPENAPI, ArtifactContent.DEFAULT_OPENAPI_UPDATED);
        put(ArtifactType.ASYNCAPI, ArtifactContent.DEFAULT_ASYNCAPI_UPDATED);
        put(ArtifactType.GRAPHQL, ArtifactContent.DEFAULT_GRAPHQL_UPDATED);
        put(ArtifactType.KCONNECT, ArtifactContent.DEFAULT_KCONNECT_UPDATED);
        put(ArtifactType.WSDL, ArtifactContent.DEFAULT_WSDL_UPDATED);
        put(ArtifactType.XSD, ArtifactContent.DEFAULT_XSD_UPDATED);
        put(ArtifactType.XML, ArtifactContent.DEFAULT_XML_UPDATED);
    }};
    private static final HashMap<ArtifactType, String> artifactsUpdatedPlain = new HashMap<>(){{
        put(ArtifactType.AVRO, ArtifactContent.DEFAULT_AVRO_UPDATED_PLAIN);
    }};

    public static void testCreateReadUpdateDeleteAvro(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.AVRO, false);
    }

    public static void testCreateReadUpdateDeleteProtobuf(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.PROTOBUF, false);
    }

    public static void testCreateReadUpdateDeleteJson(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.JSON, false);
    }

    public static void testCreateReadUpdateDeleteOpenapi(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.OPENAPI, false);
    }

    public static void testCreateReadUpdateDeleteAsyncapi(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.ASYNCAPI, false);
    }

    public static void testCreateReadUpdateDeleteGraphql(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.GRAPHQL, false);
    }

    public static void testCreateReadUpdateDeleteKconnect(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.KCONNECT, false);
    }

    public static void testCreateReadUpdateDeleteWsdl(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.WSDL, false);
    }

    public static void testCreateReadUpdateDeleteXsd(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.XSD, false);
    }

    public static void testCreateReadUpdateDeleteXml(ApicurioRegistry3 apicurioRegistry) {
        testCreateReadUpdateDelete(apicurioRegistry, null, null, ArtifactType.XML, false);
    }

    public static void testCreateReadUpdateDelete(
            ApicurioRegistry3 apicurioRegistry,
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
<<<<<<< HEAD
        String artifactContent = ArtifactContent.ARTIFACTS_BY_TYPE.get(artifactType);
        String updatedArtifactContent = ArtifactContent.UPDATED_ARTIFACTS_BY_TYPE.get(artifactType);
=======
        String artifactContent = artifacts.get(artifactType);
        String updatedArtifactContent = updatedArtifacts.get(artifactType);
>>>>>>> 9b97fabc7 (Fix(testsuite): Artifact type tests)
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
        Assertions.assertEquals(client.readArtifactContent(artifactGroupId, artifactId), artifactsPlain.get(artifactType));

        // Update artifact
        Assertions.assertTrue(client.updateArtifact(artifactGroupId, artifactId, updatedArtifactContent));
        // Check update of artifact
        Assertions.assertEquals(client.readArtifactContent(artifactGroupId, artifactId), artifactsUpdatedPlain.get(artifactType));

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
