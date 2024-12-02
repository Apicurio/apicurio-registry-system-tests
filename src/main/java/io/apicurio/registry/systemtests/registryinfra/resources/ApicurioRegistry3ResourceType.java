package io.apicurio.registry.systemtests.registryinfra.resources;

import io.apicur.registry.v1.ApicurioRegistry3;
import io.apicur.registry.v1.ApicurioRegistry3Builder;
import io.apicur.registry.v1.apicurioregistry3spec.app.Env;
import io.apicur.registry.v1.apicurioregistry3spec.app.env.ValueFrom;
import io.apicur.registry.v1.apicurioregistry3spec.app.env.valuefrom.SecretKeyRef;
import io.apicur.registry.v1.apicurioregistry3spec.app.podtemplatespec.spec.Containers;
import io.apicur.registry.v1.apicurioregistry3spec.app.podtemplatespec.spec.Volumes;
import io.apicur.registry.v1.apicurioregistry3spec.app.podtemplatespec.spec.containers.VolumeMounts;
import io.apicur.registry.v1.apicurioregistry3spec.app.podtemplatespec.spec.volumes.Secret;
import io.apicur.registry.v1.apicurioregistry3spec.app.sql.Datasource;
import io.apicur.registry.v1.apicurioregistry3spec.app.sql.DatasourceBuilder;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ApicurioRegistry3ResourceType implements ResourceType<ApicurioRegistry3> {
    private static String getApicurioRegistry3FilePath(String filename) {
        return Paths.get(Environment.TESTSUITE_PATH, "kubefiles", "apicurio", filename).toString();
    }

    @Override
    public Duration getTimeout() {
        return Duration.ofMinutes(10);
    }

    @Override
    public String getKind() {
        return ResourceKind.APICURIO_REGISTRY;
    }

    @Override
    public ApicurioRegistry3 get(String namespace, String name) {
        return getOperation()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }

    public static MixedOperation<
            ApicurioRegistry3,
            KubernetesResourceList<ApicurioRegistry3>,
            Resource<ApicurioRegistry3>
            >
    getOperation() {
        return Kubernetes.getResources(ApicurioRegistry3.class);
    }

    @Override
    public void create(ApicurioRegistry3 resource) {
        getOperation()
                .inNamespace(resource.getMetadata().getNamespace())
                .create(resource);
    }

    @Override
    public void createOrReplace(ApicurioRegistry3 resource) {
        getOperation()
                .inNamespace(resource.getMetadata().getNamespace())
                .createOrReplace(resource);
    }

    @Override
    public void delete(ApicurioRegistry3 resource) {
        getOperation()
                .inNamespace(resource.getMetadata().getNamespace())
                .withName(resource.getMetadata().getName())
                .delete();
    }

    @Override
    public boolean isReady(ApicurioRegistry3 resource) {
        /*ApicurioRegistry3 apicurioRegistry = get(
                resource.getMetadata().getNamespace(),
                resource.getMetadata().getName()
        );

        if (apicurioRegistry == null || apicurioRegistry.getStatus() == null) {
            return false;
        }

        return apicurioRegistry
                .getStatus()
                .getConditions()
                .stream()
                .filter(condition -> condition.getType().equals("Ready"))
                .map(condition -> condition.getStatus().name().equals("TRUE"))
                .findFirst()
                .orElse(false);*/
        String namespace = resource.getMetadata().getNamespace();
        String name = resource.getMetadata().getName();

        boolean appReady = Kubernetes.isDeploymentReady(namespace, name + Constants.REGISTRY_API_DEPLOYMENT_SUFFIX);
        boolean uiReady = Kubernetes.isDeploymentReady(namespace, name + Constants.REGISTRY_UI_DEPLOYMENT_SUFFIX);

        return (appReady && uiReady);
    }

    @Override
    public boolean doesNotExist(ApicurioRegistry3 resource) {
        if (resource == null) {
            return true;
        }

        return get(resource.getMetadata().getNamespace(), resource.getMetadata().getName()) == null;
    }

    @Override
    public void refreshResource(ApicurioRegistry3 existing, ApicurioRegistry3 newResource) {
        existing.setMetadata(newResource.getMetadata());
        existing.setSpec(newResource.getSpec());
        existing.setStatus(newResource.getStatus());
    }

    public static String getHost(String prefix) {
        return Kubernetes
                .getRouteHost("openshift-console", "console")
                .replace("console-openshift-console", prefix);
    }

    public static String getApiUrl(String prefix) {
        return "http://" + getHost(prefix) + "/apis/registry/v3";
    }

    /** Get default instances **/



    public static ApicurioRegistry3 getDefaultMem(String name, String namespace) {
        return new ApicurioRegistry3Builder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withNewApp()
                        .withNewPodTemplateSpec()
                            .withNewSpec()
                                .withContainers(getDefaultContainers())
                                .withVolumes(getDefaultVolumes())
                            .endSpec()
                        .endPodTemplateSpec()
                    .endApp()
                .endSpec()
                .build();
    }

    public static ApicurioRegistry3 getDefaultSql(String name, String namespace) {
        return getDefaultSql(name, namespace, Constants.DB_NAME, Constants.DB_NAMESPACE);
    }

    public static ArrayList<io.apicur.registry.v1.apicurioregistry3spec.ui.Env> getDefaultUiEnv() {
        return new ArrayList<>() {{
            add(new io.apicur.registry.v1.apicurioregistry3spec.ui.Env() {{
                setName("REGISTRY_API_URL");
                setValue(getApiUrl("apicurio-registry-api"));
            }});
        }};
    }

    public static Datasource getDefaultSqlDataSource(String sqlUrl) {
        return new DatasourceBuilder()
                .withUrl(sqlUrl)
                .withUsername(Constants.DB_USERNAME)
                .withPassword(Constants.DB_PASSWORD)
                .build();
    }

    public static ApicurioRegistry3 getDefaultSql(String name, String namespace, String sqlName, String sqlNamespace) {
        String sqlUrl = "jdbc:postgresql://" + sqlName + "." + sqlNamespace + ".svc.cluster.local:5432/postgresdb";

        return new ApicurioRegistry3Builder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withNewApp()
                        .withHost(getHost("apicurio-registry-api"))
                        .withNewSql()
                            .withDatasource(getDefaultSqlDataSource(sqlUrl))
                        .endSql()
                        .withNewPodTemplateSpec()
                            .withNewSpec()
                                .withContainers(getDefaultContainers())
                                .withVolumes(getDefaultVolumes())
                            .endSpec()
                        .endPodTemplateSpec()
                    .endApp()
                    .withNewUi()
                        .withEnv(getDefaultUiEnv())
                        .withHost(getHost("apicurio-registry-ui"))
                    .endUi()
                .endSpec()
                .build();

    }
    public static ApicurioRegistry3 getDefaultKafkasql(String name, String namespace) {
       return new ApicurioRegistry3Builder()
               .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
               .endMetadata()
               .withNewSpec()
                    .withNewApp()
                        .withNewKafkasql()
                            .withBootstrapServers(
                                    Constants.KAFKA + "-kafka-bootstrap." + Environment.NAMESPACE +
                                            ".svc.cluster.local:9092"
                            )
                        .endKafkasql()
                        .withNewPodTemplateSpec()
                            .withNewSpec()
                                .withContainers(getDefaultContainers())
                                .withVolumes(getDefaultVolumes())
                            .endSpec()
                        .endPodTemplateSpec()
                    .endApp()
                .endSpec()
               .build();
    }

    public static ApicurioRegistry3 getDefaultMem(String name) {
        return getDefaultMem(name, Environment.NAMESPACE);
    }

    public static ApicurioRegistry3 getDefaultSql(String name) {
        return getDefaultSql(name, Environment.NAMESPACE);
    }

    public static ApicurioRegistry3 getDefaultKafkasql(String name) {
        return getDefaultKafkasql(name, Environment.NAMESPACE);
    }

    private static ArrayList<Env> getDefaultSslTruststoreEnv() {
        return new ArrayList<>() {{
            add(new Env() {{
                setName("JAVA_TOOL_OPTIONS");
                setValue("-Djavax.net.ssl.trustStore=/mytruststore/myTrustStore " +
                        "-Djavax.net.ssl.trustStorePassword=password " +
                        "-Djavax.net.ssl.trustStoreType=PKCS12");
            }});
        }};
    }

    private static ArrayList<Env> getDefaultOAuthKafkaEnv1() {
        return new ArrayList<>() {{
            addAll(getDefaultSslTruststoreEnv());
            add(new Env() {{
                setName("QUARKUS_LOG_LEVEL"); setValue("INFO"); }});
            add(new Env() {{
                setName("ENABLE_KAFKA_SASL"); setValue("true"); }});
            add(new Env() {{
                setName("CLIENT_ID");
                setValueFrom(new ValueFrom() {{
                    setSecretKeyRef(new SecretKeyRef() {{
                        setName("console-ui-secrets"); setKey("REGISTRY_CLIENT_ID");
                    }});
                }});
            }});
            add(new Env() {{
                setName("CLIENT_SECRET");
                setValueFrom(new ValueFrom() {{
                    setSecretKeyRef(new SecretKeyRef() {{
                        setName("console-ui-secrets"); setKey("REGISTRY_CLIENT_SECRET");
                    }});
                }});
            }});
        }};
    }

    private static ArrayList<Env> getDefaultOAuthKafkaEnv2() {
        return new ArrayList<>() {{
            add(new Env() {{
                setName("KAFKA_SECURITY_PROTOCOL");
                setValue("SASL_SSL");
            }});
            add(new Env() {{
                setName("KAFKA_SSL_TRUSTSTORE_TYPE");
                setValue("PKCS12");
            }});
            add(new Env() {{
                setName("KAFKA_SSL_TRUSTSTORE_LOCATION");
                setValue("/tmp/cluster-ca-cert/ca.p12");
            }});
            add(new Env() {{
                setName("KAFKA_SSL_TRUSTSTORE_PASSWORD");
                setValueFrom(new ValueFrom() {{
                    setSecretKeyRef(new SecretKeyRef() {{
                        setName("kafka1-cluster-ca-cert");
                        setKey("ca.password");
                    }});
                }});
            }});
            add(new Env() {{
                setName("OAUTH_TOKEN_ENDPOINT_URI");
                setValue(KeycloakUtils.getDefaultOAuthKafkaTokenEndpointUri());
            }});
        }};
    }

    private static ArrayList<Env> getDefaultOAuthKafkaEnv() {
        ArrayList<Env> fullList = getDefaultOAuthKafkaEnv1();

        fullList.addAll(getDefaultOAuthKafkaEnv2());

        return fullList;
    }

    private static Containers getDefaultContainers() {
        return new Containers() {{
            setName("apicurio-registry-app");
            setVolumeMounts(new ArrayList<>() {{
                add(new VolumeMounts() {{
                    setName("mytruststore");
                    setMountPath("/mytruststore");
                }});
            }});
        }};
    }

    private static Containers getDefaultOAuthKafkaContainers() {
        return new Containers() {{
            setName("registry");
            setVolumeMounts(new ArrayList<>() {{
                add(new VolumeMounts() {{
                    setName("cluster-ca-cert");
                    setMountPath("/tmp/cluster-ca-cert");
                }});
                add(new VolumeMounts() {{
                    setName("mytruststore");
                    setMountPath("/mytruststore");
                }});
            }});
        }};
    }

    private static ArrayList<Volumes> getDefaultVolumes() {
        return new ArrayList<>() {{
            add(new Volumes() {{
                setName("mytruststore");
                setSecret(new Secret() {{
                    setSecretName(Constants.TRUSTSTORE_SECRET_NAME);
                    setDefaultMode(420L);
                }});
            }});
        }};
    }

    private static ArrayList<Volumes> getDefaultOAuthKafkaVolumes() {
        return new ArrayList<>() {{
            add(new Volumes() {{
                setName("cluster-ca-cert");
                setSecret(new Secret() {{
                    setSecretName("kafka1-cluster-ca-cert");
                }});
            }});
            addAll(getDefaultVolumes());
        }};
    }

    public static ApicurioRegistry3 getDefaultOAuthKafka(String name, String namespace) {
        return new ApicurioRegistry3Builder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withNewApp()
                        .withEnv(getDefaultOAuthKafkaEnv())
                        .withNewKafkasql()
                            .withBootstrapServers(
                                    // TODO: Use "public" URL with 443 port
                                    Kubernetes.getRouteHost(
                                            Environment.NAMESPACE,
                                            Constants.OAUTH_KAFKA_NAME + "-kafka-oauth-bootstrap"
                                    ) + ":443"
                            )
                        .endKafkasql()
                        .withNewPodTemplateSpec()
                            .withNewSpec()
                                .withContainers(getDefaultOAuthKafkaContainers())
                                .withVolumes(getDefaultOAuthKafkaVolumes())
                            .endSpec()
                        .endPodTemplateSpec()
                    .endApp()
                .endSpec()
                .build();
    }

    public static ApicurioRegistry3 getDefaultMem() {
        return getDefaultMem(Constants.REGISTRY);
    }

    public static ApicurioRegistry3 getDefaultSql() {
        return getDefaultSql(Constants.REGISTRY);
    }

    public static ApicurioRegistry3 getDefaultKafkasql() {
        return getDefaultKafkasql(Constants.REGISTRY);
    }

    /*public static void updateWithDefaultTLS(ApicurioRegistry3 apicurioRegistry) {
        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getKafkasql()
                .setSecurity(
                        new SecurityBuilder()
                                .withNewTls()
                                    .withKeystoreSecretName(Constants.KAFKA_USER + "-keystore")
                                    .withTruststoreSecretName(Constants.KAFKA + "-cluster-ca-truststore")
                                .endTls()
                                .build()
                );

        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getKafkasql()
                .setBootstrapServers(
                        Constants.KAFKA + "-kafka-bootstrap." + Environment.NAMESPACE +
                                ".svc.cluster.local:9093"
                );
    }*/

    /*public static void updateWithDefaultSCRAM(ApicurioRegistry3 apicurioRegistry) {
        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getKafkasql()
                .setSecurity(
                        new SecurityBuilder()
                                .withNewScram()
                                    .withTruststoreSecretName(Constants.KAFKA + "-cluster-ca-truststore")
                                    .withPasswordSecretName(Constants.KAFKA_USER)
                                    .withUser(Constants.KAFKA_USER)
                                .endScram()
                                .build()
                );

        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getKafkasql()
                .setBootstrapServers(
                        Constants.KAFKA + "-kafka-bootstrap." + Environment.NAMESPACE +
                                ".svc.cluster.local:9093"
                );
    }*/

    /*public static void updateWithDefaultKeycloak(ApicurioRegistry3 apicurioRegistry) {
        // Get env
        List<Env> envList = apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getEnv();

        // Check if env is not null
        if (envList == null) {
            // Create env when null
            envList = new ArrayList<>();
        }

        // Add SSL truststore
        envList.addAll(getDefaultSslTruststoreEnv());

        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .setEnv(envList);

        // Add Keycloak section
        // TODO: Check if Security already exists and update it accordingly
        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .setSecurity(
                        new io.apicur.registry.v1.apicurioregistryspec.configuration.SecurityBuilder()
                                .withNewKeycloak()
                                .withApiClientId(Constants.SSO_CLIENT_API)
                                .withUiClientId(Constants.SSO_CLIENT_UI)
                                .withRealm(Constants.SSO_REALM)
                                .withUrl(KeycloakUtils.getDefaultKeycloakURL())
                                .endKeycloak()
                                .build()
                );
    }*/

    private static ArrayList<Env> getDefaultKeycloakEnv() {
        return new ArrayList<>() {{
            add(new Env() {{
                setName("QUARKUS_OIDC_TENANT_ENABLED");
                setValue("true");
            }});
            add(new Env() {{
                setName("QUARKUS_OIDC_AUTH_SERVER_URL");
                setValue(KeycloakUtils.getDefaultKeycloakURL() + "/realms/" + Constants.SSO_REALM);
            }});
            add(new Env() {{
                setName("QUARKUS_OIDC_TOKEN_PATH");
                setValue(KeycloakUtils.getDefaultKeycloakURL() + "/realms/" + Constants.SSO_REALM +
                        "/protocol/openid-connect/token"
                );
            }});
            add(new Env() {{
                setName("QUARKUS_OIDC_CLIENT_ID");
                setValue(Constants.SSO_CLIENT_API);
            }});
            add(new Env() {{
                setName("APICURIO_UI_AUTH_OIDC_CLIENT_ID");
                setValue(Constants.SSO_CLIENT_UI);
            }});
        }};
    }

    /*
            - name: APICURIO_UI_AUTH_OIDC_REDIRECT_URI
              value: 'https://apicurio-registry-ui-registry3-test.apps.registry3.apicurio.integration-qe.com/'
            - name: APICURIO_LOG_LEVEL
              value: DEBUG
            - name: LOG_LEVEL
              value: INFO
            - name: QUARKUS_PROFILE
              value: prod
            - name: APICURIO_CONFIG_CACHE_ENABLED
              value: 'true'
            - name: APICURIO_REST_DELETION_GROUP_ENABLED
              value: 'true'
            - name: APICURIO_REST_DELETION_ARTIFACT_ENABLED
              value: 'true'
            - name: APICURIO_REST_DELETION_ARTIFACT-VERSION_ENABLED
              value: 'true'
            - name: APICURIO_REST_MUTABILITY_ARTIFACT-VERSION-CONTENT_ENABLED
              value: 'true'
            - name: QUARKUS_HTTP_CORS_ORIGINS
              value: '*'
            - name: APICURIO_STORAGE_KIND
              value: sql
            - name: APICURIO_STORAGE_SQL_KIND
              value: postgresql
            - name: APICURIO_AUTHN_BASIC_CLIENT_CREDENTIALS_ENABLED
              value: 'true'
            - name: APICURIO_AUTHN_BASIC_CLIENT_CREDENTIALS_CACHE_EXPIRATION
              value: '4'
            - name: APICURIO_DATASOURCE_USERNAME
              value: username
            - name: APICURIO_DATASOURCE_PASSWORD
              value: password
            - name: APICURIO_DATASOURCE_URL
              value: >-
                jdbc:postgresql://postgresql.postgresql.svc.cluster.local:5432/postgresdb
            - name: APICURIO_APIS_DATE_FORMAT
              value: 'yyyy-MM-dd''T''HH:mm:ssZ'
            - name: QUARKUS_HTTP_CORS_HEADERS
              value: 'x-registry-name,x-registry-name-encoded,x-registry-description,x-registry-description-encoded,
              x-registry-version,x-registry-artifactid,x-registry-artifacttype,x-registry-hash-algorithm,
              x-registry-content-hash,access-control-request-method,access-control-allow-credentials,
              access-control-allow-origin,access-control-allow-headers,authorization,content-type,content-encoding,
              user-agent'
     */

    public static void updateWithDefaultKeycloak(ApicurioRegistry3 apicurioRegistry) {
        // Get env
        List<Env> envList = apicurioRegistry
                .getSpec()
                .getApp()
                .getEnv();

        // Check if env is not null
        if (envList == null) {
            // Create env when null
            envList = new ArrayList<>();
        }

        // Add SSL truststore
        envList.addAll(getDefaultSslTruststoreEnv());
        envList.addAll(getDefaultKeycloakEnv());

        apicurioRegistry
                .getSpec()
                .getApp()
                .setEnv(envList);
    }

    /*public static void updateWithDefaultHttpsSecret(ApicurioRegistry3 apicurioRegistry) {
        Https https = new io.apicur.registry.v1.apicurioregistryspec.configuration.security.HttpsBuilder()
                .withSecretName(Constants.HTTPS_SECRET_NAME)
                .build();
        Security security = apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getSecurity();

        if (security == null) {
            apicurioRegistry
                    .getSpec()
                    .getConfiguration()
                    .setSecurity(new io.apicur.registry.v1.apicurioregistryspec.configuration.SecurityBuilder()
                            .withHttps(https)
                            .build()
                    );
        } else {
            apicurioRegistry
                    .getSpec()
                    .getConfiguration()
                    .getSecurity()
                    .setHttps(https);
        }
    }*/
}