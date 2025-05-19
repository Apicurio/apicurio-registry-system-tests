package io.apicurio.registry.systemtests.framework;

import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PodUtils {
    private static ExecWatch execCmd(
            Pod pod,
            CompletableFuture<String> dataOut,
            CompletableFuture<String> dataErr,
            Logger logger,
            String... command
    ) {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        return Kubernetes
                .getClient()
                .pods()
                .inNamespace(pod.getMetadata().getNamespace())
                .withName(pod.getMetadata().getName())
                .writingOutput(stdout)
                .writingError(stderr)
                .usingListener(new PodExecListener(dataOut, dataErr, stdout, stderr, logger))
                .exec(command);
    }

    public static PodExecResult execCommandOnPod(
            Pod pod,
            String namespace,
            Logger logger,
            String... cmd
    ) throws ExecutionException, InterruptedException, TimeoutException {
        logger.info(
                "Running command: {} on pod {} in namespace {}",
                Arrays.toString(cmd),
                pod.getMetadata().getName(),
                namespace
        );

        CompletableFuture<String> dataOut = new CompletableFuture<>();
        CompletableFuture<String> dataErr = new CompletableFuture<>();

        try (ExecWatch execWatch = execCmd(pod, dataOut, dataErr, logger, cmd)) {
            return new PodExecResult(
                    dataOut.get(10, TimeUnit.SECONDS),
                    dataErr.get(10, TimeUnit.SECONDS),
                    execWatch.exitCode().get(10, TimeUnit.SECONDS)
            );
        }
    }

}
