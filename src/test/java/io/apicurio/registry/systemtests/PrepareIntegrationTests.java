package io.apicurio.registry.systemtests;

import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.DatabaseUtils;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.operator.OperatorManager;
import io.apicurio.registry.systemtests.operator.types.KeycloakOLMOperatorType;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.Duration;

public class PrepareIntegrationTests {
    protected static Logger LOGGER = LoggerUtils.getLogger();
    protected final OperatorManager operatorManager = OperatorManager.getInstance();

    @Test
    public void prepareKeycloak() throws InterruptedException, IOException {
        LoggerUtils.logDelimiter("#");
        LOGGER.info("Deploying keycloak operator and instance...");
        LoggerUtils.logDelimiter("#");

        DatabaseUtils.createKeycloakPostgresqlDatabaseSecret();

        DatabaseUtils.deployKeycloakPostgresqlDatabase();

        KeycloakOLMOperatorType keycloakOLMOperator = new KeycloakOLMOperatorType(Environment.SSO_CHANNEL);
        operatorManager.installOperatorShared(keycloakOLMOperator);

        KeycloakUtils.deployKeycloak();

        Thread.sleep(Duration.ofMinutes(1).toMillis());

        LoggerUtils.logDelimiter("#");
        LOGGER.info("Deployment of keycloak operator and instance is done.");
        LoggerUtils.logDelimiter("#");
    }

    @Test
    public void prepareRegistryDatabase() {
        DatabaseUtils.deployDefaultPostgresqlDatabase();
    }

    @Test
    public void getRegistryAdminToken() {
        String keycloakUrl = Kubernetes
                .getRouteHost("openshift-console", "console")
                .replace("console-openshift-console", "https://" + Constants.SSO_NAME);

        LOGGER.info(
                "Token: \n\n{}\n",
                KeycloakUtils.getAccessToken(
                        keycloakUrl,
                        Constants.SSO_REALM,
                        Constants.SSO_CLIENT_API,
                        Constants.SSO_ADMIN_USER,
                        Constants.SSO_USER_PASSWORD
                )
        );
    }
}
