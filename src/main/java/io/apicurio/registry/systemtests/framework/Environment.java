package io.apicurio.registry.systemtests.framework;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Environment {
    /** Environment variables */
    public static final String CATALOG_IMAGE_ENV_VAR = "CATALOG_IMAGE";
    public static final String CATALOG_ENV_VAR = "CATALOG";
    public static final String SSO_CATALOG_ENV_VAR = "SSO_CATALOG";
    public static final String KAFKA_CATALOG_ENV_VAR = "KAFKA_CATALOG";
    public static final String CATALOG_NAMESPACE_ENV_VAR = "CATALOG_NAMESPACE";
    public static final String CONVERTERS_SHA512SUM_ENV_VAR = "CONVERTERS_SHA512SUM";
    public static final String CONVERTERS_URL_ENV_VAR = "CONVERTERS_URL";
    public static final String KAFKA_BUNDLE_ENV_VAR = "KAFKA_BUNDLE";
    public static final String KAFKA_DEPLOYMENT_ENV_VAR = "KAFKA_DEPLOYMENT";
    public static final String KAFKA_PACKAGE_ENV_VAR = "KAFKA_PACKAGE";
    public static final String POSTGRESQL_VERSION_ENV_VAR = "POSTGRESQL_VERSION";
    public static final String REGISTRY_BUNDLE_ENV_VAR = "REGISTRY_BUNDLE";
    public static final String REGISTRY_CHANNEL_ENV_VAR = "REGISTRY_CHANNEL";
    public static final String REGISTRY_CSV_ENV_VAR = "REGISTRY_CSV";
    public static final String REGISTRY_HOSTNAME_ENV_VAR = "REGISTRY_HOSTNAME";
    public static final String REGISTRY_OPERATOR_DEPLOYMENT_NAME_ENV_VAR = "REGISTRY_OPERATOR_DEPLOYMENT_NAME";
    public static final String REGISTRY_OPERATOR_DEPLOYMENT_NAME_UPGRADE_ENV_VAR
            = "REGISTRY_OPERATOR_DEPLOYMENT_NAME_UPGRADE";
    public static final String REGISTRY_PACKAGE_ENV_VAR = "REGISTRY_PACKAGE";
    public static final String SSO_PACKAGE_ENV_VAR = "SSO_PACKAGE";
    public static final String SSO_CHANNEL_ENV_VAR = "SSO_CHANNEL";
    public static final String TESTSUITE_PATH_ENV_VAR = "TESTSUITE_PATH";
    public static final String TMP_PATH_ENV_VAR = "TMP_PATH";

    public static final String FORCE_NAMESPACE = "FORCE_NAMESPACE";
    public static final String DELETE_RESOURCES_ENV_VAR = "DELETE_RESOURCES";
    public static final String DEPLOY_KEYCLOAK_ENV_VAR = "DEPLOY_KEYCLOAK";
    public static final String DEPLOY_KAFKA_ENV_VAR = "DEPLOY_KAFKA";

    /** Default values of environment variables */
    public static final String CATALOG_DEFAULT = "redhat-operators";
    public static final String SSO_CATALOG_DEFAULT = "redhat-operators";
    public static final String KAFKA_CATALOG_DEFAULT = "redhat-operators";
    public static final String KAFKA_DEPLOYMENT_DEFAULT = "amq-streams-cluster-operator"; // Default from catalog
    public static final String KAFKA_PACKAGE_DEFAULT = "amq-streams"; // Default from catalog
    public static final String POSTGRESQL_VERSION_DEFAULT = "17";
    public static final String REGISTRY_CHANNEL_DEFAULT = "3.x";
    public static final String REGISTRY_CSV_DEFAULT = "apicurio-registry-3.v3.0.15-r1";
    public static final String REGISTRY_BUNDLE_DEFAULT =
            "https://raw.githubusercontent.com/Apicurio/apicurio-registry-operator/main/install/" +
                    "apicurio-registry-operator-1.0.0-v2.0.0.final.yaml";
    public static final String REGISTRY_OPERATOR_DEPLOYMENT_NAME_DEFAULT = "apicurio-registry-operator-v3.0.15-r1";
    public static final String REGISTRY_OPERATOR_DEPLOYMENT_NAME_UPGRADE_DEFAULT
            = "apicurio-registry-operator-v3.0.15-r1";
    public static final String REGISTRY_PACKAGE_DEFAULT = "apicurio-registry-3"; // Default from catalog
    public static final String SSO_PACKAGE_DEFAULT = "rhbk-operator"; // Default from catalog
    public static final String SSO_CHANNEL_DEFAULT = "stable-v26";
    public static final String TESTSUITE_PATH_DEFAULT = System.getProperty("user.dir");
    public static final String TMP_PATH_DEFAULT = "/tmp";

    public static final String DELETE_RESOURCES_DEFAULT = "true";
    public static final String DEPLOY_KEYCLOAK_DEFAULT = "true";
    public static final String DEPLOY_KAFKA_DEFAULT = "true";

    /** Collecting environment variables */
    public static final String CATALOG_IMAGE = get(CATALOG_IMAGE_ENV_VAR);
    public static final String CATALOG = getOrDefault(CATALOG_ENV_VAR, CATALOG_DEFAULT);
    public static final String SSO_CATALOG = getOrDefault(SSO_CATALOG_ENV_VAR, SSO_CATALOG_DEFAULT);
    public static final String KAFKA_CATALOG = getOrDefault(KAFKA_CATALOG_ENV_VAR, KAFKA_CATALOG_DEFAULT);

    public static final String CATALOG_NAMESPACE = getOrDefault(CATALOG_NAMESPACE_ENV_VAR, Constants.CATALOG_NAMESPACE);
    public static final String CONVERTERS_SHA512SUM = get(CONVERTERS_SHA512SUM_ENV_VAR);
    public static final String CONVERTERS_URL = get(CONVERTERS_URL_ENV_VAR);

    public static final String KAFKA_DEPLOYMENT = getOrDefault(KAFKA_DEPLOYMENT_ENV_VAR, KAFKA_DEPLOYMENT_DEFAULT);
    public static final String KAFKA_PACKAGE = getOrDefault(KAFKA_PACKAGE_ENV_VAR, KAFKA_PACKAGE_DEFAULT);
    public static final String POSTGRESQL_VERSION
            = getOrDefault(POSTGRESQL_VERSION_ENV_VAR, POSTGRESQL_VERSION_DEFAULT);
    public static final String REGISTRY_BUNDLE = getOrDefault(REGISTRY_BUNDLE_ENV_VAR, REGISTRY_BUNDLE_DEFAULT);
    public static final String REGISTRY_CHANNEL = getOrDefault(REGISTRY_CHANNEL_ENV_VAR, REGISTRY_CHANNEL_DEFAULT);
    public static final String REGISTRY_CSV = getOrDefault(REGISTRY_CSV_ENV_VAR, REGISTRY_CSV_DEFAULT);
    public static final String REGISTRY_HOSTNAME = get(REGISTRY_HOSTNAME_ENV_VAR);
    public static final String REGISTRY_PACKAGE = getOrDefault(REGISTRY_PACKAGE_ENV_VAR, REGISTRY_PACKAGE_DEFAULT);
    public static final String SSO_PACKAGE = getOrDefault(SSO_PACKAGE_ENV_VAR, SSO_PACKAGE_DEFAULT);
    public static final String SSO_CHANNEL = getOrDefault(SSO_CHANNEL_ENV_VAR, SSO_CHANNEL_DEFAULT);
    public static final String TESTSUITE_PATH = getOrDefault(TESTSUITE_PATH_ENV_VAR, TESTSUITE_PATH_DEFAULT);
    public static final String TMP_PATH = getOrDefault(TMP_PATH_ENV_VAR, TMP_PATH_DEFAULT);

    public static final String NAMESPACE = getOrDefault(FORCE_NAMESPACE, Constants.TESTSUITE_NAMESPACE);
    public static final boolean DELETE_RESOURCES = Boolean
            .parseBoolean(getOrDefault(DELETE_RESOURCES_ENV_VAR, DELETE_RESOURCES_DEFAULT));
    public static final boolean DEPLOY_KEYCLOAK = Boolean
            .parseBoolean(getOrDefault(DEPLOY_KEYCLOAK_ENV_VAR, DEPLOY_KEYCLOAK_DEFAULT));
    public static final boolean DEPLOY_KAFKA = Boolean
            .parseBoolean(getOrDefault(DEPLOY_KAFKA_ENV_VAR, DEPLOY_KAFKA_DEFAULT));

    public static final String KAFKA_BUNDLE_DEFAULT =
            "https://strimzi.io/install/latest?namespace=" + NAMESPACE;

    public static final String KAFKA_BUNDLE = getOrDefault(KAFKA_BUNDLE_ENV_VAR, KAFKA_BUNDLE_DEFAULT);

    public static final String CLUSTER_WIDE_NAMESPACE =  "openshift-operators";
    public static final String REGISTRY_OPERATOR_DEPLOYMENT_NAME
            = getOrDefault(REGISTRY_OPERATOR_DEPLOYMENT_NAME_ENV_VAR, REGISTRY_OPERATOR_DEPLOYMENT_NAME_DEFAULT);
    public static final String REGISTRY_OPERATOR_DEPLOYMENT_NAME_UPGRADE
            = getOrDefault(
                    REGISTRY_OPERATOR_DEPLOYMENT_NAME_UPGRADE_ENV_VAR,
                    REGISTRY_OPERATOR_DEPLOYMENT_NAME_UPGRADE_DEFAULT
            );


    private static String get(String key) {
        return System.getenv().get(key);
    }

    private static String getOrDefault(String key, String defaultValue) {
        return System.getenv().getOrDefault(key, defaultValue);
    }

    public static Path getTmpPath(String filename) {
        return Paths.get(TMP_PATH, filename);
    }
}
