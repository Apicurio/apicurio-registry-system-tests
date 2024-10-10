package io.apicurio.registry.systemtests.registryinfra.resources;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicur.registry.v1.ApicurioRegistryBuilder;
import io.apicur.registry.v1.apicurioregistryspec.configuration.Env;
import io.apicur.registry.v1.apicurioregistryspec.configuration.Security;
import io.apicur.registry.v1.apicurioregistryspec.configuration.env.ValueFrom;
import io.apicur.registry.v1.apicurioregistryspec.configuration.env.valuefrom.SecretKeyRef;
import io.apicur.registry.v1.apicurioregistryspec.configuration.kafkasql.SecurityBuilder;
import io.apicur.registry.v1.apicurioregistryspec.configuration.security.Https;
import io.apicur.registry.v1.apicurioregistryspec.deployment.podtemplatespecpreview.spec.Containers;
import io.apicur.registry.v1.apicurioregistryspec.deployment.podtemplatespecpreview.spec.Volumes;
import io.apicur.registry.v1.apicurioregistryspec.deployment.podtemplatespecpreview.spec.containers.VolumeMounts;
import io.apicur.registry.v1.apicurioregistryspec.deployment.podtemplatespecpreview.spec.volumes.Secret;
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

public class ApicurioRegistryResourceType implements ResourceType<ApicurioRegistry> {
    private static String getApicurioRegistryFilePath(String filename) {
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
    public ApicurioRegistry get(String namespace, String name) {
        return getOperation()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }

    public static MixedOperation<ApicurioRegistry, KubernetesResourceList<ApicurioRegistry>, Resource<ApicurioRegistry>>
    getOperation() {
        return Kubernetes.getResources(ApicurioRegistry.class);
    }

    @Override
    public void create(ApicurioRegistry resource) {
        getOperation()
                .inNamespace(resource.getMetadata().getNamespace())
                .create(resource);
    }

    @Override
    public void createOrReplace(ApicurioRegistry resource) {
        getOperation()
                .inNamespace(resource.getMetadata().getNamespace())
                .createOrReplace(resource);
    }

    @Override
    public void delete(ApicurioRegistry resource) {
        getOperation()
                .inNamespace(resource.getMetadata().getNamespace())
                .withName(resource.getMetadata().getName())
                .delete();
    }

    @Override
    public boolean isReady(ApicurioRegistry resource) {
        ApicurioRegistry apicurioRegistry = get(
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
                .orElse(false);
    }

    @Override
    public boolean doesNotExist(ApicurioRegistry resource) {
        if (resource == null) {
            return true;
        }

        return get(resource.getMetadata().getNamespace(), resource.getMetadata().getName()) == null;
    }

    @Override
    public void refreshResource(ApicurioRegistry existing, ApicurioRegistry newResource) {
        existing.setMetadata(newResource.getMetadata());
        existing.setSpec(newResource.getSpec());
        existing.setStatus(newResource.getStatus());
    }

    /** Get default instances **/



    public static ApicurioRegistry getDefaultMem(String name, String namespace) {
        return new ApicurioRegistryBuilder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withNewConfiguration()
                        .withPersistence("mem")
                    .endConfiguration()
                    .withNewDeployment()
                        .withNewPodTemplateSpecPreview()
                            .withNewSpec()
                                .withContainers(getDefaultContainers())
                                .withVolumes(getDefaultVolumes())
                            .endSpec()
                        .endPodTemplateSpecPreview()
                    .endDeployment()
                .endSpec()
                .build();
    }

    public static ApicurioRegistry getDefaultSql(String name, String namespace) {
        return getDefaultSql(name, namespace, "postgresql", "postgresql");
    }

    public static ApicurioRegistry getDefaultSql(String name, String namespace, String sqlName, String sqlNamespace) {
        String sqlUrl = "jdbc:postgresql://" + sqlName + "." + sqlNamespace + ".svc.cluster.local:5432/postgresdb";

        return new ApicurioRegistryBuilder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withNewConfiguration()
                        .withPersistence("sql")
                        .withNewSql()
                            .withNewDataSource()
                                .withUrl(sqlUrl)
                                .withUserName(Constants.DB_USERNAME)
                                .withPassword(Constants.DB_PASSWORD)
                            .endDataSource()
                        .endSql()
                    .endConfiguration()
                    .withNewDeployment()
                        .withNewPodTemplateSpecPreview()
                            .withNewSpec()
                                .withContainers(getDefaultContainers())
                                .withVolumes(getDefaultVolumes())
                            .endSpec()
                        .endPodTemplateSpecPreview()
                    .endDeployment()
                .endSpec()
                .build();

    }
    public static ApicurioRegistry getDefaultKafkasql(String name, String namespace) {
       return new ApicurioRegistryBuilder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withNewConfiguration()
                        .withPersistence("kafkasql")
                        .withNewKafkasql()
                            .withBootstrapServers(
                                    Constants.KAFKA + "-kafka-bootstrap." + Environment.NAMESPACE +
                                            ".svc.cluster.local:9092"
                            )
                        .endKafkasql()
                    .endConfiguration()
                    .withNewDeployment()
                        .withNewPodTemplateSpecPreview()
                            .withNewSpec()
                                .withContainers(getDefaultContainers())
                                .withVolumes(getDefaultVolumes())
                            .endSpec()
                        .endPodTemplateSpecPreview()
                    .endDeployment()
                .endSpec()
               .build();
    }

    public static ApicurioRegistry getDefaultMem(String name) {
        return getDefaultMem(name, Environment.NAMESPACE);
    }

    public static ApicurioRegistry getDefaultSql(String name) {
        return getDefaultSql(name, Environment.NAMESPACE);
    }

    public static ApicurioRegistry getDefaultKafkasql(String name) {
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
            setName("registry");
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
                    setDefaultMode(420);
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

    public static ApicurioRegistry getDefaultOAuthKafka(String name, String namespace) {
        return new ApicurioRegistryBuilder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withNewConfiguration()
                        .withEnv(getDefaultOAuthKafkaEnv())
                        .withPersistence("kafkasql")
                        .withNewKafkasql()
                            .withBootstrapServers(
                                    // TODO: Use "public" URL with 443 port
                                    Kubernetes.getRouteHost(
                                            Environment.NAMESPACE,
                                            Constants.OAUTH_KAFKA_NAME + "-kafka-oauth-bootstrap"
                                    ) + ":443"
                            )
                        .endKafkasql()
                    .endConfiguration()
                    .withNewDeployment()
                        .withNewPodTemplateSpecPreview()
                            .withNewSpec()
                                .withContainers(getDefaultOAuthKafkaContainers())
                                .withVolumes(getDefaultOAuthKafkaVolumes())
                            .endSpec()
                        .endPodTemplateSpecPreview()
                    .endDeployment()
                .endSpec()
                .build();
    }

    public static ApicurioRegistry getDefaultMem() {
        return getDefaultMem(Constants.REGISTRY);
    }

    public static ApicurioRegistry getDefaultSql() {
        return getDefaultSql(Constants.REGISTRY);
    }

    public static ApicurioRegistry getDefaultKafkasql() {
        return getDefaultKafkasql(Constants.REGISTRY);
    }

    public static void updateWithDefaultTLS(ApicurioRegistry apicurioRegistry) {
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
    }

    public static void updateWithDefaultSCRAM(ApicurioRegistry apicurioRegistry) {
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
    }

    public static void updateWithDefaultKeycloak(ApicurioRegistry apicurioRegistry) {
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
    }

    public static void updateWithDefaultHttpsSecret(ApicurioRegistry apicurioRegistry) {
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
    }
}