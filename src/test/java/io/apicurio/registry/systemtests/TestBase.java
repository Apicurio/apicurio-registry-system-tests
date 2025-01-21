package io.apicurio.registry.systemtests;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.Base64Utils;
import io.apicurio.registry.systemtests.framework.Certificate;
import io.apicurio.registry.systemtests.framework.CertificateUtils;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.DatabaseUtils;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.KafkaUtils;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.framework.TestNameGenerator;
import io.apicurio.registry.systemtests.operator.OperatorManager;
import io.apicurio.registry.systemtests.operator.types.KeycloakOLMOperatorType;
import io.apicurio.registry.systemtests.operator.types.StrimziClusterOLMOperatorType;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import io.apicurio.registry.systemtests.resolver.ExtensionContextParameterResolver;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.strimzi.api.kafka.model.kafka.Kafka;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@DisplayNameGeneration(TestNameGenerator.class)
@ExtendWith(ExtensionContextParameterResolver.class)
public abstract class TestBase {
    protected static Logger LOGGER = LoggerUtils.getLogger();
    protected final ResourceManager resourceManager = ResourceManager.getInstance();
    protected final OperatorManager operatorManager = OperatorManager.getInstance();

    /* Function to set all necessary variables for test subclasses */

    public abstract void setupTestClass();

    /* Constructor for all test subclasses */

    public TestBase() {
        setupTestClass();
    }

    @BeforeAll
    protected void beforeAllTests() throws InterruptedException, IOException {
        if (Environment.DEPLOY_KEYCLOAK) {
            // Install Keycloak operator
            LoggerUtils.logDelimiter("#");
            LOGGER.info("Deploying shared keycloak operator and instance...");
            LoggerUtils.logDelimiter("#");

            DatabaseUtils.createKeycloakPostgresqlDatabaseSecret();

            DatabaseUtils.deployKeycloakPostgresqlDatabase();

            KeycloakOLMOperatorType keycloakOLMOperator = new KeycloakOLMOperatorType(Environment.SSO_CHANNEL);
            operatorManager.installOperatorShared(keycloakOLMOperator);
            KeycloakUtils.deployKeycloak();
            Thread.sleep(Duration.ofMinutes(2).toMillis());
        }
        LoggerUtils.logDelimiter("#");
        LOGGER.info("Deploying shared strimzi operator...");
        LoggerUtils.logDelimiter("#");

        StrimziClusterOLMOperatorType strimziOperator = new StrimziClusterOLMOperatorType();
        operatorManager.installOperatorShared(strimziOperator);

        LoggerUtils.logDelimiter("#");
        LOGGER.info("Creating SSL truststore...");
        LoggerUtils.logDelimiter("#");

        /* */
        Secret routerCertsDefaultSecret = Kubernetes.getSecret("openshift-config-managed", Constants.ROUTER_CERTS);
        String clusterBaseUrl = Objects.requireNonNull(
                Kubernetes.getRouteHost("openshift-console", "console")
        ).replace("console-openshift-console.", "");
        ArrayList<Certificate> certificates = CertificateUtils.readCertificates(
                Base64Utils.decode(routerCertsDefaultSecret.getData().get(clusterBaseUrl))
        );
        Secret newSecret = new SecretBuilder()
                .withNewMetadata()
                .withName(routerCertsDefaultSecret.getMetadata().getName())
                .withNamespace(Environment.NAMESPACE)
                .endMetadata()
                .withType("Opaque")
                .withData(new HashMap<>() {{
                    put(Constants.TRUSTSTORE_DATA_NAME, Base64Utils.encode(CertificateUtils.getCertificates(certificates)));
                }})
                .build();

        resourceManager.createSharedResource(true, newSecret);
        /* */

        CertificateUtils.createSslTruststore(
                Environment.NAMESPACE,
                Constants.ROUTER_CERTS,
                Constants.TRUSTSTORE_SECRET_NAME,
                Constants.TRUSTSTORE_DATA_NAME,
                true
        );

        LoggerUtils.logDelimiter("#");
        LOGGER.info("Deployment of shared resources is done.");
        LoggerUtils.logDelimiter("#");
    }

