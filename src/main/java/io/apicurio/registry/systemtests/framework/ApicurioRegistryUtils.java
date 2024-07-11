package io.apicurio.registry.systemtests.framework;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicur.registry.v1.apicurioregistryspec.configuration.Env;
import io.apicur.registry.v1.apicurioregistryspec.configuration.kafkasql.Security;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.apicurio.registry.systemtests.registryinfra.resources.ApicurioRegistryResourceType;
import io.apicurio.registry.systemtests.time.TimeoutBudget;
import io.fabric8.openshift.api.model.Route;
import io.strimzi.api.kafka.model.kafka.Kafka;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApicurioRegistryUtils {
    private static final Logger LOGGER = LoggerUtils.getLogger();

    private static String getTruststoreSecretName(ApicurioRegistry registry) {
        Security security = registry
                .getSpec()
                .getConfiguration()
                .getKafkasql()
                .getSecurity();

        if (security.getTls() != null) {
            return security.getTls().getTruststoreSecretName();
        } else if (security.getScram() != null) {
            return security.getScram().getTruststoreSecretName();
        }

        return null;
    }

    private static String getKeystoreSecretName(ApicurioRegistry registry) {
        Security security = registry
                .getSpec()
                .getConfiguration()
                .getKafkasql()
                .getSecurity();

        if (security.getTls() != null) {
            return security.getTls().getKeystoreSecretName();
        }

        return null;
    }

    public static ApicurioRegistry deployDefaultApicurioRegistrySql(boolean useKeycloak) throws InterruptedException {
        // Get Apicurio Registry
        ApicurioRegistry apicurioRegistrySql = ApicurioRegistryResourceType.getDefaultSql(
                Constants.REGISTRY,
                Environment.NAMESPACE
        );

        if (useKeycloak) {
            ApicurioRegistryResourceType.updateWithDefaultKeycloak(apicurioRegistrySql);
        }

        // Create Apicurio Registry
        ResourceManager.getInstance().createResource(true, apicurioRegistrySql);

        return apicurioRegistrySql;
    }

    public static ApicurioRegistry deployDefaultApicurioRegistryKafkasqlNoAuth(
            boolean useKeycloak
    ) throws InterruptedException {
        // Get Apicurio Registry
        ApicurioRegistry apicurioRegistryKafkasqlNoAuth = ApicurioRegistryResourceType.getDefaultKafkasql(
                Constants.REGISTRY,
                Environment.NAMESPACE
        );

        if (useKeycloak) {
            ApicurioRegistryResourceType.updateWithDefaultKeycloak(apicurioRegistryKafkasqlNoAuth);
        }

        // Create Apicurio Registry without authentication
        ResourceManager.getInstance().createResource(true, apicurioRegistryKafkasqlNoAuth);

        return apicurioRegistryKafkasqlNoAuth;
    }

    public static ApicurioRegistry deployDefaultApicurioRegistryKafkasqlTLS(
            Kafka kafka,
            boolean useKeycloak
    ) throws InterruptedException {
        // Get Apicurio Registry
        ApicurioRegistry apicurioRegistryKafkasqlTLS = ApicurioRegistryResourceType.getDefaultKafkasql(
                Constants.REGISTRY,
                Environment.NAMESPACE
        );

        // Update Apicurio Registry to have TLS configuration
        ApicurioRegistryResourceType.updateWithDefaultTLS(apicurioRegistryKafkasqlTLS);

        CertificateUtils.createTruststore(
                kafka.getMetadata().getNamespace(),
                kafka.getMetadata().getName() + "-cluster-ca-cert",
                getTruststoreSecretName(apicurioRegistryKafkasqlTLS)
        );

        CertificateUtils.createKeystore(
                kafka.getMetadata().getNamespace(),
                Constants.KAFKA_USER,
                getKeystoreSecretName(apicurioRegistryKafkasqlTLS),
                kafka.getMetadata().getName() + "-kafka-bootstrap"
        );

        if (useKeycloak) {
            ApicurioRegistryResourceType.updateWithDefaultKeycloak(apicurioRegistryKafkasqlTLS);
        }

        // Create Apicurio Registry with TLS configuration
        ResourceManager.getInstance().createResource(true, apicurioRegistryKafkasqlTLS);

        return apicurioRegistryKafkasqlTLS;
    }

    public static ApicurioRegistry deployDefaultApicurioRegistryKafkasqlSCRAM(
            Kafka kafka,
            boolean useKeycloak
    ) throws InterruptedException {
        // Get Apicurio Registry
        ApicurioRegistry apicurioRegistryKafkasqlSCRAM = ApicurioRegistryResourceType.getDefaultKafkasql(
                Constants.REGISTRY,
                Environment.NAMESPACE
        );

        // Update to have SCRAM configuration
        ApicurioRegistryResourceType.updateWithDefaultSCRAM(apicurioRegistryKafkasqlSCRAM);

        CertificateUtils.createTruststore(
                kafka.getMetadata().getNamespace(),
                kafka.getMetadata().getName() + "-cluster-ca-cert",
                getTruststoreSecretName(apicurioRegistryKafkasqlSCRAM)
        );

        if (useKeycloak) {
            ApicurioRegistryResourceType.updateWithDefaultKeycloak(apicurioRegistryKafkasqlSCRAM);
        }

        // Create Apicurio Registry with SCRAM configuration
        ResourceManager.getInstance().createResource(true, apicurioRegistryKafkasqlSCRAM);

        return apicurioRegistryKafkasqlSCRAM;
    }

    public static boolean waitApicurioRegistryHostnameReady(ApicurioRegistry apicurioRegistry) {
        String name = apicurioRegistry.getMetadata().getName();
        String namespace = apicurioRegistry.getMetadata().getNamespace();
        String info = MessageFormat.format("with name {0} in namespace {1}", name, namespace);
        TimeoutBudget timeout = TimeoutBudget.ofDuration(Duration.ofMinutes(3));

        LOGGER.info("Waiting for hostname of ApicurioRegistry {} to be ready...", info);

        while (!timeout.timeoutExpired()) {
            if (isApicurioRegistryHostnameReady(apicurioRegistry)) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return false;
            }
        }

        if (!isApicurioRegistryHostnameReady(apicurioRegistry)) {
            LOGGER.error("Hostname of ApicurioRegistry {} failed readiness check.", info);

            return false;
        }

        return true;
    }

    public static String getApicurioRegistryHostname(ApicurioRegistry apicurioRegistry) {
        Route route = Kubernetes.getRoute(apicurioRegistry);

        if (route == null) {
            return null;
        }

        return Kubernetes.getRouteHost(route.getMetadata().getNamespace(), route.getMetadata().getName());
    }

    public static boolean isApicurioRegistryHostnameReady(ApicurioRegistry apicurioRegistry) {
        // Apicurio Registry values
        String registryName = apicurioRegistry.getMetadata().getName();
        String registryNamespace = apicurioRegistry.getMetadata().getNamespace();
        String defaultRegistryHostname = registryName + "." + registryNamespace;

        // Get Route
        Route registryRoute = Kubernetes.getRoute(apicurioRegistry);

        if (registryRoute == null) {
            return false;
        }

        String registryRouteNamespace = registryRoute.getMetadata().getNamespace();
        String registryRouteName = registryRoute.getMetadata().getName();

        return (
                Kubernetes.isRouteReady(registryRouteNamespace, registryRouteName)
                && !defaultRegistryHostname.equals(Kubernetes.getRouteHost(registryRouteNamespace, registryRouteName))
        );
    }

    public static boolean waitApicurioRegistryReady(ApicurioRegistry apicurioRegistry) {
        ApicurioRegistryResourceType registryResourceType = new ApicurioRegistryResourceType();
        TimeoutBudget timeoutBudget = TimeoutBudget.ofDuration(registryResourceType.getTimeout());

        while (!timeoutBudget.timeoutExpired()) {
            if (registryResourceType.isReady(apicurioRegistry)) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return false;
            }
        }

        return registryResourceType.isReady(apicurioRegistry);
    }

    public static ApicurioRegistry deployDefaultApicurioRegistryOAuthKafka() throws InterruptedException {
        // Get Apicurio Registry
        ApicurioRegistry apicurioRegistryOAuthKafka = ApicurioRegistryResourceType.getDefaultOAuthKafka(
                Constants.REGISTRY,
                Environment.NAMESPACE
        );

        CertificateUtils.createOAuthTruststore(
                Environment.NAMESPACE,
                Constants.OAUTH_KAFKA_ROUTER_CERTS,
                Constants.OAUTH_KAFKA_TRUSTSTORE_SECRET_NAME
        );

        // Create Apicurio Registry
        ResourceManager.getInstance().createResource(true, apicurioRegistryOAuthKafka);

        return apicurioRegistryOAuthKafka;
    }

    /* Environment variables processing */

    public static boolean envVarExists(ApicurioRegistry apicurioRegistry, String envVarName) {
        List<Env> currentEnv = apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getEnv();

        if (currentEnv == null) {
            return false;
        }

        return currentEnv
                .stream()
                .anyMatch(ev -> ev.getName().equals(envVarName));
    }

    public static Env getEnvVar(ApicurioRegistry apicurioRegistry, String envVarName) {
        List<Env> currentEnv = apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getEnv();

        if (currentEnv == null) {
            return null;
        }

        return currentEnv
                .stream()
                .filter(ev -> ev.getName().equals(envVarName))
                .findFirst()
                .orElse(null);
    }

    public static void addEnvVar(ApicurioRegistry apicurioRegistry, Env envVar) {
        List<Env> currentEnv = apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getEnv();

        if (currentEnv == null) {
            currentEnv = new ArrayList<>();
        }

        currentEnv.add(envVar);

        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .setEnv(currentEnv);
    }

    public static void removeEnvVar(ApicurioRegistry apicurioRegistry, String envVarName) {
        List<Env> currentEnv = apicurioRegistry
                .getSpec()
                .getConfiguration()
                .getEnv();

        if (currentEnv == null) {
            return;
        }

        currentEnv.remove(getEnvVar(apicurioRegistry, envVarName));

        apicurioRegistry
                .getSpec()
                .getConfiguration()
                .setEnv(currentEnv);
    }

    public static ApicurioRegistry processChange(ApicurioRegistry apicurioRegistry) {
        String namespace = apicurioRegistry.getMetadata().getNamespace();
        String name = apicurioRegistry.getMetadata().getName();

        Kubernetes.createOrReplaceResources(namespace, Collections.singletonList(apicurioRegistry));

        Assertions.assertTrue(waitApicurioRegistryReady(apicurioRegistry));

        Assertions.assertTrue(waitApicurioRegistryHostnameReady(apicurioRegistry));

        return (new ApicurioRegistryResourceType()).get(namespace, name);
    }

    public static void deleteEnvVar(ApicurioRegistry apicurioRegistry, String envVarName) {
        deleteEnvVars(apicurioRegistry, Collections.singletonList(envVarName));
    }

    public static void deleteEnvVars(ApicurioRegistry apicurioRegistry, List<String> envVarNames) {
        // Get registry name
        String dName = apicurioRegistry.getMetadata().getName();
        // Flag to indicate if registry was changed
        boolean changed = false;

        for (String evn : envVarNames) {
            // If environment variable already exists in registry
            if (envVarExists(apicurioRegistry, evn)) {
                // Log information about current action
                LOGGER.info("Deleting environment variable {} of registry {}.", evn, dName);

                // Delete environment variable
                removeEnvVar(apicurioRegistry, evn);

                changed = true;
            } else {
                // Log information about current action
                LOGGER.info("Environment variable {} is not present in registry {}.", evn, dName);
            }
        }

        if (changed) {
            // Process change and get registry
            apicurioRegistry = processChange(apicurioRegistry);

            for (String evn : envVarNames) {
                // Check deletion of environment variable
                Assertions.assertNull(
                        getEnvVar(apicurioRegistry, evn),
                        MessageFormat.format("Environment variable {0} of registry {1} was NOT deleted.", evn, dName)
                );
            }
        }
    }

    public static ApicurioRegistry getApicurioRegistry(ApicurioRegistry apicurioRegistry) {
        return (new ApicurioRegistryResourceType()).get(
                apicurioRegistry.getMetadata().getNamespace(),
                apicurioRegistry.getMetadata().getName()
        );
    }

    public static void createOrReplaceEnvVar(ApicurioRegistry apicurioRegistry, Env envVar) {
        createOrReplaceEnvVars(apicurioRegistry, Collections.singletonList(envVar));
    }

    public static void createOrReplaceEnvVars(ApicurioRegistry apicurioRegistry, List<Env> envVars) {
        // Get fresh registry state
        apicurioRegistry = getApicurioRegistry(apicurioRegistry);
        // Get registry name
        String dName = apicurioRegistry.getMetadata().getName();
        // Flag to indicate if registry was changed
        boolean changed = false;

        for (Env ev : envVars) {
            String evName = ev.getName();
            String evValue = ev.getValue();

            // If environment variable does not exist
            if (!envVarExists(apicurioRegistry, evName)) {
                // Log information about current action
                LOGGER.info("Adding environment variable {} with value {} to registry {}.", evName, evValue, dName);

                addEnvVar(apicurioRegistry, new Env() {{ setName(evName); setValue(evValue); }});

                changed = true;
            } else if (!getEnvVar(apicurioRegistry, evName).getValue().equals(evValue)) {
                // If environment variable exists, but has another value

                // Log information about current action
                LOGGER.info("Setting environment variable {} of registry {} to {}.", evName, dName, evValue);

                // Set value of environment variable
                getEnvVar(apicurioRegistry, evName).setValue(evValue);

                changed = true;
            } else {
                // Log information about current action
                LOGGER.warn("Environment variable {} of registry {} is already set to {}.", evName, dName, evValue);
            }
        }

        if (changed) {
            // Process change and get registry
            apicurioRegistry = processChange(apicurioRegistry);

            for (Env ev : envVars) {
                // Check value of environment variable
                Assertions.assertEquals(getEnvVar(apicurioRegistry, ev.getName()).getValue(), ev.getValue(),
                        MessageFormat.format("Environment variable {0} of registry {1} was NOT set to {2}.",
                                ev.getName(), dName, ev.getValue()
                        )
                );
            }
        }
    }
}
