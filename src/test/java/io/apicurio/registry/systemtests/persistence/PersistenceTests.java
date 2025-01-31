package io.apicurio.registry.systemtests.persistence;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.client.ArtifactType;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.persistence.features.CreateReadRestartReadDelete;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class PersistenceTests extends TestBase {
    protected void runCreateReadRestartReadDeleteTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadRestartReadDelete.testCreateReadRestartReadDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.AVRO,
                    true
            );
        } else {
            CreateReadRestartReadDelete.testCreateReadRestartReadDelete(
                    registry,
                    null,
                    null,
                    ArtifactType.AVRO,
                    false
            );
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS - PostgreSQL */

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("interop")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.SQL, null, true);
    }

    /* TESTS - KafkaSQL */

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("interop")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadRestartReadDelete() throws InterruptedException {
        runCreateReadRestartReadDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
}
