package io.apicurio.registry.systemtests.oauthkafka;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-clusterwide")
@Tag("olm-clusterwide-auth")
@Tag("olm-clusterwide-oauthkafka")
public class OLMClusterWideOAuthKafkaTests extends OLMOAuthKafkaTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(true);
    }
}
