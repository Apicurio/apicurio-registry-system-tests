package io.apicurio.registry.systemtests.rapidast;

import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryOLMOperatorType;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;

@Getter
@Setter
public abstract class OLMRapidastTests extends RapidastTests {
    private boolean clusterWide;

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

