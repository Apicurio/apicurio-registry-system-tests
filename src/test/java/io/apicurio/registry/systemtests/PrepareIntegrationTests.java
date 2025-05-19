package io.apicurio.registry.systemtests;

import io.apicur.registry.v1.ApicurioRegistry3;
import io.apicur.registry.v1.ApicurioRegistry3Builder;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.Base64Utils;
import io.apicurio.registry.systemtests.framework.Certificate;
import io.apicurio.registry.systemtests.framework.CertificateUtils;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.DatabaseUtils;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.operator.OperatorManager;
import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryOLMOperatorType;
import io.apicurio.registry.systemtests.operator.types.KeycloakOLMOperatorType;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @Test
    public void tryRegistry3() {
        ApicurioRegistry3 apicurioRegistry3 = new ApicurioRegistry3Builder()
                .withNewMetadata()
                    .withName(Constants.REGISTRY)
                    .withNamespace(Environment.NAMESPACE)
                .endMetadata()
                .withNewSpec()
                    .withNewApp()
                        .withNewSql()
                            .withNewDataSource()
                                .withPassword(Constants.DB_PASSWORD)
                                .withUsername(Constants.DB_USERNAME)
                            .endDataSource()
                        .endSql()
                    .endApp()
                    .withNewUi()
                    .endUi()
                .endSpec()
                .build();
    }

    @Test
    public void installOLMClusterWideRegistryOperator() throws InterruptedException {
        ApicurioRegistryOLMOperatorType operator = new ApicurioRegistryOLMOperatorType(Environment.CATALOG_IMAGE, true);

        operator.install();
    }

    @Test
    public void prepareTruststore() throws InterruptedException {
        Secret routerCertsDefaultSecret = Kubernetes.getSecret("openshift-config-managed", Constants.ROUTER_CERTS);
        String clusterBaseUrl = Objects.requireNonNull(
                Kubernetes.getRouteHost("openshift-console", "console")
        ).replace("console-openshift-console.", "");
        ArrayList<Certificate> certificates = CertificateUtils.readCertificates(
                Base64Utils.decode(routerCertsDefaultSecret.getData().get(clusterBaseUrl))
        );
        Secret newSecret = new SecretBuilder()
                .withNewMetadata()
                    .withName(routerCertsDefaultSecret.getMetadata().getName())
                    .withNamespace(Environment.NAMESPACE)
                .endMetadata()
                .withType("Opaque")
                .withData(new HashMap<>() {{
                    put(Constants.TRUSTSTORE_DATA_NAME, Base64Utils.encode(CertificateUtils.getCertificates(certificates)));
                }})
                .build();

        Kubernetes.createSecret(Environment.NAMESPACE, newSecret);

        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String caCertSecretValue = CertificateUtils.decodeBase64Secret(Environment.NAMESPACE, Constants.ROUTER_CERTS, Constants.TRUSTSTORE_DATA_NAME);
        Path caPath = Environment.getTmpPath("tls-" + timestamp + ".crt");

        CertificateUtils.writeToFile(caCertSecretValue, caPath);

        Path truststorePath = Environment.getTmpPath("truststore-" + timestamp + ".p12");

        CertificateUtils.runTruststoreCmd(truststorePath, "password", caPath);

        Secret secret = new SecretBuilder()
                .withNewMetadata()
                    .withName(Constants.TRUSTSTORE_SECRET_NAME)
                    .withNamespace(Environment.NAMESPACE)
                .endMetadata()
                .addToData(new HashMap<>() {{
                    put(Constants.TRUSTSTORE_DATA_NAME, Base64Utils.encode(truststorePath));
                }})
                .build();

        Kubernetes.createSecret(Environment.NAMESPACE, secret);
    }

    @Test
    public void prepareRegistry3SqlNoIAM() throws InterruptedException {
        // Deploy and get registry
        ApicurioRegistry3 apicurioRegistry3 = ApicurioRegistryUtils.deployDefaultApicurioRegistrySql(false);
    }
}
