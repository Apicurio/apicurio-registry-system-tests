package io.apicurio.registry.systemtests;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.Base64Utils;
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
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Secret;
import io.strimzi.api.kafka.model.Kafka;
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
import java.util.HashMap;

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

        Secret dbSecret = new Secret();
        dbSecret.setMetadata(new ObjectMeta() {{
            setName(Constants.SSO_DB_SECRET_NAME);
            setNamespace(Environment.NAMESPACE);
        }});
        dbSecret.setType("Opaque");
        dbSecret.setData(new HashMap<>() {{
            put("password", Base64Utils.encode(Constants.DB_PASSWORD));
            put("username", Base64Utils.encode(Constants.DB_USERNAME));
        }});

        resourceManager.createResource(true, dbSecret);

        DatabaseUtils.deployPostgresqlDatabase("keycloak-db", Environment.NAMESPACE, "keycloak");

        KeycloakOLMOperatorType keycloakOLMOperator = new KeycloakOLMOperatorType("fast");
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
        Secret routerCertsDefaultSecret = Kubernetes.getSecret("openshift-ingress", Constants.OAUTH_KAFKA_ROUTER_CERTS);
        Secret newSecret = new Secret();

        newSecret.setMetadata(new ObjectMeta() {{
            setName(routerCertsDefaultSecret.getMetadata().getName());
            setNamespace(Environment.NAMESPACE);
        }});
        newSecret.setType("kubernetes.io/tls");
        newSecret.setData(routerCertsDefaultSecret.getData());

        resourceManager.createResource(true, newSecret);

        KafkaUtils.deployDefaultOAuthKafka();

        Kubernetes.createSecret(Environment.NAMESPACE, new Secret() {{
            setMetadata(new ObjectMeta() {{
                setName("console-ui-secrets");
                setNamespace(Environment.NAMESPACE);
            }});
            setData(new HashMap<>() {{
                put("REGISTRY_CLIENT_ID", Base64Utils.encode("kafka"));
                put("REGISTRY_CLIENT_SECRET", Base64Utils.encode("**********"));
            }});
        }});

        return ApicurioRegistryUtils.deployDefaultApicurioRegistryOAuthKafka();
    }
}
