package io.apicurio.registry.systemtests.auth;

import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryOLMOperatorType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class OLMAuthTests extends AuthTests {
    private boolean clusterWide;

    public boolean getClusterWide() {
        return clusterWide;
    }

    public void setClusterWide(boolean clusterWide) {
        this.clusterWide = clusterWide;
    }

    @BeforeAll
    public void testBeforeAll(ExtensionContext testContext) throws InterruptedException {
        LOGGER.info("BeforeAll: " + testContext.getTestClass().get().getSimpleName());

        ApicurioRegistryOLMOperatorType registryOLMOperator = new ApicurioRegistryOLMOperatorType(clusterWide);

        operatorManager.installOperatorShared(registryOLMOperator);
    }

    @BeforeEach
    public void testBeforeEach(ExtensionContext testContext) {
        LOGGER.info("BeforeEach: " + testContext.getTestMethod().get().getName());
    }
}
