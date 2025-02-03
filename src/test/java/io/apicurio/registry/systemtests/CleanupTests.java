package io.apicurio.registry.systemtests;

import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.fabric8.openshift.api.model.operatorhub.v1alpha1.ClusterServiceVersion;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class CleanupTests {
    private static final Logger LOGGER = LoggerUtils.getLogger();

    @Test
    public void cleanupCluster() {
        // Namespace with all test resources
        if (Kubernetes.getNamespace(Environment.NAMESPACE) != null) {
            LOGGER.info("Deleting Namespace {}...", Environment.NAMESPACE);

            Kubernetes.deleteNamespace(Environment.NAMESPACE);
        }

        // Namespace with PostgreSQL database
        if (Kubernetes.getNamespace(Constants.DB_NAMESPACE) != null) {
            LOGGER.info("Deleting Namespace {}...", Constants.DB_NAMESPACE);

            Kubernetes.deleteNamespace(Constants.DB_NAMESPACE);
        }

        // ### KAFKA OPERATOR
        // Subscription of Kafka operator
        if (Kubernetes.getSubscription(Environment.CLUSTER_WIDE_NAMESPACE, Constants.KAFKA_SUBSCRIPTION_NAME) != null) {
            LOGGER.info("Deleting Subscription {}...", Constants.KAFKA_SUBSCRIPTION_NAME);

            Kubernetes.deleteSubscription(Environment.CLUSTER_WIDE_NAMESPACE, Constants.KAFKA_SUBSCRIPTION_NAME);
        }

        // ClusterServiceVersion of Kafka operator
        ClusterServiceVersion csvKafka = Kubernetes.getClusterServiceVersionByPrefix(
                Environment.CLUSTER_WIDE_NAMESPACE,
                Constants.KAFKA_CSV_PREFIX
        );

        if (csvKafka != null) {
            LOGGER.info("Deleting ClusterServiceVersion {}...", csvKafka.getMetadata().getName());

            Kubernetes.deleteClusterServiceVersion(
                    csvKafka.getMetadata().getNamespace(),
                    csvKafka.getMetadata().getName()
            );
        }

        // ### REGISTRY OPERATOR
        // Subscription of clusterwide Registry operator
        if (Kubernetes.getSubscription(Environment.CLUSTER_WIDE_NAMESPACE, Constants.REGISTRY_SUBSCRIPTION) != null) {
            LOGGER.info("Deleting Subscription {}...", Constants.REGISTRY_SUBSCRIPTION);

            Kubernetes.deleteSubscription(Environment.CLUSTER_WIDE_NAMESPACE, Constants.REGISTRY_SUBSCRIPTION);
        }

        // ClusterServiceVersion of Registry operator
        ClusterServiceVersion csvRegistryClusterWide = Kubernetes.getClusterServiceVersionByPrefix(
                Environment.CLUSTER_WIDE_NAMESPACE,
                Constants.REGISTRY_CSV_PREFIX
        );

        if (csvRegistryClusterWide != null) {
            LOGGER.info("Deleting ClusterServiceVersion {}...", csvRegistryClusterWide.getMetadata().getName());

            Kubernetes.deleteClusterServiceVersion(
                    csvRegistryClusterWide.getMetadata().getNamespace(),
                    csvRegistryClusterWide.getMetadata().getName()
            );
        }

        // ### REGISTRY OPERATOR
        // Subscription of namespace Registry operator
        if (Kubernetes.getSubscription(Environment.NAMESPACE, Constants.REGISTRY_SUBSCRIPTION) != null) {
            LOGGER.info("Deleting Subscription {}...", Constants.REGISTRY_SUBSCRIPTION);

            Kubernetes.deleteSubscription(Environment.NAMESPACE, Constants.REGISTRY_SUBSCRIPTION);
        }

        // ClusterServiceVersion of Registry operator
        ClusterServiceVersion csvRegistryNamespaced = Kubernetes.getClusterServiceVersionByPrefix(
                Environment.NAMESPACE,
                Constants.REGISTRY_CSV_PREFIX
        );

        if (csvRegistryNamespaced != null) {
            LOGGER.info("Deleting ClusterServiceVersion {}...", csvRegistryNamespaced.getMetadata().getName());

            Kubernetes.deleteClusterServiceVersion(
                    csvRegistryNamespaced.getMetadata().getNamespace(),
                    csvRegistryNamespaced.getMetadata().getName()
            );
        }

        // CatalogSource
        if (Kubernetes.getCatalogSource(Environment.CATALOG_NAMESPACE, Constants.CATALOG_NAME) != null) {
            LOGGER.info("Deleting CatalogSource {}...", Constants.CATALOG_NAME);

            Kubernetes.deleteCatalogSource(Environment.CATALOG_NAMESPACE, Constants.CATALOG_NAME);
        }
    }
}
