package io.apicurio.registry.systemtests.registryinfra.resources;

import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;

import java.time.Duration;
import java.util.HashMap;

public class SecretResourceType implements ResourceType<Secret> {

    @Override
    public Duration getTimeout() {
        return Duration.ofMinutes(1);
    }

    @Override
    public String getKind() {
        return ResourceKind.SECRET;
    }

    @Override
    public Secret get(String namespace, String name) {
        return Kubernetes.getSecret(namespace, name);
    }

    @Override
    public void create(Secret resource) {
        Kubernetes.createSecret(resource.getMetadata().getNamespace(), resource);
    }

    @Override
    public void createOrReplace(Secret resource) {
        Kubernetes.createOrReplaceSecret(resource.getMetadata().getNamespace(), resource);
    }

    @Override
    public void delete(Secret resource) throws Exception {
        Kubernetes.deleteSecret(resource.getMetadata().getNamespace(), resource.getMetadata().getName());
    }

    @Override
    public boolean isReady(Secret resource) {
        return get(resource.getMetadata().getNamespace(), resource.getMetadata().getName()) != null;
    }

    @Override
    public boolean doesNotExist(Secret resource) {
        if (resource == null) {
            return true;
        }

        return get(resource.getMetadata().getNamespace(), resource.getMetadata().getName()) == null;
    }

    @Override
    public void refreshResource(Secret existing, Secret newResource) {
        existing.setMetadata(newResource.getMetadata());
    }

    public static Secret getDefaultHttpsSecret() {
        return getHttpsSecret(Environment.NAMESPACE);
    }

    public static Secret getHttpsSecret(String namespace) {
        return new SecretBuilder()
                .withNewMetadata()
                    .withName(Constants.HTTPS_SECRET_NAME)
                    .withNamespace(namespace)
                .endMetadata()
                .withType("Opaque")
                .withData(new HashMap<>() {{
                    put("tls.crt", Constants.HTTPS_SECRET_CRT);
                    put("tls.key", Constants.HTTPS_SECRET_KEY);
                }})
                .build();
    }
}
