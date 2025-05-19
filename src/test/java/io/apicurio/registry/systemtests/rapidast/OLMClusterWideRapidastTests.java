package io.apicurio.registry.systemtests.rapidast;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-clusterwide")
@Tag("olm-clusterwide-rapidast")
public class OLMClusterWideRapidastTests extends OLMRapidastTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(true);
    }
}
