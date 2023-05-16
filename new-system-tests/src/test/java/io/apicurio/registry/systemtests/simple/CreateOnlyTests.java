package io.apicurio.registry.systemtests.simple;

import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.infra.Infra;
import io.apicurio.registry.systemtests.infra.SystemTestsExtension;
import io.apicurio.registry.systemtests.infra.matrix.Auth;
import io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity;
import io.apicurio.registry.systemtests.infra.matrix.Matrix;
import io.apicurio.registry.systemtests.todo.ArtifactContent;
import io.apicurio.registry.systemtests.todo.ArtifactType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static io.apicurio.registry.systemtests.infra.matrix.Auth.OIDC;
import static io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity.SCRAM;
import static io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity.TLS;
import static io.apicurio.registry.systemtests.infra.matrix.Storage.KAFKASQL;
import static io.apicurio.registry.systemtests.infra.matrix.Storage.SQL;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Jakub Senko <em>m@jsenko.net</em>
 */
@Matrix(
        storage = {SQL, KAFKASQL},
        auth = {Auth.NONE, OIDC},
        kafkaSecurity = {KafkaSecurity.NONE, TLS, SCRAM}
)
@ExtendWith({SystemTestsExtension.class})
class CreateOnlyTests extends TestBase {

    private Infra infra;

    @Test
    void test() {

        /*
         * When this test runs, we're assuming:
         * - The infrastructure is ready
         * - Infrastructure information is injected into `infra`
         */

        assertEquals("TODO", infra.getRegistryBaseUrl()); // TODO Remove

        String artifactGroupId = "registry-" + UUID.randomUUID();
        String artifactId = "registry-" + UUID.randomUUID();

        infra.restAssuredForRegistry()
                .header("X-Registry-ArtifactId", artifactId)
                .header("X-Registry-ArtifactType", ArtifactType.AVRO.name())
                .contentType("application/json") // TODO Constant
                .body(ArtifactContent.DEFAULT_AVRO)
                .post("/apis/registry/v2/groups/{groupId}/artifacts", artifactGroupId)
                .then()
                .statusCode(200);  // TODO Constant
    }
}
