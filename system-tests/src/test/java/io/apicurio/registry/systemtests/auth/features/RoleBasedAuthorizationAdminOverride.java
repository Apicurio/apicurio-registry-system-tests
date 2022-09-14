package io.apicurio.registry.systemtests.auth.features;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.client.ApicurioRegistryApiClient;
import io.apicurio.registry.systemtests.client.AuthMethod;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.DeploymentUtils;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.fabric8.kubernetes.api.model.EnvVar;
import org.junit.jupiter.api.Assertions;

public class RoleBasedAuthorizationAdminOverride extends RoleBasedAuthorization {
    public static void initializeClients(ApicurioRegistry apicurioRegistry, String hostname) {
        initializeClients(apicurioRegistry, hostname, true);
    }
    public static void initializeClients(ApicurioRegistry apicurioRegistry, String hostname, boolean useDefaultAdmin) {
        // GET ADMIN API CLIENT
        // Create admin API client
        adminClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update admin API client with it
        adminClient.setToken(
                KeycloakUtils.getAccessToken(
                        apicurioRegistry,
                        useDefaultAdmin ? Constants.SSO_ADMIN_USER : Constants.SSO_ADMIN_USER + "-role",
                        Constants.SSO_USER_PASSWORD
                )
        );
        // Set authentication method to token for admin client
        adminClient.setAuthMethod(AuthMethod.TOKEN);

        // GET DEVELOPER API CLIENT
        // Create developer API client
        developerClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update developer API client with it
        developerClient.setToken(
                KeycloakUtils.getAccessToken(
                        apicurioRegistry,
                        Constants.SSO_DEVELOPER_USER,
                        Constants.SSO_USER_PASSWORD
                )
        );
        // Set authentication method to token for developer client
        developerClient.setAuthMethod(AuthMethod.TOKEN);

        // GET READONLY API CLIENT
        // Create readonly API client
        readonlyClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update readonly API client with it
        readonlyClient.setToken(
                KeycloakUtils.getAccessToken(apicurioRegistry, Constants.SSO_READONLY_USER, Constants.SSO_USER_PASSWORD)
        );
        // Set authentication method to token for readonly client
        readonlyClient.setAuthMethod(AuthMethod.TOKEN);
    }

    public static void testRoleBasedAuthorizationAdminOverride(ApicurioRegistry apicurioRegistry) {
        /* RUN PRE-TEST ACTIONS */

        // GET REGISTRY HOSTNAME
        // Wait for readiness of registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));
        // Get registry hostname
        String hostname = ApicurioRegistryUtils.getApicurioRegistryHostname(apicurioRegistry);

        // INITIALIZE API CLIENTS
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);

        // PREPARE NECESSARY VARIABLES
        // Get registry deployment
        deployment = Kubernetes.getDeployment(
                apicurioRegistry.getMetadata().getNamespace(),
                apicurioRegistry.getMetadata().getName() + "-deployment"
        );
        // Basic environment variable to enable/disable role based authorization
        EnvVar roleBasedAuth = new EnvVar() {{
            setName("ROLE_BASED_AUTHZ_ENABLED");
            setValue("true");
        }};
        // Environment variable to set authorization source to application
        EnvVar roleBasedAuthSource = new EnvVar() {{
            setName("ROLE_BASED_AUTHZ_SOURCE");
            setValue("application");
        }};
        // Environment variable to enable/disable admin override
        EnvVar roleBasedAuthAdminOverride = new EnvVar() {{
            setName("REGISTRY_AUTH_ADMIN_OVERRIDE_ENABLED");
            setValue("true");
        }};
        // Environment variable to set admin override information source
        EnvVar roleBasedAuthAdminOverrideSource = new EnvVar() {{
           setName("REGISTRY_AUTH_ADMIN_OVERRIDE_FROM");
           setValue("token");
        }};
        // Set the type of information used to determine if a user is an admin
        EnvVar roleBasedAuthAdminOverrideType = new EnvVar() {{
           setName("REGISTRY_AUTH_ADMIN_OVERRIDE_TYPE");
           setValue("role");
        }};
        // Set name of admin override role
        EnvVar roleBasedAuthAdminOverrideRole = new EnvVar() {{
            setName("REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE");
            setValue("sr-admin");
        }};

