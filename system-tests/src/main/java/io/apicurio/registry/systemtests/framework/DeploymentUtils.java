package io.apicurio.registry.systemtests.framework;

import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.time.TimeoutBudget;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DeploymentUtils {

    private static final Logger LOGGER = LoggerUtils.getLogger();

    public static boolean deploymentEnvVarExists(Deployment deployment, EnvVar envVar) {
        return deployment
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .stream()
                .anyMatch(ev -> ev.getName().equals(envVar.getName()));
    }

    public static EnvVar getDeploymentEnvVar(Deployment deployment, String envVarName) {
        return deployment
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .stream()
                .filter(ev -> ev.getName().equals(envVarName))
                .findFirst()
                .orElse(null);
    }

    public static List<EnvVar> getDeploymentEnvVars(Deployment deployment) {
        return deployment
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv();
    }

    public static List<EnvVar> cloneDeploymentEnvVars(Deployment deployment) {
        List<EnvVar> clonedEnvVars = new ArrayList<>();

        for (EnvVar ev : getDeploymentEnvVars(deployment)) {
            clonedEnvVars.add(new EnvVar(ev.getName(), ev.getValue(), ev.getValueFrom()) {{
                setAdditionalProperties(ev.getAdditionalProperties());
            }});
        }

        return clonedEnvVars;
    }

    public static void addDeploymentEnvVar(Deployment deployment, EnvVar envVar) {
        deployment
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .add(envVar);
    }

    public static boolean waitDeploymentReady(String namespace, String name) {
        return waitDeploymentReady(namespace, name, TimeoutBudget.ofDuration(Duration.ofMinutes(5)));
    }

    public static boolean waitDeploymentReady(String namespace, String name, TimeoutBudget timeoutBudget) {
        while (!timeoutBudget.timeoutExpired()) {
            if (Kubernetes.isDeploymentReady(namespace, name)) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return false;
            }
        }

        if (!Kubernetes.isDeploymentReady(namespace, name)) {
            LOGGER.error("Deployment with name {} in namespace {} failed readiness check.", name, namespace);

            return false;
        }

        return true;
    }

    public static void createOrReplaceDeploymentEnvVar(Deployment deployment, EnvVar envVar) {
        // Get deployment namespace
        String deploymentNamespace = deployment.getMetadata().getNamespace();
        // Get deployment name
        String deploymentName = deployment.getMetadata().getName();

        // If environment variable already exists in deployment
        if (deploymentEnvVarExists(deployment, envVar)) {
            // Log information about current action
            LOGGER.info(
                    "Setting environment variable {} of deployment {} to {}.",
                    envVar.getName(), deploymentName, envVar.getValue()
            );

            // Set value of environment variable
            getDeploymentEnvVar(deployment, envVar.getName()).setValue(envVar.getValue());
        } else {
            // Log information about current action
            LOGGER.info(
                    "Adding environment variable {} with value {} to deployment {}.",
                    envVar.getName(), envVar.getValue(), deploymentName
            );

            // Add environment variable if it does not exist yet
            addDeploymentEnvVar(deployment, envVar);
        }

        // Update deployment
        Kubernetes.createOrReplaceDeployment(deploymentNamespace, deployment);

        // Wait for deployment readiness
        waitDeploymentReady(deploymentNamespace, deploymentName);

        // Get deployment
        deployment = Kubernetes.getDeployment(deploymentNamespace, deploymentName);

        // Check value of environment variable
        Assertions.assertTrue(
                getDeploymentEnvVar(deployment, envVar.getName()).getValue().equals(envVar.getValue()),
                MessageFormat.format(
                        "Environment variable {0} of deployment {1} was NOT set to {2}.",
                        envVar.getName(),
                        deploymentName,
                        envVar.getValue()
                )
        );
    }

    public static void setDeploymentEnvVars(Deployment deployment, List<EnvVar> envVars) {
        // Log information about current action
        LOGGER.info("Setting environment variables of deployment {}: {}", deployment.getMetadata().getName(), envVars);

        // Set deployment environment variables
        deployment
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .get(0)
                .setEnv(envVars);

        // Update deployment
        Kubernetes.createOrReplaceDeployment(deployment.getMetadata().getNamespace(), deployment);

        // Wait for deployment readiness
        waitDeploymentReady(deployment.getMetadata().getNamespace(), deployment.getMetadata().getName());
    }
}