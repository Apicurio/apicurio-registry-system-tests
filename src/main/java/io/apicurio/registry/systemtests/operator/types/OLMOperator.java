package io.apicurio.registry.systemtests.operator.types;

import io.apicurio.registry.systemtests.framework.Environment;
import io.apicurio.registry.systemtests.framework.OperatorUtils;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.time.TimeoutBudget;
import io.fabric8.openshift.api.model.operatorhub.v1.OperatorGroup;
import io.fabric8.openshift.api.model.operatorhub.v1alpha1.Subscription;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public abstract class OLMOperator extends Operator {
    private String clusterServiceVersion;
    private boolean clusterWide;
    private Subscription subscription = null;
    private OperatorGroup operatorGroup = null;

    public boolean getClusterWide() {
        return clusterWide;
    }

    public OLMOperator(String source, String operatorNamespace, boolean clusterWide) {
        super(source);
        setNamespace(operatorNamespace);
        setClusterWide(clusterWide);
    }

    public boolean waitSubscriptionCurrentCSV(String catalog, TimeoutBudget timeout) {
        String channel = OperatorUtils.getDefaultChannel(catalog, Environment.REGISTRY_PACKAGE);
        String expectedCSV = OperatorUtils.getCurrentCSV(catalog, Environment.REGISTRY_PACKAGE, channel);
        String subscriptionNamespace = subscription.getMetadata().getNamespace();
        String subscriptionName = subscription.getMetadata().getName();
        Subscription operatorSubscription = Kubernetes.getSubscription(subscriptionNamespace, subscriptionName);

        while (timeout.timeoutNotExpired()) {
            if (operatorSubscription.getStatus().getCurrentCSV().equals(expectedCSV)) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return false;
            }

            operatorSubscription = Kubernetes.getSubscription(subscriptionNamespace, subscriptionName);
        }

        return operatorSubscription.getStatus().getCurrentCSV().equals(expectedCSV);
    }

    public boolean waitSubscriptionCurrentCSV(String catalog) {
        return waitSubscriptionCurrentCSV(catalog, TimeoutBudget.ofDuration(Duration.ofMinutes(3)));
    }

    public boolean waitClusterServiceVersionReady(TimeoutBudget timeout) {
        while (timeout.timeoutNotExpired()) {
            if (Kubernetes.isClusterServiceVersionReady(getNamespace(), getClusterServiceVersion())) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return false;
            }
        }

        return Kubernetes.isClusterServiceVersionReady(getNamespace(), getClusterServiceVersion());
    }

    public boolean waitClusterServiceVersionReady() {
        return waitClusterServiceVersionReady(TimeoutBudget.ofDuration(Duration.ofMinutes(3)));
    }
}