    @AfterAll
    protected void afterAllTests() throws InterruptedException {
        if (Environment.DELETE_RESOURCES) {
            LoggerUtils.logDelimiter("#");
            LOGGER.info("Cleaning shared resources...");
            LoggerUtils.logDelimiter("#");
            resourceManager.deleteKafka();
            if (Environment.DEPLOY_KEYCLOAK) {
                KeycloakUtils.removeKeycloak(Environment.NAMESPACE);
                Thread.sleep(Duration.ofMinutes(2).toMillis());
            }
            operatorManager.uninstallSharedOperators();
            resourceManager.deleteSharedResources();
            LoggerUtils.logDelimiter("#");
            LOGGER.info("Cleaning of shared resources done.");
            LoggerUtils.logDelimiter("#");
        } else {
            LoggerUtils.logDelimiter("#");
            LOGGER.info("NOT cleaning shared resources!");
            LoggerUtils.logDelimiter("#");
        }
    }

    @BeforeEach
    protected void beforeEachTest(ExtensionContext testContext) {
        LoggerUtils.logDelimiter("#");
        LOGGER.info(
                "[TEST-START] {}.{}-STARTED",
                testContext.getTestClass().get().getName(),
                testContext.getTestMethod().get().getName()
        );
        LoggerUtils.logDelimiter("#");
        LOGGER.info("");
    }

    @AfterEach
    protected void afterEachTest(ExtensionContext testContext) {
        if (Environment.DELETE_RESOURCES) {
            resourceManager.deleteResources();

            operatorManager.uninstallOperators();
        } else {
            LoggerUtils.logDelimiter("#");
            LOGGER.info("NOT cleaning test resources!");
            LoggerUtils.logDelimiter("#");
        }

        LOGGER.info("");
        LoggerUtils.logDelimiter("#");
        LOGGER.info(
                "[TEST-END] {}.{}-FINISHED",
                testContext.getTestClass().get().getName(),
                testContext.getTestMethod().get().getName()
        );
        LoggerUtils.logDelimiter("#");
    }

    protected ApicurioRegistry deployTestRegistry(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        if (persistenceKind.equals(PersistenceKind.SQL)) {
            // Deploy PostgreSQL with/without Keycloak
            DatabaseUtils.deployDefaultPostgresqlDatabase();

            return ApicurioRegistryUtils.deployDefaultApicurioRegistrySql(useKeycloak);
        } else if (persistenceKind.equals(PersistenceKind.KAFKA_SQL)) {
            Kafka kafka;

            // Deploy Kafka
            if (kafkaKind.equals(KafkaKind.NO_AUTH)) {
                // Deploy noAuthKafka
                KafkaUtils.deployDefaultKafkaNoAuth();

                return ApicurioRegistryUtils.deployDefaultApicurioRegistryKafkasqlNoAuth(useKeycloak);
            } else if (kafkaKind.equals(KafkaKind.TLS)) {
                // Deploy tlsKafka
                kafka = KafkaUtils.deployDefaultKafkaTls();

                return ApicurioRegistryUtils.deployDefaultApicurioRegistryKafkasqlTLS(kafka, useKeycloak);
            } else if (kafkaKind.equals(KafkaKind.SCRAM)) {
                // Deploy scramKafka
                kafka = KafkaUtils.deployDefaultKafkaScram();

                return ApicurioRegistryUtils.deployDefaultApicurioRegistryKafkasqlSCRAM(
                        kafka,
                        useKeycloak
                );
            } else {
                LOGGER.error("Unrecognized KafkaKind: {}.", kafkaKind);

                return null;
            }
        } else if (persistenceKind.equals(PersistenceKind.MEM)) {
            // TODO: Deploy mem with/without Keycloak
            LOGGER.error("Deployment with mem persistence is not supported yet.");

            return null;
        } else {
            LOGGER.error("Unrecognized PersistenceKind: {}.", persistenceKind);

            return null;
        }
    }
}
