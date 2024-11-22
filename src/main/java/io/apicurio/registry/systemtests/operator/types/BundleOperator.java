package io.apicurio.registry.systemtests.operator.types;

import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public abstract class BundleOperator extends Operator {
    private List<HasMetadata> resources;

    public BundleOperator(String source, String operatorNamespace) {
        super(source, operatorNamespace);
    }

}
