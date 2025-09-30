package io.apicurio.registry.systemtests.auth.features;

import io.apicur.registry.v1.ApicurioRegistry3;
import io.apicur.registry.v1.apicurioregistry3spec.app.Env;
import io.apicurio.registry.systemtests.client.ApicurioRegistryApiClient;
import io.apicurio.registry.systemtests.client.ArtifactContent;
import io.apicurio.registry.systemtests.client.ArtifactType;
import io.apicurio.registry.systemtests.client.AuthMethod;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;

public class ArtifactOwnerOnlyAuthorization {
    public static void testArtifactOwnerOnlyAuthorization(ApicurioRegistry3 apicurioRegistry) {
        /* RUN PRE-TEST ACTIONS */

        // GET REGISTRY HOSTNAME
        // Wait for readiness of registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));
        // Get registry hostname
        String hostname = ApicurioRegistryUtils.getApicurioRegistryHostname(apicurioRegistry);

        // GET OWNER API CLIENT
        // Create owner API client
        ApicurioRegistryApiClient ownerClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update owner API client with it
        ownerClient.setToken(
                KeycloakUtils.getAccessToken(apicurioRegistry, Constants.SSO_ADMIN_USER, Constants.SSO_USER_PASSWORD)
        );
        // Set authentication method of owner API client
        ownerClient.setAuthMethod(AuthMethod.TOKEN);
        // Wait for API availability
        Assertions.assertTrue(ownerClient.waitServiceAvailable());

        // GET TEST API CLIENT
        // Create test API client
        ApicurioRegistryApiClient testClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update test API client with it
        testClient.setToken(
                KeycloakUtils.getAccessToken(
                        apicurioRegistry,
                        Constants.SSO_DEVELOPER_USER,
                        Constants.SSO_USER_PASSWORD
                )
        );
        // Set authentication method of test API client
        testClient.setAuthMethod(AuthMethod.TOKEN);

        // PREPARE NECESSARY VARIABLES
        // Define artifact group ID
        String groupId = "artifactOwnerOnlyAuthorizationTest";
        // Define artifact ID
        String id = "artifact-owner-only-authorization-test";
        // Define artifact ID prefix for actions that should succeed
        String succeedId = id + "-succeed-";
        // Define artifact type
        ArtifactType type = ArtifactType.JSON;
        // Define artifact initial content
        String initialContent = ArtifactContent.DEFAULT_AVRO;
        // Define artifact updated content
        String updatedContent = ArtifactContent.DEFAULT_AVRO_UPDATED;

        /* RUN TEST ACTIONS */

        // TEST DEFAULT VALUE OF REGISTRY_AUTH_OBAC_ENABLED (false)
        // Define artifact ID for the owner part
        String testArtifactId = succeedId + "default-by-owner";
        // Wait for API availability
        Assertions.assertTrue(ownerClient.waitServiceAvailable());
        // Check that API returns 200 OK when reading artifacts by owner
        Assertions.assertNotNull(ownerClient.listArtifacts());
        // Check that API returns 200 OK when creating artifact by owner
        Assertions.assertTrue(ownerClient.createArtifact(groupId, testArtifactId, type, initialContent));
        // Check that API returns 200 OK when updating artifact by owner
        Assertions.assertTrue(ownerClient.updateArtifact(groupId, testArtifactId, updatedContent));
        // Check that API returns 204 No Content when deleting artifact by owner
        Assertions.assertTrue(ownerClient.deleteArtifact(groupId, testArtifactId));
        // Define artifact ID for the test part
        testArtifactId = succeedId + "default-by-test";
        // Check that API returns 200 OK when creating artifact by owner
        Assertions.assertTrue(ownerClient.createArtifact(groupId, testArtifactId, type, initialContent));
        // Check that API returns 200 OK when reading artifacts by test
        Assertions.assertNotNull(testClient.listArtifacts());
        // Check that API returns 200 OK when updating artifact by test
        Assertions.assertTrue(testClient.updateArtifact(groupId, testArtifactId, updatedContent));
        // Check that API returns 204 No Content when deleting artifact by test
        Assertions.assertTrue(testClient.deleteArtifact(groupId, testArtifactId));

        // ENABLE ARTIFACT OWNER ONLY AUTHORIZATION AND TEST IT
        // Set environment variable REGISTRY_AUTH_OBAC_ENABLED of deployment to true
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, new Env() {{
            setName("APICURIO_AUTH_OWNER-ONLY-AUTHORIZATION");
            setValue("true");
        }});

