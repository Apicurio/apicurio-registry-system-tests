package io.apicurio.registry.systemtests.config;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.resources.ApicurioRegistryResourceType;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ConfigTests extends TestBase {
    @Test
    @Tag("https")
    public void testConfigHttps() throws InterruptedException {
        ApicurioRegistry apicurioRegistry = deployTestRegistry(PersistenceKind.SQL, null, false);

        ApicurioRegistryResourceType.updateWithDefaultHttpsSecret(apicurioRegistry);

        Kubernetes.createOrReplaceResources(Environment.NAMESPACE, Collections.singletonList(apicurioRegistry));

        // TODO: Test HTTPS connection inside registry pod
    }
}
