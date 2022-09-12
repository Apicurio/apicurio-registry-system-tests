package io.apicurio.registry.systemtests.auth;

import io.apicurio.registry.operator.api.model.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.auth.features.AnonymousReadAccess;
import io.apicurio.registry.systemtests.auth.features.ArtifactGroupOwnerOnlyAuthorization;
import io.apicurio.registry.systemtests.auth.features.ArtifactOwnerOnlyAuthorization;
import io.apicurio.registry.systemtests.auth.features.AuthenticatedReads;
import io.apicurio.registry.systemtests.auth.features.BasicAuthentication;
import io.apicurio.registry.systemtests.auth.features.RoleBasedAuthorizationApplication;
import io.apicurio.registry.systemtests.auth.features.RoleBasedAuthorizationToken;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AuthTests extends TestBase {
    /* TEST RUNNERS */

    protected void runAnonymousReadAccessTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            AnonymousReadAccess.testAnonymousReadAccess(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    true
            );

            KeycloakUtils.removeKeycloak();
        } else {
            AnonymousReadAccess.testAnonymousReadAccess(registry, null, null, false);
        }
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runBasicAuthenticationTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, true);

        BasicAuthentication.testBasicAuthentication(registry, Constants.SSO_ADMIN_USER, Constants.SSO_USER_PASSWORD);

        KeycloakUtils.removeKeycloak();
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runAuthenticatedReadsTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, true);

        AuthenticatedReads.testAuthenticatedReads(registry);

        KeycloakUtils.removeKeycloak();
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runArtifactOwnerOnlyAuthorizationTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, true);

        ArtifactOwnerOnlyAuthorization.testArtifactOwnerOnlyAuthorization(registry);

        KeycloakUtils.removeKeycloak();
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runArtifactGroupOwnerOnlyAuthorizationTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, true);

        ArtifactGroupOwnerOnlyAuthorization.testArtifactGroupOwnerOnlyAuthorization(registry);

        KeycloakUtils.removeKeycloak();
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runRoleBasedAuthorizationTokenTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, true);

        RoleBasedAuthorizationToken.testRoleBasedAuthorizationToken(registry);

        KeycloakUtils.removeKeycloak();
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runRoleBasedAuthorizationApplicationTest(
            ExtensionContext testContext,
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) {
        ApicurioRegistry registry = deployTestRegistry(testContext, persistenceKind, kafkaKind, true);

        RoleBasedAuthorizationApplication.testRoleBasedAuthorizationApplication(registry);

        KeycloakUtils.removeKeycloak();
    }

    /* TESTS - PostgreSQL */

    @Test
    public void testRegistrySqlNoIAMAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.SQL, null, false);
    }

    @Test
    public void testRegistrySqlKeycloakAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.SQL, null, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlKeycloakBasicAuthentication(ExtensionContext testContext) {
        runBasicAuthenticationTest(testContext, PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlKeycloakAuthenticatedReads(ExtensionContext testContext) {
        runAuthenticatedReadsTest(testContext, PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlKeycloakArtifactOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactOwnerOnlyAuthorizationTest(testContext, PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlKeycloakArtifactGroupOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactGroupOwnerOnlyAuthorizationTest(testContext, PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlKeycloakRoleBasedAuthorizationToken(ExtensionContext testContext) {
        runRoleBasedAuthorizationTokenTest(testContext, PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlKeycloakRoleBasedAuthorizationApplication(ExtensionContext testContext) {
        runRoleBasedAuthorizationApplicationTest(testContext, PersistenceKind.SQL, null);
    }

    /* TESTS - KafkaSQL */

    @Test
    public void testRegistryKafkasqlNoAuthNoIAMAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    public void testRegistryKafkasqlNoAuthKeycloakAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    public void testRegistryKafkasqlTLSNoIAMAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    public void testRegistryKafkasqlSCRAMNoIAMAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakAnonymousReadAccess(ExtensionContext testContext) {
        runAnonymousReadAccessTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistryKafkasqlNoAuthKeycloakBasicAuthentication(ExtensionContext testContext) {
        runBasicAuthenticationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakBasicAuthentication(ExtensionContext testContext) {
        runBasicAuthenticationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakBasicAuthentication(ExtensionContext testContext) {
        runBasicAuthenticationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistryKafkasqlNoAuthKeycloakAuthenticatedReads(ExtensionContext testContext) {
        runAuthenticatedReadsTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakAuthenticatedReads(ExtensionContext testContext) {
        runAuthenticatedReadsTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakAuthenticatedReads(ExtensionContext testContext) {
        runAuthenticatedReadsTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistryKafkasqlNoAuthKeycloakArtifactOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactOwnerOnlyAuthorizationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakArtifactOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactOwnerOnlyAuthorizationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakArtifactOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactOwnerOnlyAuthorizationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistryKafkasqlNoAuthKeycloakArtifactGroupOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactGroupOwnerOnlyAuthorizationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakArtifactGroupOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactGroupOwnerOnlyAuthorizationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakArtifactGroupOwnerOnlyAuthorization(ExtensionContext testContext) {
        runArtifactGroupOwnerOnlyAuthorizationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistryKafkasqlNoAuthKeycloakRoleBasedAuthorizationToken(ExtensionContext testContext) {
        runRoleBasedAuthorizationTokenTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakRoleBasedAuthorizationToken(ExtensionContext testContext) {
        runRoleBasedAuthorizationTokenTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakRoleBasedAuthorizationToken(ExtensionContext testContext) {
        runRoleBasedAuthorizationTokenTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistryKafkasqlNoAuthKeycloakRoleBasedAuthorizationApplication(ExtensionContext testContext) {
        runRoleBasedAuthorizationApplicationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloakRoleBasedAuthorizationApplication(ExtensionContext testContext) {
        runRoleBasedAuthorizationApplicationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloakRoleBasedAuthorizationApplication(ExtensionContext testContext) {
        runRoleBasedAuthorizationApplicationTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
}