        // WAIT FOR API AVAILABILITY
        Assertions.assertTrue(adminClient.waitServiceAvailable());

        /* RUN TEST ACTIONS */

        // DO NOT SET ANY ROLE BASED RELATED ENVIRONMENT VARIABLE AND TEST DEFAULT BEHAVIOR
        // Run test actions
        testRoleBasedDisabled();

        // ENABLE ROLE BASED AUTHORIZATION BY TOKEN IN REGISTRY AND TEST IT
        // Set environment variable ROLE_BASED_AUTHZ_ENABLED of deployment to true
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuth);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabled();

        // SET ROLE BASED AUTHORIZATION SOURCE IN REGISTRY TO APPLICATION AND TEST IT
        // Set environment variable ROLE_BASED_AUTHZ_SOURCE of deployment to application
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthSource);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledAllForbidden();

        // DISABLE ADMIN OVERRIDE FEATURE IN REGISTRY AND TEST IT (SET TO DEFAULT VALUE)
        // Update environment variable value to false
        roleBasedAuthAdminOverride.setValue("false");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ENABLED of deployment to false
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthAdminOverride);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledAllForbidden();

        // REINITIALIZE API CLIENTS TO "REFRESH" TOKENS
        initializeClients(apicurioRegistry, hostname);

        // ENABLE ADMIN OVERRIDE FEATURE IN REGISTRY AND TEST IT
        // Update environment variable value to true
        roleBasedAuthAdminOverride.setValue("true");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ENABLED of deployment to true
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthAdminOverride);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE INFORMATION SOURCE IN REGISTRY TO TOKEN AND TEST IT (SET TO DEFAULT VALUE)
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_FROM of deployment to token
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthAdminOverrideSource);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE INFORMATION SOURCE IN REGISTRY TO ? AND TEST IT
        // NOTE: Nothing to set here, only token is currently supported.

        // SET ADMIN OVERRIDE INFORMATION TYPE IN REGISTRY TO ROLE AND TEST IT (SET TO DEFAULT VALUE)
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_TYPE of deployment to role
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthAdminOverrideType);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE ROLE NAME IN REGISTRY TO sr-admin AND TEST IT (SET TO DEFAULT VALUE)
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE of deployment to sr-admin
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthAdminOverrideRole);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE ROLE NAME IN REGISTRY TO test-admin-role AND TEST IT
        // Update environment variable value to test-admin-role
        roleBasedAuthAdminOverrideRole.setValue("test-admin-role");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE of deployment to test-admin-role
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthAdminOverrideRole);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions with default clients
        testRoleBasedEnabledAllForbidden();
        // Initialize clients with user-defined admin role
        initializeClients(apicurioRegistry, hostname, false);
        // Run test actions with user-defined admin role (other clients are still default)
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE ROLE NAME IN REGISTRY TO invalid-admin-role AND TEST IT
        // Update environment variable value to invalid-admin-role
        roleBasedAuthAdminOverrideRole.setValue("invalid-admin-role");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE of deployment to invalid-admin-role
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, roleBasedAuthAdminOverrideRole);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions with user-defined admin role (other clients are still default)
        testRoleBasedEnabledAllForbidden();
        // Initialize clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Run test actions with default clients
        testRoleBasedEnabledAllForbidden();

        // TODO: Add tests with claim
        // * SET ADMIN OVERRIDE INFORMATION TYPE IN REGISTRY TO CLAIM AND TEST IT
        // * SET ADMIN OVERRIDE CLAIM NAME IN REGISTRY TO org-admin AND TEST IT (SET TO DEFAULT VALUE)
        // * SET ADMIN OVERRIDE CLAIM NAME IN REGISTRY TO <user-defined-name> AND TEST IT
        // * SET ADMIN OVERRIDE CLAIM VALUE IN REGISTRY TO true AND TEST IT (SET TO DEFAULT VALUE)
        // * SET ADMIN OVERRIDE CLAIM VALUE IN REGISTRY TO <user-defined-value> AND TEST IT
    }
}
