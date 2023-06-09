package io.apicurio.registry.systemtests;

import io.apicur.registry.v1.ApicurioRegistry;
import io.fabric8.junit.jupiter.api.KubernetesTest;
import io.fabric8.junit.jupiter.api.LoadKubernetesManifests;
import io.fabric8.openshift.client.OpenShiftClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.apicurio.registry.systemtests.Utils.findOperatorDeployment;
import static io.apicurio.registry.systemtests.Utils.isDeploymentReady;
import static io.apicurio.registry.systemtests.Utils.waitDeploymentExists;
import static io.apicurio.registry.systemtests.Utils.waitDeploymentReady;
import static io.apicurio.registry.systemtests.Utils.waitKafkaReady;
import static org.junit.jupiter.api.Assertions.assertTrue;

@KubernetesTest
@LoadKubernetesManifests({
        // Shared resources
        "/common/00_operator_group.yaml", // Operator group for installed operators in namespace
        // Keycloak IAM
        "/keycloak/00_service.yaml", // Service
        "/keycloak/01_realm.yaml", // Keycloak realm as realm.json in ConfigMap
        "/keycloak/02_deployment.yaml", // Deployment of Keycloak itself
        // Strimzi Kafka operator
        "/kafkasql/01_subscription.yaml", // Strimzi Kafka operator subscription
        // Kafka instance
        "/kafkasql/02_no_auth.yaml", // Kafka instance without authentication
        // Apicurio Registry operator
        "/apicurio/01_subscription.yaml", // Apicurio Registry operator subscription
        // Apicurio Registry instance
        "/apicurio/02_registry_kafkasql_no_auth_keycloak.yaml" // Apicurio Registry instance with KafkaSQL storage and Keycloak IAM
})
public class KafkasqlNoAuthKeycloak extends TestBase {
    /** {@link OpenShiftClient} instance for tests. */
    private OpenShiftClient client;

    /**
     * Performs actions that should be done before each test.
     */
    @BeforeEach
    public void beforeEach() {
        // Log information about current action
        logger.info("Running BeforeEach actions.");

        // Wait for readiness of Keycloak deployment
        assertTrue(waitDeploymentReady(client, Constants.KEYCLOAK_NAME));

        // Wait for existence of Strimzi Kafka operator deployment
        assertTrue(waitDeploymentExists(client, Constants.KAFKA_OPERATOR_NAME));

        // Wait for readiness of Strimzi Kafka operator deployment
        assertTrue(waitDeploymentReady(
                client,
                findOperatorDeployment(client, Constants.KAFKA_OPERATOR_NAME).getMetadata().getName())
        );

        // Wait for readiness of Kafka instance
        assertTrue(waitKafkaReady(client, Constants.KAFKA_NAME));

        // Wait for readiness of Apicurio Registry operator deployment
        assertTrue(waitDeploymentReady(
                client,
                findOperatorDeployment(client, Constants.REGISTRY_OPERATOR_NAME).getMetadata().getName())
        );

        // Log information about current action
        logger.info("BeforeEach finished.");
    }

    /**
     * Tests that {@link ApicurioRegistry} with KafkaSQL storage and Keycloak IAM becomes ready.
     */
    @Test
    public void testDeploy() {
        // Log information about current action
        logger.info("Running testDeploy test.");

        // Wait for readiness of Apicurio Registry instance with KafkaSQL storage deployment and Keycloak IAM
        assertTrue(waitDeploymentReady(client, Constants.REGISTRY_NAME + "-deployment"));

        // Check readiness of Apicurio Registry instance with KafkaSQL storage deployment and Keycloak IAM
        assertTrue(isDeploymentReady(client, Constants.REGISTRY_NAME + "-deployment"));

        // Log information about current action
        logger.info("testDeploy test finished.");
    }
}
