package io.apicurio.registry.systemtests.deploy;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.DatabaseUtils;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryOLMOperatorType;
import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.apicurio.registry.systemtests.registryinfra.resources.ApicurioRegistryResourceType;
import io.apicurio.registry.systemtests.time.TimeoutBudget;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;

public abstract class OLMDeployTests extends DeployTests {
    private boolean clusterWide;

    public boolean getClusterWide() {
        return clusterWide;
    }

    public void setClusterWide(boolean clusterWide) {
        this.clusterWide = clusterWide;
    }

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

    @Test
    public void testMultipleNamespaces() throws InterruptedException {
        // Deploy default PostgreSQL database
        DatabaseUtils.deployDefaultPostgresqlDatabase();
        // Deploy default Apicurio Registry with default PostgreSQL database
        ApicurioRegistryUtils.deployDefaultApicurioRegistrySql(false);

        // Set suffix of second resources
        String suffix = "-multi";
        // Get second PostgreSQL database name
        String secondSqlName = Constants.DB_NAME + suffix;
        // Get second PostgreSQL database namespace
        String secondSqlNamespace = Constants.DB_NAMESPACE + suffix;

        // Deploy second PostgreSQL database
        DatabaseUtils.deployPostgresqlDatabase(secondSqlName, secondSqlNamespace);
        // Get second Apicurio Registry with second PostgreSQL database
        ApicurioRegistry secondSqlRegistry = ApicurioRegistryResourceType.getDefaultSql(
                Constants.REGISTRY + suffix,
                Environment.NAMESPACE + suffix,
                secondSqlName,
                secondSqlNamespace
        );

        // Deploy second Apicurio Registry with second PostgreSQL database
        if (clusterWide) {
            // If OLM operator is installed as cluster wide,
            // second Apicurio Registry should be deployed successfully
            ResourceManager.getInstance().createResource(true, secondSqlRegistry);
        } else {
            // If OLM operator is installed as namespaced,
            // second Apicurio Registry deployment should fail
            ResourceManager.getInstance().createResource(false, secondSqlRegistry);
            assertFalse(ResourceManager.getInstance().waitResourceCondition(secondSqlRegistry,
                    ResourceManager.getInstance().findResourceType(secondSqlRegistry)::isReady,
                    TimeoutBudget.ofDuration(Duration.ofMinutes(2))));
        }
    }
}
