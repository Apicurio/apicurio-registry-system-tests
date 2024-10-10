package io.apicurio.registry.systemtests.rapidast;

import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryOLMOperatorType;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;

@Getter
@Setter
public abstract class OLMRapidastTests extends RapidastTests {
    private boolean clusterWide;

    @BeforeEach
    public void testBeforeEach(ExtensionContext testContext) throws InterruptedException {
        LOGGER.info("BeforeEach: " + testContext.getTestMethod().get().getName());

        ApicurioRegistryOLMOperatorType registryOLMOperator = new ApicurioRegistryOLMOperatorType(clusterWide);

        operatorManager.installOperator(registryOLMOperator);
    }
}

