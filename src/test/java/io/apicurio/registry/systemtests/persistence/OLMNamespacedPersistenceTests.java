package io.apicurio.registry.systemtests.persistence;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-namespace")
@Tag("olm-namespace-persistence")
public class OLMNamespacedPersistenceTests extends OLMPersistenceTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(false);
    }
}
