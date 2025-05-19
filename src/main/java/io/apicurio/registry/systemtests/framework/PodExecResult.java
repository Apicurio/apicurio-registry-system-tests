package io.apicurio.registry.systemtests.framework;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

@Setter
@Getter
public class PodExecResult {

    private String stdout;
    private String stderr;
    private Integer exitCode;

    public PodExecResult(
            String stdout,
            String stderr,
            Integer exitCode
    ) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCode = exitCode;
    }

    public void logResult(Logger logger) {
        logger.info("--- STDOUT START ---");
        logger.info("{}", stdout);
        logger.info("--- STDOUT END ---");

        logger.info("--- STDERR START ---");
        logger.info("{}", stderr);
        logger.info("--- STDERR END ---");

        logger.info("Exit code: {}", exitCode);
    }
}
