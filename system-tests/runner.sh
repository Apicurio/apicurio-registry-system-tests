FORCE_NAMESPACE=rkubis-namespace \
    REGISTRY_PACKAGE=service-registry-operator \
    REGISTRY_BUNDLE=./install.yaml \
    KAFKA_PACKAGE=amq-streams \
    KAFKA_DEPLOYMENT=amq-streams-cluster-operator \
    CATALOG=redhat-operators \
    mvn test -Dtest=BundleAPITests#testRegistrySqlNoIAMCreateReadUpdateDelete
