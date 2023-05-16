package io.apicurio.registry.systemtests.infra.component.impl;

import io.apicurio.registry.systemtests.infra.component.InfraComponent;

public class Keycloak implements InfraComponent {

    @Override
    public boolean isReady() {
        return true;
    }
}
