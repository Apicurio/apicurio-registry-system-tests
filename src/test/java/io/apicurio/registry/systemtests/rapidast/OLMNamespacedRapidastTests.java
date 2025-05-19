package io.apicurio.registry.systemtests.rapidast;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-namespace")
@Tag("olm-namespace-rapidast")
public class OLMNamespacedRapidastTests extends OLMRapidastTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(false);
    }
}
