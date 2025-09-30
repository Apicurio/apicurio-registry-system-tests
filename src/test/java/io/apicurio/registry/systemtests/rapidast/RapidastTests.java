package io.apicurio.registry.systemtests.rapidast;

import io.apicur.registry.v1.ApicurioRegistry3;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.executor.Exec;
import io.apicurio.registry.systemtests.framework.RapidastUtils;
import io.apicurio.registry.systemtests.framework.TextFileUtils;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class RapidastTests extends TestBase {
    /* UNAUTHENTICATED Tests */

    @Test
    @Tag("unauthenticated")
    @Tag("v2")
    @Tag("v3")
    @Tag("debug")
    @Tag("v3-beta")
    public void testRapidastRegistryV2Unauthenticated() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v2_unauthenticated.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("unauthenticated")
    @Tag("v3")
    @Tag("v3-beta")
    public void testRapidastRegistryV3Unauthenticated() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v3_unauthenticated.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("unauthenticated")
    @Tag("v2")
    public void testRapidastRegistryV1Unauthenticated() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v1_unauthenticated.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("unauthenticated")
    @Tag("v2")
    public void testRapidastCcompatV6Unauthenticated() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v6_unauthenticated.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("unauthenticated")
    @Tag("v2")
    @Tag("v3")
    @Tag("v3-beta")
    public void testRapidastCcompatV7Unauthenticated() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v7_unauthenticated.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("unauthenticated")
    @Tag("v2")
    public void testRapidastCncfV0Unauthenticated() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("cncf_v0_unauthenticated.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    /* HTTP Basic Tests */

    @Test
    @Tag("http-basic")
    @Tag("v2")
    @Tag("v3")
    public void testRapidastRegistryV2HttpBasic() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v2_http_basic.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, true);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("http-basic")
    @Tag("v3")
    public void testRapidastRegistryV3HttpBasic() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v3_http_basic.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, true);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("http-basic")
    @Tag("v2")
    public void testRapidastRegistryV1HttpBasic() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v1_http_basic.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, true);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("http-basic")
    @Tag("v2")
    public void testRapidastCcompatV6HttpBasic() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v6_http_basic.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, true);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("http-basic")
    @Tag("v2")
    @Tag("v3")
    public void testRapidastCcompatV7HttpBasic() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v7_http_basic.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, true);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }

    @Test
    @Tag("http-basic")
    @Tag("v2")
    public void testRapidastCncfV0HttpBasic() throws InterruptedException {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("cncf_v0_http_basic.yaml");
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, true);

        // Get registry hostname
        String hostname = Kubernetes.getRouteByPrefixHost(
                // Use registry namespace
                apicurioRegistry.getMetadata().getNamespace(),
                // Use registry name for prefix
                apicurioRegistry.getMetadata().getName() + "-app-ingress"
        );

        // Replace placeholder in config file with registry hostname
        TextFileUtils.replaceInFile(configFilePath, "<hostname>", hostname);

        LOGGER.info("Config file path: " + configFilePath);
        LOGGER.info("Hostname: " + hostname);

        // Log current action
        LOGGER.info("Running RapiDAST...");

        // Execute RapiDAST with updated config file
        Exec.executeAndCheck("/usr/bin/python", "../rapidast/rapidast.py", "--config", configFilePath);

        // Log current action
        LOGGER.info("RapiDAST finished.");
    }
}