        // Define artifact ID for the owner part
        testArtifactId = succeedId + "true-by-owner";
        // Wait for API availability
        Assertions.assertTrue(ownerClient.waitServiceAvailable());
        // Check that API returns 200 OK when reading artifacts by owner
        Assertions.assertNotNull(ownerClient.listArtifacts());
        // Check that API returns 200 OK when creating artifact by owner
        Assertions.assertTrue(ownerClient.createArtifact(groupId, testArtifactId, type, initialContent));
        // Check that API returns 200 OK when updating artifact by owner
        Assertions.assertTrue(ownerClient.updateArtifact(groupId, testArtifactId, updatedContent));
        // Check that API returns 204 No Content when deleting artifact by owner
        Assertions.assertTrue(ownerClient.deleteArtifact(groupId, testArtifactId));
        // Define artifact ID for the test part
        testArtifactId = succeedId + "true-by-test";
        // Check that API returns 200 OK when creating artifact by owner
        Assertions.assertTrue(ownerClient.createArtifact(groupId, testArtifactId, type, initialContent));
        // Check that API returns 200 OK when reading artifacts by test
        Assertions.assertNotNull(testClient.listArtifacts());
        // Check that API returns 403 Forbidden when updating artifact by test
        Assertions.assertTrue(
                testClient.updateArtifact(groupId, testArtifactId, updatedContent, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 403 Forbidden when deleting artifact by test
        Assertions.assertTrue(testClient.deleteArtifact(groupId, testArtifactId, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 204 No Content when deleting artifact by owner
        Assertions.assertTrue(ownerClient.deleteArtifact(groupId, testArtifactId));

        // DISABLE ARTIFACT OWNER ONLY AUTHORIZATION AND TEST IT
        // Set environment variable REGISTRY_AUTH_OBAC_ENABLED of deployment to false
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, new Env() {{
            setName("APICURIO_AUTH_OWNER-ONLY-AUTHORIZATION");
            setValue("false");
        }});
        // Define artifact ID for the owner part
        testArtifactId = succeedId + "false-by-owner";
        // Wait for API availability
        Assertions.assertTrue(ownerClient.waitServiceAvailable());
        // Check that API returns 200 OK when reading artifacts by owner
        Assertions.assertNotNull(ownerClient.listArtifacts());
        // Check that API returns 200 OK when creating artifact by owner
        Assertions.assertTrue(ownerClient.createArtifact(groupId, testArtifactId, type, initialContent));
        // Check that API returns 200 OK when updating artifact by owner
        Assertions.assertTrue(ownerClient.updateArtifact(groupId, testArtifactId, updatedContent));
        // Check that API returns 204 No Content when deleting artifact by owner
        Assertions.assertTrue(ownerClient.deleteArtifact(groupId, testArtifactId));
        // Define artifact ID for the test part
        testArtifactId = succeedId + "false-by-test";
        // Check that API returns 200 OK when creating artifact by owner
        Assertions.assertTrue(ownerClient.createArtifact(groupId, testArtifactId, type, initialContent));
        // Check that API returns 200 OK when reading artifacts by test
        Assertions.assertNotNull(testClient.listArtifacts());
        // Check that API returns 200 OK when updating artifact by test
        Assertions.assertTrue(testClient.updateArtifact(groupId, testArtifactId, updatedContent));
        // Check that API returns 204 No Content when deleting artifact by test
        Assertions.assertTrue(testClient.deleteArtifact(groupId, testArtifactId));
    }
}
