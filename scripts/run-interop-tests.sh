FORCE_NAMESPACE=interop-test-namespace \
    REGISTRY_PACKAGE=service-registry-operator \
    REGISTRY_BUNDLE=./scripts/install.yaml \
    KAFKA_PACKAGE=amq-streams \
    KAFKA_DEPLOYMENT=amq-streams-cluster-operator \
    CATALOG=redhat-operators \
    SSO_CHANNEL=stable-v22 \
    mvn test -P interop -Dmaven.repo.local=/tmp/m2
