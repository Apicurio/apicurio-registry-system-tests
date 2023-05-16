package io.apicurio.registry.systemtests.infra;

import io.apicurio.registry.systemtests.infra.matrix.Auth;
import io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.util.Arrays;
import java.util.stream.Collectors;

import static io.apicurio.registry.systemtests.infra.matrix.Storage.SQL;

public class SystemTestsExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {

        var injectionPoints = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(f -> Infra.class.equals(f.getType()))
                .collect(Collectors.toList());

        if (injectionPoints.size() != 1) {
            throw new IllegalStateException("TODO");
        }

        var infraConfig = Infra.builder()
                .registryBaseUrl("TODO")
                .currentMatrixValues(Infra.Matrix.builder()
                        .storage(SQL)
                        .auth(Auth.NONE)
                        .kafkaSecurity(KafkaSecurity.NONE)
                        .build())
                .build();

        injectionPoints.get(0).setAccessible(true);
        injectionPoints.get(0).set(testInstance, infraConfig);
    }
}
