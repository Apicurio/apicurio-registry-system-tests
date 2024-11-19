package io.apicurio.registry.systemtests.rapidast;

import io.apicurio.registry.systemtests.executor.Exec;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.framework.RapidastUtils;
import io.apicurio.registry.systemtests.framework.TestNameGenerator;
import io.apicurio.registry.systemtests.framework.TextFileUtils;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.resolver.ExtensionContextParameterResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;

@Tag("rapidast-static")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayNameGeneration(TestNameGenerator.class)
@ExtendWith(ExtensionContextParameterResolver.class)
public class StaticRapidastTests {
    protected static Logger LOGGER = LoggerUtils.getLogger();

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
        LOGGER.info("");
        LoggerUtils.logDelimiter("#");
        LOGGER.info(
                "[TEST-END] {}.{}-FINISHED",
                testContext.getTestClass().get().getName(),
                testContext.getTestMethod().get().getName()
        );
        LoggerUtils.logDelimiter("#");
    }

    /* UNAUTHENTICATED Tests */

    @Test
    @Tag("unauthenticated")
    @Tag("v2")
    @Tag("v3")
    public void testRapidastRegistryV2Unauthenticated() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v2_unauthenticated.yaml");

        LOGGER.info("Environment.NAMESPACE: {}", Environment.NAMESPACE);
        LOGGER.info("Environment.REGISTRY_ROUTE: {}", Environment.REGISTRY_ROUTE);

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastRegistryV3Unauthenticated() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v3_unauthenticated.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastRegistryV1Unauthenticated() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v1_unauthenticated.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastCcompatV6Unauthenticated() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v6_unauthenticated.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastCcompatV7Unauthenticated() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v7_unauthenticated.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastCncfV0Unauthenticated() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("cncf_v0_unauthenticated.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastRegistryV2HttpBasic() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v2_http_basic.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastRegistryV3HttpBasic() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v3_http_basic.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastRegistryV1HttpBasic() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("registry_v1_http_basic.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastCcompatV6HttpBasic() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v6_http_basic.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastCcompatV7HttpBasic() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("ccompat_v7_http_basic.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
    public void testRapidastCncfV0HttpBasic() {
        // Get path to config file
        String configFilePath = RapidastUtils.getRapidastFilePath("cncf_v0_http_basic.yaml");

        // Get registry hostname
        String hostname = Kubernetes.getRouteHost(
                // Use registry namespace
                Environment.NAMESPACE,
                // Use registry route name
                Environment.REGISTRY_ROUTE
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
