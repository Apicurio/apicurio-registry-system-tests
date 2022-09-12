package io.apicurio.registry.systemtests.api;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.api.features.CreateArtifact;
import io.apicurio.registry.systemtests.api.features.CreateReadDelete;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class APITests extends TestBase {
    /* TEST RUNNERS */

    protected void runCreateReadDeleteTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadDelete.testCreateReadDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    true
            );
        } else {
            CreateReadDelete.testCreateReadDelete(registry, null, null, false);
        }
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runCreateArtifactTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateArtifact.testCreateArtifact(registry, Constants.SSO_ADMIN_USER, Constants.SSO_USER_PASSWORD, true);
        } else {
            CreateArtifact.testCreateArtifact(registry, null, null, false);
        }
    }

    /* TESTS - PostgreSQL */

    @Test
    public void testRegistrySqlNoIAMCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.SQL, null, false);
    }

    @Test
    public void testRegistrySqlKeycloakCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.SQL, null, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlNoIAMCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.SQL, null, false);
    }

    @Test
    public void testRegistrySqlKeycloakCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.SQL, null, true);
    }

    /* TESTS - KafkaSQL */

    @Test
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    public void testRegistryKafkasqlTLSNoIAMCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadDelete(ExtensionContext testContext) throws InterruptedException {
        runCreateReadDeleteTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistryKafkasqlNoAuthNoIAMCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    public void testRegistryKafkasqlNoAuthKeycloakCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    public void testRegistryKafkasqlTLSNoIAMCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    public void testRegistryKafkasqlSCRAMNoIAMCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakCreateArtifact(ExtensionContext testContext) throws InterruptedException {
        runCreateArtifactTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
}
