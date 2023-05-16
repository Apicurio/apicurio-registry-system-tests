package io.apicurio.registry.systemtests.infra.matrix;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Jakub Senko <em>m@jsenko.net</em>
 */
@Target(value = {TYPE})
@Retention(RUNTIME)
public @interface Matrix {

    Storage[] storage();

    Auth[] auth();

    KafkaSecurity[] kafkaSecurity();
}
