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

public class AuthenticatedReads {
    public static void testAuthenticatedReads(ApicurioRegistry3 apicurioRegistry) {
        /* RUN PRE-TEST ACTIONS */

        // GET REGISTRY HOSTNAME
        // Wait for readiness of registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));
        // Get registry hostname
        String hostname = ApicurioRegistryUtils.getApicurioRegistryHostname(apicurioRegistry);

        // GET CONTROL API CLIENT
        // Create control API client for setup of registry before test
        ApicurioRegistryApiClient controlClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update control API client with it
        controlClient.setToken(
                KeycloakUtils.getAccessToken(apicurioRegistry, Constants.SSO_ADMIN_USER, Constants.SSO_USER_PASSWORD)
        );
        // Set authentication method to token
        controlClient.setAuthMethod(AuthMethod.TOKEN);
        // Wait for API availability
        Assertions.assertTrue(controlClient.waitServiceAvailable());

        // GET TEST API CLIENT
        // Create test API client for test actions
        ApicurioRegistryApiClient testClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update test API client with it
        testClient.setToken(
                KeycloakUtils.getAccessToken(apicurioRegistry, Constants.SSO_NO_ROLE_USER, Constants.SSO_USER_PASSWORD)
        );
        // Set authentication method to token
        testClient.setAuthMethod(AuthMethod.TOKEN);

        // PREPARE NECESSARY VARIABLES
        // Define artifact group ID
        String groupId = "authenticatedReadsTest";
        // Define artifact ID
        String id = "authenticated-reads-test";
        // Define artifact ID for create actions that should fail
        String failId = id + "-fail";
        // Define artifact type
        ArtifactType type = ArtifactType.JSON;
        // Define artifact initial content
        String initialContent = ArtifactContent.DEFAULT_AVRO;
        // Define artifact updated content
        String updatedContent = ArtifactContent.DEFAULT_AVRO_UPDATED;

        // PREPARE REGISTRY CONTENT
        // Create artifact for test
        Assertions.assertTrue(controlClient.createArtifact(groupId, id, type, initialContent));

        /* RUN TEST ACTIONS */

        // ENABLE ROLE-BASED AUTHORIZATION AND TEST DEFAULT VALUE (false) OF REGISTRY_AUTH_AUTHENTICATED_READS_ENABLED
        // Set environment variable APICURIO_AUTH_ROLE_BASED_AUTHORIZATION of deployment to true
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, new Env() {{
            setName("APICURIO_AUTH_ROLE_BASED_AUTHORIZATION");
            setValue("true");
        }});
        // Wait for API availability
        Assertions.assertTrue(controlClient.waitServiceAvailable());
        // Check that API returns 403 Forbidden when reading artifacts
        Assertions.assertTrue(testClient.checkUnauthorized(HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when creating artifact
        Assertions.assertTrue(
                testClient.createArtifact(groupId, failId, type, initialContent, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 403 Forbidden when updating artifact
        Assertions.assertTrue(testClient.updateArtifact(groupId, id, updatedContent, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when deleting artifact
        Assertions.assertTrue(testClient.deleteArtifact(groupId, id, HttpStatus.SC_FORBIDDEN));

        // ENABLE AUTHENTICATED READS IN REGISTRY AUTHENTICATION AND TEST IT
        // Set environment variable REGISTRY_AUTH_AUTHENTICATED_READS_ENABLED of deployment to true
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, new Env() {{
            setName("APICURIO_AUTH_AUTHENTICATED_READS_ENABLED");
            setValue("true");
        }});
        // Wait for API availability
        Assertions.assertTrue(controlClient.waitServiceAvailable());
        // Check that API returns 200 OK when reading artifacts
        Assertions.assertNotNull(testClient.listArtifacts());
        // Check that API returns 403 Forbidden when creating artifact
        Assertions.assertTrue(
                testClient.createArtifact(groupId, failId, type, initialContent, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 403 Forbidden when updating artifact
        Assertions.assertTrue(testClient.updateArtifact(groupId, id, updatedContent, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when deleting artifact
        Assertions.assertTrue(testClient.deleteArtifact(groupId, id, HttpStatus.SC_FORBIDDEN));

        // DISABLE AUTHENTICATED READS IN REGISTRY AUTHENTICATION AND TEST IT
        // Set environment variable REGISTRY_AUTH_AUTHENTICATED_READS_ENABLED of deployment to false
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, new Env() {{
            setName("APICURIO_AUTH_AUTHENTICATED_READS_ENABLED");
            setValue("false");
        }});
        // Wait for API availability
        Assertions.assertTrue(controlClient.waitServiceAvailable());
        // Check that API returns 403 Forbidden when reading artifacts
        Assertions.assertTrue(testClient.checkUnauthorized(HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when creating artifact
        Assertions.assertTrue(
                testClient.createArtifact(groupId, failId, type, initialContent, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 403 Forbidden when updating artifact
        Assertions.assertTrue(testClient.updateArtifact(groupId, id, updatedContent, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when deleting artifact
        Assertions.assertTrue(testClient.deleteArtifact(groupId, id, HttpStatus.SC_FORBIDDEN));
    }
}
