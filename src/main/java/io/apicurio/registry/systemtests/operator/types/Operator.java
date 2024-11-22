package io.apicurio.registry.systemtests.operator.types;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Operator {
    /* Contains path to bundle operator file or OLM operator catalog source image. */
    private String source;
    private String namespace;

    public Operator(String source) {
        this.source = source;
    }

    public Operator(String source, String namespace) {
        this.source = source;
        this.namespace = namespace;
    }

}
