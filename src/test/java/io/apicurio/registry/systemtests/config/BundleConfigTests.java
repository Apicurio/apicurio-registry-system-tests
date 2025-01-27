package io.apicurio.registry.systemtests.config;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryBundleOperatorType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtensionContext;

@Tag("bundle")
@Tag("bundle-config")
public class BundleConfigTests extends ConfigTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();
    }

    @BeforeAll
    public void testBeforeAll(ExtensionContext testContext) throws InterruptedException {
        LOGGER.info("BeforeAll: " + testContext.getTestClass().get().getSimpleName());

        ApicurioRegistryBundleOperatorType registryBundleOperator = new ApicurioRegistryBundleOperatorType();

        operatorManager.installOperatorShared(registryBundleOperator);
    }

    @BeforeEach
    public void testBeforeEach(ExtensionContext testContext) {
        LOGGER.info("BeforeEach: " + testContext.getTestMethod().get().getName());
    }
}
