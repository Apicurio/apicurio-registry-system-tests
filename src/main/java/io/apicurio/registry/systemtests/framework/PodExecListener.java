package io.apicurio.registry.systemtests.framework;

import io.fabric8.kubernetes.client.dsl.ExecListener;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class PodExecListener implements ExecListener {

    private final CompletableFuture<String> dataOut;
    private final CompletableFuture<String> dataErr;
    private final ByteArrayOutputStream stdout;
    private final ByteArrayOutputStream stderr;
    private final Logger logger;

    public PodExecListener(
            CompletableFuture<String> dataOut,
            CompletableFuture<String> dataErr,
            ByteArrayOutputStream stdout,
            ByteArrayOutputStream stderr,
            Logger logger
    ) {
        this.dataOut = dataOut;
        this.dataErr = dataErr;
        this.stdout = stdout;
        this.stderr = stderr;
        this.logger = logger;
    }

    @Override
    public void onOpen() {
        logger.info("Reading data...");
    }

    @Override
    public void onFailure(Throwable t, Response failureResponse) {
        logger.error(t.getMessage());

        dataErr.completeExceptionally(t);
    }

    @Override
    public void onClose(int code, String reason) {
        logger.info("Exit with: {} and with reason: {}", code, reason);

        dataOut.complete(stdout.toString());
        dataErr.complete(stderr.toString());
    }
}