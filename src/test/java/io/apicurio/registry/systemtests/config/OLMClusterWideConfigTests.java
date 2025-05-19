package io.apicurio.registry.systemtests.config;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-clusterwide")
@Tag("olm-clusterwide-config")
public class OLMClusterWideConfigTests extends OLMConfigTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(true);
    }
}
