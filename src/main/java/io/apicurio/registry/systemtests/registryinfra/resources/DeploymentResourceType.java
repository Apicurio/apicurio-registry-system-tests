package io.apicurio.registry.systemtests.registryinfra.resources;

import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EmptyDirVolumeSource;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeploymentResourceType implements ResourceType<Deployment> {
    @Override
    public Duration getTimeout() {
        return Duration.ofMinutes(5);
    }

    @Override
    public String getKind() {
        return ResourceKind.DEPLOYMENT;
    }

    @Override
    public Deployment get(String namespace, String name) {
        return Kubernetes.getDeployment(namespace, name);
    }

    @Override
    public void create(Deployment resource) {
        Kubernetes.createDeployment(resource.getMetadata().getNamespace(), resource);
    }

    @Override
    public void createOrReplace(Deployment resource) {
        Kubernetes.createOrReplaceDeployment(resource.getMetadata().getNamespace(), resource);
    }

    @Override
    public void delete(Deployment resource) {
        Kubernetes.deleteDeployment(resource.getMetadata().getNamespace(), resource.getMetadata().getName());
    }

    @Override
    public boolean isReady(Deployment resource) {
        return Kubernetes.isDeploymentReady(resource.getMetadata().getNamespace(), resource.getMetadata().getName());
    }

    @Override
    public boolean doesNotExist(Deployment resource) {
        if (resource == null) {
            return true;
        }

        return get(resource.getMetadata().getNamespace(), resource.getMetadata().getName()) == null;
    }

    @Override
    public void refreshResource(Deployment existing, Deployment newResource) {
        existing.setMetadata(newResource.getMetadata());
        existing.setSpec(newResource.getSpec());
        existing.setStatus(newResource.getStatus());
    }

    /** Get default instances **/

    private static List<EnvVar> getDefaultPostgresqlEnvVars(String databaseName) {
        List<EnvVar> envVars = new ArrayList<>();

        envVars.add(new EnvVar("POSTGRES_ADMIN_PASSWORD", "adminpassword", null));
        envVars.add(new EnvVar("POSTGRES_DB", databaseName, null));
        envVars.add(new EnvVar("POSTGRES_USER", Constants.DB_USERNAME, null));
        envVars.add(new EnvVar("POSTGRES_PASSWORD", Constants.DB_PASSWORD, null));
        envVars.add(new EnvVar("PGDATA", "/" + Constants.DB_MOUNT_PATH + "/data", null));

        return envVars;
    }

    private static List<EnvVar> getDefaultPostgresqlEnvVars() {
        return getDefaultPostgresqlEnvVars("postgresdb");
    }

    private static Container getDefaultPostgresqlContainer(String name) {
        return new ContainerBuilder()
                .withEnv(getDefaultPostgresqlEnvVars())
                .withImage("mirror.gcr.io/library/postgres:" + Environment.POSTGRESQL_VERSION)
                .withImagePullPolicy("IfNotPresent")
                .withName(name)
                .addNewPort()
                    .withContainerPort(5432)
                    .withName(Constants.DB_PORT_NAME)
                    .withProtocol("TCP")
                .endPort()
                .withNewReadinessProbe()
                    .withNewTcpSocket()
                        .withNewPort(5432)
                    .endTcpSocket()
                .endReadinessProbe()
                .withNewLivenessProbe()
                    .withNewTcpSocket()
                        .withNewPort(5432)
                    .endTcpSocket()
                .endLivenessProbe()
                .withVolumeMounts(new VolumeMount() {{
                    setMountPath("/" + Constants.DB_MOUNT_PATH);
                    setName(name);
                }})
                .build();
    }

    private static Container getDefaultPostgresqlContainer(String name, String databaseName) {
        return new ContainerBuilder()
                .withEnv(getDefaultPostgresqlEnvVars(databaseName))
                .withImage("mirror.gcr.io/library/postgres:" + Environment.POSTGRESQL_VERSION)
                .withImagePullPolicy("IfNotPresent")
                .withName(name)
                .addNewPort()
                    .withContainerPort(5432)
                    .withName(Constants.DB_PORT_NAME)
                    .withProtocol("TCP")
                .endPort()
                .withNewReadinessProbe()
                    .withNewTcpSocket()
                        .withNewPort(5432)
                    .endTcpSocket()
                .endReadinessProbe()
                .withNewLivenessProbe()
                    .withNewTcpSocket()
                        .withNewPort(5432)
                    .endTcpSocket()
                .endLivenessProbe()
                .withVolumeMounts(new VolumeMount() {{
                    setMountPath("/" + Constants.DB_MOUNT_PATH);
                    setName(name);
                }})
                .build();
    }

    public static Deployment getDefaultPostgresql(String name, String namespace) {
        return new DeploymentBuilder()
                .withNewMetadata()
                    .addToLabels("app", name)
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withReplicas(1)
                    .withNewSelector()
                        .addToMatchLabels("app", name)
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToLabels("app", name)
                        .endMetadata()
                        .withNewSpec()
                            .withContainers(getDefaultPostgresqlContainer(name))
                            .withVolumes(new Volume() {{
                                setName(name);
                                setEmptyDir(new EmptyDirVolumeSource());
                            }})
                            .withRestartPolicy("Always")
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }

    public static Deployment getDefaultPostgresql(String name, String namespace, String databaseName) {
        return new DeploymentBuilder()
                .withNewMetadata()
                    .addToLabels("app", name)
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .withReplicas(1)
                    .withNewSelector()
                        .addToMatchLabels("app", name)
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToLabels("app", name)
                        .endMetadata()
                        .withNewSpec()
                            .withContainers(getDefaultPostgresqlContainer(name, databaseName))
                            .withVolumes(new Volume() {{
                                setName(name);
                                setEmptyDir(new EmptyDirVolumeSource());
                            }})
                            .withRestartPolicy("Always")
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }

    public static Deployment getDefaultPostgresql() {
        return getDefaultPostgresql(Constants.DB_NAME, Constants.DB_NAMESPACE);
    }

    private static Container getDefaultSeleniumContainer(String name) {
        return new ContainerBuilder()
                .withName(name)
                .withImage("quay.io/redhatqe/selenium-standalone")
                .addNewPort()
                    .withContainerPort(4444)
                    .withName("http")
                    .withProtocol("TCP")
                .endPort()
                .withNewReadinessProbe()
                    .withNewHttpGet()
                        .withPath("/wd/hub/status")
                        .withNewPort(4444)
                    .endHttpGet()
                    .withInitialDelaySeconds(10)
                    .withPeriodSeconds(2)
                .endReadinessProbe()
                .build();
    }

    public static Deployment getDefaultSelenium(String name, String namespace) {
        return new DeploymentBuilder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                    .withLabels(Collections.singletonMap("app", name))
                .endMetadata()
                .withNewSpec()
                    .withReplicas(1)
                    .withNewSelector()
                        .addToMatchLabels("app", name)
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToLabels("app", name)
                        .endMetadata()
                        .withNewSpec()
                            .withContainers(getDefaultSeleniumContainer(name))
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }

    public static Deployment getDefaultSelenium() {
        return getDefaultSelenium("selenium-chrome", "selenium");
    }
}
