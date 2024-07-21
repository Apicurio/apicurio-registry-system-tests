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
import io.apicurio.registry.systemtests.resolver.ExtensionContextParameterResolver;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
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
public abstract class TestBaseOAuthKafka {
    protected static Logger LOGGER = LoggerUtils.getLogger();
    protected final ResourceManager resourceManager = ResourceManager.getInstance();
    protected final OperatorManager operatorManager = OperatorManager.getInstance();

    /* Function to set all necessary variables for test subclasses */

    public abstract void setupTestClass();

    /* Constructor for all test subclasses */

    public TestBaseOAuthKafka() {
        setupTestClass();
    }

    @BeforeAll
    protected void beforeAllTests() throws InterruptedException, IOException {
        // Install Keycloak operator
        LoggerUtils.logDelimiter("#");
        LOGGER.info("Deploying shared keycloak operator and instance!");
        LoggerUtils.logDelimiter("#");

        DatabaseUtils.createKeycloakPostgresqlDatabaseSecret();

        DatabaseUtils.deployKeycloakPostgresqlDatabase();

        KeycloakOLMOperatorType keycloakOLMOperator = new KeycloakOLMOperatorType(Environment.SSO_CHANNEL);
        operatorManager.installOperatorShared(keycloakOLMOperator);
        KeycloakUtils.deployOAuthKafkaKeycloak();
        Thread.sleep(Duration.ofMinutes(2).toMillis());
        LoggerUtils.logDelimiter("#");
        LOGGER.info("Deploying shared strimzi operator");
        LoggerUtils.logDelimiter("#");

        StrimziClusterOLMOperatorType strimziOperator = new StrimziClusterOLMOperatorType();
        operatorManager.installOperatorShared(strimziOperator);

        LoggerUtils.logDelimiter("#");
        LOGGER.info("Deployment of shared resources is done!");
        LoggerUtils.logDelimiter("#");
    }

    @AfterAll
    protected void afterAllTests() throws InterruptedException {
        LoggerUtils.logDelimiter("#");
        LOGGER.info("Cleaning shared resources!");
        LoggerUtils.logDelimiter("#");
        resourceManager.deleteKafka();
        KeycloakUtils.removeOAuthKafkaKeycloak(Environment.NAMESPACE);
        Thread.sleep(Duration.ofMinutes(2).toMillis());
        operatorManager.uninstallSharedOperators();
        resourceManager.deleteSharedResources();
        LoggerUtils.logDelimiter("#");
        LOGGER.info("Cleaning done!");
        LoggerUtils.logDelimiter("#");
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
        resourceManager.deleteResources();

        operatorManager.uninstallOperators();

        LOGGER.info("");
        LoggerUtils.logDelimiter("#");
        LOGGER.info(
                "[TEST-END] {}.{}-FINISHED",
                testContext.getTestClass().get().getName(),
                testContext.getTestMethod().get().getName()
        );
        LoggerUtils.logDelimiter("#");
    }

    protected ApicurioRegistry deployOAuthKafkaTestRegistry() throws InterruptedException {
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
                .withType("kubernetes.io/tls")
                .withData(new HashMap<>() {{
                    put("tls.crt", Base64Utils.encode(CertificateUtils.getCertificates(certificates)));
                    put("tls.key", Base64Utils.encode(CertificateUtils.getKeys(certificates)));
                }})
                .build();
        Secret consoleUiSecret = new SecretBuilder()
                .withNewMetadata()
                .withName("console-ui-secrets")
                .withNamespace(Environment.NAMESPACE)
                .endMetadata()
                .withData(new HashMap<>() {{
                    put("REGISTRY_CLIENT_ID", Base64Utils.encode("kafka"));
                    put("REGISTRY_CLIENT_SECRET", Base64Utils.encode("**********"));
                }})
                .build();

        resourceManager.createResource(true, newSecret);

        KafkaUtils.deployDefaultOAuthKafka();

        resourceManager.createResource(true, consoleUiSecret);

        return ApicurioRegistryUtils.deployDefaultApicurioRegistryOAuthKafka();
    }
}
