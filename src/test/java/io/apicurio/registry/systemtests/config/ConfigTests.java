package io.apicurio.registry.systemtests.config;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.PodExecResult;
import io.apicurio.registry.systemtests.framework.PodUtils;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.resources.ApicurioRegistryResourceType;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import io.apicurio.registry.systemtests.registryinfra.resources.SecretResourceType;
import io.fabric8.kubernetes.api.model.Pod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("config")
public abstract class ConfigTests extends TestBase {
    @Test
    @Tag("https")
    @Tag("smoke")
    public void testConfigHttps() throws InterruptedException, ExecutionException, TimeoutException {
        // Deploy registry with PostgreSQL storage and without Keycloak
        ApicurioRegistry apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);
        // Create HTTPS secret
        resourceManager.createResource(true, SecretResourceType.getDefaultHttpsSecret());
        // Log current action
        LOGGER.info("Updating registry to use HTTPS with secret...");
        // Update registry to enable HTTPS and use HTTPS secret
        ApicurioRegistryResourceType.updateWithDefaultHttpsSecret(apicurioRegistry);
        // Log current action
        LOGGER.info("Re-creating registry to use HTTPS with secret...");
        // Re-create registry with HTTPS
        Kubernetes.createOrReplaceResources(Environment.NAMESPACE, Collections.singletonList(apicurioRegistry));
        // Wait for re-creation of registry
        Thread.sleep(13_000);
        // Wait for registry to be ready
        ApicurioRegistryUtils.waitApicurioRegistryReady(apicurioRegistry);
        // Log current action
        LOGGER.info("Registry should be updated to use HTTPS with secret now.");
        // Get registry pod
        Pod pod = Kubernetes.getPodByPrefix(Environment.NAMESPACE, Constants.REGISTRY + "-deployment");
        // Get registry service IP address
        String clusterIp = Kubernetes.getServiceClusterIp(Environment.NAMESPACE, Constants.REGISTRY + "-service");
        // Run curl command on pod to test HTTPS connection
        PodExecResult result = PodUtils.execCommandOnPod(
                pod, Environment.NAMESPACE, LOGGER, "curl", "-k", "https://" + clusterIp + ":8443/health", "-f"
        );
        // Log command result
        result.logResult(LOGGER);
        // Check if command succeeded
        Assertions.assertEquals(0, (int) result.getExitCode());
    }
}
