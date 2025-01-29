package io.apicurio.registry.systemtests.api;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.api.features.CreateReadUpdateDelete;
import io.apicurio.registry.systemtests.client.ArtifactType;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class APITests extends TestBase {
    /* TEST RUNNERS */

    protected void runCreateReadUpdateDeleteTestAvro(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.AVRO,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.AVRO, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestProtobuf(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.PROTOBUF,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.PROTOBUF, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestJson(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.JSON,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.JSON, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestOpenapi(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.OPENAPI,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.OPENAPI, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestAsyncapi(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.ASYNCAPI,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.ASYNCAPI, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestGraphql(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.GRAPHQL,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.GRAPHQL, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestKconnect(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.KCONNECT,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.KCONNECT, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestWsdl(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.WSDL,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.WSDL, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestXsd(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.XSD,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.XSD, false);
        }
    }

    protected void runCreateReadUpdateDeleteTestXml(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    ArtifactType.XML,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, ArtifactType.XML, false);
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS AVRO - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("interop")
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.SQL, null, true);
    }

    /* TESTS AVRO - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("interop")
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteAvro() throws InterruptedException {
        runCreateReadUpdateDeleteTestAvro(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS PROTOBUF - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.SQL, null, true);
    }

    /* TESTS PROTOBUF - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteProtobuf() throws InterruptedException {
        runCreateReadUpdateDeleteTestProtobuf(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS JSON - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.SQL, null, true);
    }

    /* TESTS JSON - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteJson() throws InterruptedException {
        runCreateReadUpdateDeleteTestJson(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS OPENAPI - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.SQL, null, true);
    }

    /* TESTS OPENAPI - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteOpenapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestOpenapi(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS ASYNCAPI - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.SQL, null, true);
    }

    /* TESTS ASYNCAPI - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteAsyncapi() throws InterruptedException {
        runCreateReadUpdateDeleteTestAsyncapi(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS GRAPHQL - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.SQL, null, true);
    }

    /* TESTS GRAPHQL - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteGraphql() throws InterruptedException {
        runCreateReadUpdateDeleteTestGraphql(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS KCONNECT - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.SQL, null, true);
    }

    /* TESTS KCONNECT - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteKconnect() throws InterruptedException {
        runCreateReadUpdateDeleteTestKconnect(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS WSDL - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.SQL, null, true);
    }

    /* TESTS WSDL - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteWsdl() throws InterruptedException {
        runCreateReadUpdateDeleteTestWsdl(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS XSD - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.SQL, null, true);
    }

    /* TESTS XSD - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteXsd() throws InterruptedException {
        runCreateReadUpdateDeleteTestXsd(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /* TESTS XML - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("smoke")
    @Tag("sql")
    public void testRegistrySqlKeycloakCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.SQL, null, true);
    }

    /* TESTS XML - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("smoke")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDeleteXml() throws InterruptedException {
        runCreateReadUpdateDeleteTestXml(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
}
