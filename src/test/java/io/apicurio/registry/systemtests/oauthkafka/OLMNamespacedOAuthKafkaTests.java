package io.apicurio.registry.systemtests.oauthkafka;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-namespace")
@Tag("olm-namespace-oauthkafka")
public class OLMNamespacedOAuthKafkaTests extends OLMOAuthKafkaTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(false);
    }
}
