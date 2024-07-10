package io.apicurio.registry.systemtests.framework;

import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.apicurio.registry.systemtests.registryinfra.resources.DeploymentResourceType;
import io.apicurio.registry.systemtests.registryinfra.resources.ServiceResourceType;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.HashMap;

public class DatabaseUtils {
    public static void deployDefaultPostgresqlDatabase() {
        Deployment deployment = DeploymentResourceType.getDefaultPostgresql();
        Service service = ServiceResourceType.getDefaultPostgresql();

        try {
            ResourceManager.getInstance().createResource(true, deployment);
            ResourceManager.getInstance().createResource(false, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deployPostgresqlDatabase(String name, String namespace) {
        Deployment deployment = DeploymentResourceType.getDefaultPostgresql(name, namespace);
        Service service = ServiceResourceType.getDefaultPostgresql(name, namespace);

        try {
            ResourceManager.getInstance().createResource(true, deployment);
            ResourceManager.getInstance().createResource(false, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deployPostgresqlDatabase(String name, String namespace, String databaseName) {
        Deployment deployment = DeploymentResourceType.getDefaultPostgresql(name, namespace, databaseName);
        Service service = ServiceResourceType.getDefaultPostgresql(name, namespace);

        try {
            ResourceManager.getInstance().createResource(true, deployment);
            ResourceManager.getInstance().createResource(false, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deployKeycloakPostgresqlDatabase() {
        DatabaseUtils.deployPostgresqlDatabase("keycloak-db", Environment.NAMESPACE, "keycloak");
    }

    public static void createKeycloakPostgresqlDatabaseSecret() throws InterruptedException {
        Secret dbSecret = new Secret();
        dbSecret.setMetadata(new ObjectMeta() {{
            setName(Constants.SSO_DB_SECRET_NAME);
            setNamespace(Environment.NAMESPACE);
        }});
        dbSecret.setType("Opaque");
        dbSecret.setData(new HashMap<>() {{
            put("password", Base64Utils.encode(Constants.DB_PASSWORD));
            put("username", Base64Utils.encode(Constants.DB_USERNAME));
        }});

        ResourceManager.getInstance().createResource(true, dbSecret);
    }
}
