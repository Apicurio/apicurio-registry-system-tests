package io.apicurio.registry.systemtests.simple;

import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.infra.Infra;
import io.apicurio.registry.systemtests.infra.SystemTestsExtension;
import io.apicurio.registry.systemtests.infra.matrix.Auth;
import io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity;
import io.apicurio.registry.systemtests.infra.matrix.Matrix;
import io.apicurio.registry.systemtests.todo.ArtifactContent;
import io.apicurio.registry.systemtests.todo.ArtifactList;
import io.apicurio.registry.systemtests.todo.ArtifactType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static io.apicurio.registry.systemtests.infra.matrix.Auth.OIDC;
import static io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity.SCRAM;
import static io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity.TLS;
import static io.apicurio.registry.systemtests.infra.matrix.Storage.KAFKASQL;
import static io.apicurio.registry.systemtests.infra.matrix.Storage.SQL;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jakub Senko <em>m@jsenko.net</em>
 */
@Matrix(
        storage = {SQL, KAFKASQL},
        auth = {Auth.NONE, OIDC},
        kafkaSecurity = {KafkaSecurity.NONE, TLS, SCRAM}
)
@ExtendWith({SystemTestsExtension.class})
class CRUDTests extends TestBase {

    private Infra infra;

    @Test
    void test() {

        /*
         * When this test runs, we're assuming:
         * - The infrastructure is ready
         * - Infrastructure information is injected into `infra`
         */

        assertEquals("TODO", infra.getRegistryBaseUrl()); // TODO Remove

        // Prepare necessary variables
        String artifactGroupId = "registry-" + UUID.randomUUID();
        String artifactId = "registry-" + UUID.randomUUID();
        String artifactContent = ArtifactContent.DEFAULT_AVRO;
        String updatedArtifactContent = "{\"key\":\"id\"}";

        // List artifacts
        var artifactList = infra.restAssuredForRegistry()
                .queryParam("limit", 1000) // TODO Constant
                .get("/apis/registry/v2/search/artifacts")
                .then()
                .statusCode(200)
                .extract()
                .as(ArtifactList.class);  // TODO Constant

        assertNotNull(artifactList);

        // Check that artifact does not exist yet
        assertFalse(artifactList.contains(artifactGroupId, artifactId));

        // Create artifact
        infra.restAssuredForRegistry()
                .header("X-Registry-ArtifactId", artifactId)
                .header("X-Registry-ArtifactType", ArtifactType.AVRO.name())
                .contentType("application/json") // TODO Constant
                .body(ArtifactContent.DEFAULT_AVRO)
                .post("/apis/registry/v2/groups/{groupId}/artifacts", artifactGroupId)
                .then()
                .statusCode(200);  // TODO Constant

        // TODO Extract code repetition

        // List artifacts
        artifactList = infra.restAssuredForRegistry()
                .queryParam("limit", 1000) // TODO Constant
                .get("/apis/registry/v2/search/artifacts")
                .then()
                .statusCode(200) // TODO Constant
                .extract()
                .as(ArtifactList.class);

        assertNotNull(artifactList);

        // Check creation of artifact
        assertTrue(artifactList.contains(artifactGroupId, artifactId));

        var artifactContent2 = infra.restAssuredForRegistry()
                .queryParam("limit", 1000) // TODO Constant
                .get("/apis/registry/v2/groups/{groupId}/artifacts/{artifactId}", artifactGroupId, artifactId)
                .then()
                .statusCode(200) // TODO Constant
                .extract()
                .as(String.class);

        assertEquals(artifactContent, artifactContent2);

        // Update artifact
        infra.restAssuredForRegistry()
                .body(updatedArtifactContent)
                .put("/apis/registry/v2/groups/{groupId}/artifacts/{artifactId}", artifactGroupId, artifactId)
                .then()
                .statusCode(200); // TODO Constant

        // Check update of artifact
        artifactContent2 = infra.restAssuredForRegistry()
                .queryParam("limit", 1000) // TODO Constant
                .get("/apis/registry/v2/groups/{groupId}/artifacts/{artifactId}", artifactGroupId, artifactId)
                .then()
                .statusCode(200) // TODO Constant
                .extract()
                .as(String.class);

        assertEquals(updatedArtifactContent, artifactContent2);

        // Delete artifact
        infra.restAssuredForRegistry()
                .delete("/apis/registry/v2/groups/{groupId}/artifacts/{artifactId}", artifactGroupId, artifactId)
                .then()
                .statusCode(200); // TODO Constant


        // List artifacts
        artifactList = infra.restAssuredForRegistry()
                .queryParam("limit", 1000) // TODO Constant
                .get("/apis/registry/v2/search/artifacts")
                .then()
                .statusCode(200) // TODO Constant
                .extract()
                .as(ArtifactList.class);

        assertNotNull(artifactList);

        // Check deletion of artifact
        assertFalse(artifactList.contains(artifactGroupId, artifactId));

        /*
        // If we use token
        if (useToken) {
            // Check unauthorized API call
            assertTrue(client.checkUnauthorizedFake());
        }
        */
    }
}
