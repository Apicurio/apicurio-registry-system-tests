package io.apicurio.registry.systemtests.framework;

import io.apicurio.registry.systemtests.executor.Exec;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertificateUtils {
    private static final Logger LOGGER = LoggerUtils.getLogger();

    /**
     * @param path Path to the new truststore
     * @param password Password for the new truststore
     * @param publicKey Path to the public key to be imported
     */
    private static void runTruststoreCmd(Path path, String password, Path publicKey) {
        Exec.executeAndCheck(
                "keytool",
                "-keystore", path.toString(),
                "-storepass", password,
                "-noprompt",
                "-alias", "ca",
                "-import",
                "-file", publicKey.toString(),
                "-storetype", "PKCS12"
        );
    }

    /**
     * @param path Path to the new keystore
     * @param password Password for the new keystore
     * @param publicKey Public key to be imported
     * @param privateKey Private key to be imported
     */
    private static void runKeystoreCmd(Path path, String password, Path publicKey, Path privateKey, String hostname) {
        List<String> commands = List.of(
                "openssl",
                "pkcs12",
                "-export",
                "-in", publicKey.toString(),
                "-inkey", privateKey.toString(),
                "-name", hostname,
                "-password", "pass:" + password,
                "-out", path.toString()
        );

        Exec.executeAndCheck(
                commands,
                60_000,
                true,
                true,
                Collections.singletonMap("RANDFILE", Environment.getTmpPath(".rnd").toString())
        );
    }

    private static String decodeBase64Secret(String namespace, String name, String key) {
        return Base64Utils.decode(Kubernetes.getSecretValue(namespace, name, key));
    }

    private static void writeToFile(String data, Path path) {
        try {
            Files.writeString(path, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createSecret(
            String namespace, String name, Map<String, String> secretData, boolean shared
    ) throws InterruptedException {
        Secret secret = new SecretBuilder()
                .withNewMetadata()
                    .withName(name)
                    .withNamespace(namespace)
                .endMetadata()
                .addToData(secretData)
                .build();

        if (shared) {
            ResourceManager.getInstance().createSharedResource(true, secret);
        } else {
            ResourceManager.getInstance().createResource(true, secret);
        }
    }

    public static void createTruststore(
            String namespace,
            String caCertSecretName,
            String truststoreSecretName
    ) throws InterruptedException {
        LOGGER.info("Preparing truststore...");

        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String caCertSecretValue = decodeBase64Secret(namespace, caCertSecretName, "ca.crt");
        Path caPath = Environment.getTmpPath("ca-" + timestamp + ".crt");

        writeToFile(caCertSecretValue, caPath);

        Path truststorePath = Environment.getTmpPath("truststore-" + timestamp + ".p12");
        String truststorePassword = RandomStringUtils.randomAlphanumeric(32);

        runTruststoreCmd(truststorePath, truststorePassword, caPath);

        Map<String, String> secretData = new HashMap<>() {{
            put("ca.p12", Base64Utils.encode(truststorePath));
            put("ca.password", Base64Utils.encode(truststorePassword));
        }};

        createSecret(namespace, truststoreSecretName, secretData, false);
    }

    public static void createSslTruststore(
            String namespace,
            String caCertSecretName,
            String truststoreSecretName,
            String keyName,
            boolean shared
    ) throws InterruptedException {
        LOGGER.info("Preparing SSL truststore...");

        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String caCertSecretValue = decodeBase64Secret(namespace, caCertSecretName, keyName);
        Path caPath = Environment.getTmpPath("tls-" + timestamp + ".crt");

        writeToFile(caCertSecretValue, caPath);

        Path truststorePath = Environment.getTmpPath("truststore-" + timestamp + ".p12");

        runTruststoreCmd(truststorePath, "password", caPath);

        Map<String, String> secretData = new HashMap<>() {{
            put(Constants.TRUSTSTORE_DATA_NAME, Base64Utils.encode(truststorePath));
        }};

        createSecret(namespace, truststoreSecretName, secretData, shared);
    }

    public static void createKeystore(
            String namespace,
            String clientCertSecretName,
            String keystoreSecretName,
            String hostname
    ) throws InterruptedException {
        LOGGER.info("Preparing keystore...");

        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        Path userCertPath = Environment.getTmpPath("user-" + timestamp + ".crt");

        writeToFile(decodeBase64Secret(namespace, clientCertSecretName, "user.crt"), userCertPath);

        Path userKeyPath = Environment.getTmpPath("user-" + timestamp + ".key");

        writeToFile(decodeBase64Secret(namespace, clientCertSecretName, "user.key"), userKeyPath);

        Path keystorePath = Environment.getTmpPath("keystore-" + timestamp + ".p12");
        String keystorePassword = RandomStringUtils.randomAlphanumeric(32);

        runKeystoreCmd(keystorePath, keystorePassword, userCertPath, userKeyPath, hostname);

        Map<String, String> secretData = new HashMap<>() {{
            put("user.p12", Base64Utils.encode(keystorePath));
            put("user.password", Base64Utils.encode(keystorePassword));
        }};

        createSecret(namespace, keystoreSecretName, secretData, false);
    }

    public static ArrayList<Certificate> readCertificates(String text) {
        String[] lines = text.split(System.lineSeparator());
        ArrayList<Certificate> returnList = new ArrayList<>();
        Certificate c = null;

        for (String l : lines) {
            System.out.println(l);

            if (l.startsWith("-----BEGIN CERTIFICATE-----")) {
                c = new Certificate(CertificateType.CRT);
                c.addLine(l);
            } else if (l.startsWith("-----END CERTIFICATE-----")) {
                c.addLine(l);
                returnList.add(c);
            } else if (l.startsWith("-----BEGIN PRIVATE KEY-----")) {
                c = new Certificate(CertificateType.KEY);
                c.addLine(l);
            } else if (l.startsWith("-----END PRIVATE KEY-----")) {
                c.addLine(l);
                returnList.add(c);
            } else if (l.startsWith("-----BEGIN RSA PRIVATE KEY-----")) {
                c = new Certificate(CertificateType.KEY);
                c.addLine(l);
            } else if (l.startsWith("-----END RSA PRIVATE KEY-----")) {
                c.addLine(l);
                returnList.add(c);
            } else {
                c.addLine(l);
            }
        }

        return returnList;
    }

    public static String getCertificates(ArrayList<Certificate> certList) {
        StringBuilder certificates = new StringBuilder();

        for (Certificate c : certList) {
            if (c.getType().equals(CertificateType.CRT)) {
                certificates.append(c.getCertificate());
            }
        }

        return certificates.toString();
    }

    public static String getKeys(ArrayList<Certificate> certList) {
        StringBuilder keys = new StringBuilder();

        for (Certificate k : certList) {
            if (k.getType().equals(CertificateType.KEY)) {
                keys.append(k.getCertificate());
            }
        }

        return keys.toString();
    }
}
