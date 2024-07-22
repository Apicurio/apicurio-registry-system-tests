package io.apicurio.registry.systemtests.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.client.KeycloakAdminApiClient;
import io.apicurio.registry.systemtests.executor.Exec;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.openshift.api.model.Route;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KeycloakUtils {
    private static final Logger LOGGER = LoggerUtils.getLogger();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static String getKeycloakFilePath(String filename) {
        return Paths.get(Environment.TESTSUITE_PATH, "kubefiles", "keycloak", filename).toString();
    }

    public static void deployKeycloak() throws InterruptedException, IOException {
        deployKeycloak(Environment.NAMESPACE);
    }

    public static void deployOAuthKafkaKeycloak() throws IOException, InterruptedException {
        deployOAuthKafkaKeycloak(Environment.NAMESPACE);
    }

    private static void deployKeycloakPostgres(String namespace) throws URISyntaxException {
        URL dtb = KeycloakUtils.class.getClassLoader().getResource("postgres.yaml");

        Exec.executeAndCheck(
                "oc",
                "apply",
                "-n", namespace,
                "-f", Paths.get(dtb.toURI()).toFile().toString()
        );
        ResourceUtils.waitStatefulSetReady(namespace, "postgresql-db");

    }

    public static void deployKeycloak(String namespace) throws InterruptedException, IOException {
        LOGGER.info("Deploying Keycloak...");

        String keycloakFilePath = getKeycloakFilePath("keycloak.yaml");
        String keycloakHostname = Objects.requireNonNull(Kubernetes
                        .getRouteHost("openshift-console", "console"))
                .replace("console-openshift-console", Constants.SSO_NAME);

        TextFileUtils.replaceInFile(keycloakFilePath, "<hostname>", keycloakHostname);

        // Deploy Keycloak server
        Exec.executeAndCheck("oc", "apply", "-n", namespace, "-f", keycloakFilePath);

        // Wait for Keycloak server to be ready
        Assertions.assertTrue(ResourceUtils.waitStatefulSetReady(namespace, Constants.SSO_NAME));

        // TODO: Wait for Keycloak Realm readiness, but API model not available
        // Create Keycloak Realm
        Exec.executeAndCheck("oc", "apply", "-n", namespace, "-f", getKeycloakFilePath("keycloak-realm.yaml"));

        Thread.sleep(Duration.ofMinutes(1).toMillis());

        // CREATE AND MAP KEYCLOAK CLIENT SCOPE FOR MAPPING USER ATTRIBUTES INTO TOKEN
        // Get Keycloak API admin client
        KeycloakAdminApiClient keycloakAdminApiClient = new KeycloakAdminApiClient(
                // Set Keycloak admin URL to default one
                KeycloakUtils.getDefaultKeycloakAdminURL(namespace),
                // Get access token for Keycloak admin
                KeycloakUtils.getAdminAccessToken(namespace)
        );
        // Create user attributes client scope for mapping user attributes into token
        Assertions.assertTrue(keycloakAdminApiClient.createUserAttributesClientScope());
        // Add user attributes client scope to default client scopes of Keycloak API client
        Assertions.assertTrue(
                keycloakAdminApiClient.addDefaultClientScopeToClient(
                        // Get ID of API client (it is NOT clientId)
                        keycloakAdminApiClient.getClientId(Constants.SSO_CLIENT_API),
                        // Get ID of client scope (it is NOT client scope name)
                        keycloakAdminApiClient.getClientScopeId(Constants.SSO_SCOPE)
                )
        );

        LOGGER.info("Keycloak should be deployed.");
    }

    public static void deployOAuthKafkaKeycloak(String namespace) throws InterruptedException, IOException {
        LOGGER.info("Deploying OAuth Kafka Keycloak...");
        // ResourceManager manager = ResourceManager.getInstance();

        String keycloakFilePath = getKeycloakFilePath("keycloak_oauth_kafka.yaml");
        String keycloakHostname = Objects.requireNonNull(Kubernetes
                        .getRouteHost("openshift-console", "console"))
                .replace("console-openshift-console", Constants.SSO_NAME);

        TextFileUtils.replaceInFile(keycloakFilePath, "<hostname>", keycloakHostname);

        // Deploy Keycloak server
        Exec.executeAndCheck("oc", "apply", "-n", namespace, "-f", keycloakFilePath);

        // Wait for Keycloak server to be ready
        Assertions.assertTrue(ResourceUtils.waitStatefulSetReady(namespace, Constants.SSO_NAME));

        // TODO: Wait for Keycloak Realm readiness, but API model not available
        // Create Keycloak Realm
        Exec.executeAndCheck(
                "oc",
                "apply",
                "-n", namespace,
                "-f", getKeycloakFilePath("keycloak_oauth_kafka-realm.yaml")
        );

        Thread.sleep(Duration.ofMinutes(1).toMillis());

        LOGGER.info("Keycloak should be deployed.");
    }

    public static void removeKeycloakRealm(String namespace) {
        LOGGER.info("Removing keycloak realm");

        Exec.executeAndCheck(
                "oc",
                "delete",
                "-n", namespace,
                "-f", getKeycloakFilePath("keycloak-realm.yaml")
        );
    }

    public static void removeOAuthKafkaKeycloakRealm(String namespace) {
        LOGGER.info("Removing OAuth Kafka keycloak realm");

        Exec.executeAndCheck(
                "oc",
                "delete",
                "-n", namespace,
                "-f", getKeycloakFilePath("keycloak_oauth_kafka-realm.yaml")
        );
    }

    public static void removeKeycloak(String namespace) throws InterruptedException {
        removeKeycloakRealm(namespace);
        Thread.sleep(Duration.ofMinutes(2).toMillis());
        LOGGER.info("Removing Keycloak...");
        Exec.executeAndCheck(
                "oc",
                "delete",
                "-n", namespace,
                "-f", getKeycloakFilePath("keycloak.yaml")
        );

        LOGGER.info("Keycloak should be removed.");
    }

    public static void removeOAuthKafkaKeycloak(String namespace) throws InterruptedException {
        removeOAuthKafkaKeycloakRealm(namespace);
        Thread.sleep(Duration.ofMinutes(2).toMillis());
        LOGGER.info("Removing OAuth Kafka Keycloak...");
        Exec.executeAndCheck(
                "oc",
                "delete",
                "-n", namespace,
                "-f", getKeycloakFilePath("keycloak_oauth_kafka.yaml")
        );

        LOGGER.info("OAuth Kafka Keycloak should be removed.");
    }

    public static String getKeycloakURL(String namespace, String name, boolean secured) {
        String scheme = secured ? "https://" : "http://";

        return scheme + Kubernetes.getRouteByPrefixHost(namespace, name);
    }

    public static String getDefaultKeycloakAdminURL() {
        return getDefaultKeycloakAdminURL(Environment.NAMESPACE);
    }

    public static String getDefaultKeycloakAdminURL(String namespace) {
        return getKeycloakURL(namespace, Constants.SSO_NAME + "-ingress", true);
    }

    public static String getDefaultOAuthKafkaTokenEndpointUri() {
        Route route = Kubernetes.getRouteByPrefix(Environment.NAMESPACE, Constants.SSO_NAME + "-ingress");

        return "https://" + Kubernetes.getRouteHost(Environment.NAMESPACE, route.getMetadata().getName())
                + "/realms/demo/protocol/openid-connect/token";
    }

    public static String getDefaultOAuthKafkaJwksEndpointUri() {
        Route route = Kubernetes.getRouteByPrefix(Environment.NAMESPACE, Constants.SSO_NAME + "-ingress");

        return "https://" + Kubernetes.getRouteHost(Environment.NAMESPACE, route.getMetadata().getName())
                + "/realms/demo/protocol/openid-connect/certs";
    }

    public static String getDefaultOAuthKafkaValidIssuerUri() {
        Route route = Kubernetes.getRouteByPrefix(Environment.NAMESPACE, Constants.SSO_NAME + "-ingress");

        return "https://" + Kubernetes.getRouteHost(Environment.NAMESPACE, route.getMetadata().getName())
                + "/realms/demo";
    }

    private static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }

            stringBuilder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }

        return HttpRequest.BodyPublishers.ofString(stringBuilder.toString());
    }

    public static String getAccessToken(
            String keycloakUrl,
            String keycloakRealm,
            String clientId,
            String username,
            String password
    ) {
        // Construct token API URI of Keycloak Realm
        URI keycloakRealmUrl = HttpClientUtils.buildURI(
                "%s/realms/%s/protocol/openid-connect/token", keycloakUrl, keycloakRealm
        );

        // Prepare request data
        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "password");
        data.put("client_id", clientId);
        data.put("username", username);
        data.put("password", password);

        LOGGER.info("Requesting access token from {}...", keycloakRealmUrl);

        // Create request
        HttpRequest request = HttpClientUtils.newBuilder()
                .uri(keycloakRealmUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(data))
                .build();

        // Process request
        HttpResponse<String> response = HttpClientUtils.processRequest(request);

        // Check response status code
        if (response.statusCode() != HttpStatus.SC_OK) {
            LOGGER.error("Response: code={}, body={}", response.statusCode(), response.body());

            return null;
        }

        // Return access token
        try {
            return MAPPER.readValue(response.body(), Map.class).get("access_token").toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAccessToken(ApicurioRegistry apicurioRegistry, String username, String password) {
        // Get Keycloak URL of Apicurio Registry
        String keycloakUrl = apicurioRegistry.getSpec().getConfiguration().getSecurity().getKeycloak().getUrl();
        // Get Keycloak Realm of Apicurio Registry
        String keycloakRealm = apicurioRegistry.getSpec().getConfiguration().getSecurity().getKeycloak().getRealm();
        // Get Keycloak API client ID of Apicurio Registry
        String clientId = apicurioRegistry.getSpec().getConfiguration().getSecurity().getKeycloak().getApiClientId();

        return getAccessToken(keycloakUrl, keycloakRealm, clientId, username, password);
    }

    public static String getAdminAccessToken() {
        return getAdminAccessToken(Environment.NAMESPACE);
    }

    public static String getAdminAccessToken(String namespace) {
        // Get secret with Keycloak admin credentials
        Secret secret = Kubernetes.getSecret(namespace, Constants.SSO_NAME + "-initial-admin");
        // Get Keycloak admin username
        String username = Base64Utils.decode(secret.getData().get("username"));
        // Get Keycloak admin password
        String password = Base64Utils.decode(secret.getData().get("password"));
        // Get Keycloak admin URL
        String url = getKeycloakURL(namespace, Constants.SSO_NAME + "-ingress", true);
        // Construct token API URI of admin Keycloak Realm
        URI tokenUrl = HttpClientUtils.buildURI(
                "%s/realms/%s/protocol/openid-connect/token", url, Constants.SSO_REALM_ADMIN
        );

        // Prepare request data
        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "password");
        data.put("client_id", Constants.SSO_ADMIN_CLIENT_ID);
        data.put("username", username);
        data.put("password", password);

        // Log info about current action
        LOGGER.info("Requesting admin access token from {}...", tokenUrl);

        // Create request
        HttpRequest request = HttpClientUtils.newBuilder()
                .uri(tokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(data))
                .build();

        // Process request
        HttpResponse<String> response = HttpClientUtils.processRequest(request);

        // Check response status code
        if (response.statusCode() != HttpStatus.SC_OK) {
            LOGGER.error("Response: code={}, body={}", response.statusCode(), response.body());

            return null;
        }

        // Return access token
        try {
            return MAPPER.readValue(response.body(), Map.class).get("access_token").toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
