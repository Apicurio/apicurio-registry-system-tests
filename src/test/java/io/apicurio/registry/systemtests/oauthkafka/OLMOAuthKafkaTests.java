package io.apicurio.registry.systemtests.oauthkafka;

import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryOLMOperatorType;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;

@Setter
public abstract class OLMOAuthKafkaTests extends OAuthKafkaTests {
    private boolean clusterWide;

    public boolean getClusterWide() {
        return clusterWide;
    }

    @BeforeEach
    public void testBeforeEach(ExtensionContext testContext) throws InterruptedException {
        LOGGER.info("BeforeEach: " + testContext.getTestMethod().get().getName());

        ApicurioRegistryOLMOperatorType registryOLMOperator = new ApicurioRegistryOLMOperatorType(clusterWide);

        operatorManager.installOperator(registryOLMOperator);
    }
}
