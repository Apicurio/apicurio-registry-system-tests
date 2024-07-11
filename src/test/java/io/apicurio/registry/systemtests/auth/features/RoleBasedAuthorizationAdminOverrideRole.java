package io.apicurio.registry.systemtests.auth.features;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicur.registry.v1.apicurioregistryspec.configuration.Env;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import org.junit.jupiter.api.Assertions;

public class RoleBasedAuthorizationAdminOverrideRole extends RoleBasedAuthorizationAdminOverride {
    public static void testRoleBasedAuthorizationAdminOverrideRole(ApicurioRegistry apicurioRegistry) {
        /* RUN PRE-TEST ACTIONS */

        // PREPARE NECESSARY VARIABLES
        // Basic environment variable to enable/disable role based authorization
        Env roleBasedAuth = new Env() {{
            setName("ROLE_BASED_AUTHZ_ENABLED");
            setValue("true");
        }};
        // Environment variable to set authorization source to application
        Env roleBasedAuthSource = new Env() {{
            setName("ROLE_BASED_AUTHZ_SOURCE");
            setValue("application");
        }};
        // Environment variable to enable/disable admin override
        Env roleBasedAuthAdminOverride = new Env() {{
            setName("REGISTRY_AUTH_ADMIN_OVERRIDE_ENABLED");
            setValue("true");
        }};
        // Environment variable to set admin override information source
        Env roleBasedAuthAdminOverrideSource = new Env() {{
            setName("REGISTRY_AUTH_ADMIN_OVERRIDE_FROM");
            setValue("token");
        }};
        // Set the type of information used to determine if a user is an admin
        Env roleBasedAuthAdminOverrideType = new Env() {{
            setName("REGISTRY_AUTH_ADMIN_OVERRIDE_TYPE");
            setValue("role");
        }};
        // Set name of admin override role
        Env roleBasedAuthAdminOverrideRole = new Env() {{
            setName("REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE");
            setValue("sr-admin");
        }};

        // GET REGISTRY HOSTNAME
        // Wait for readiness of registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));
        // Get registry hostname
        String hostname = ApicurioRegistryUtils.getApicurioRegistryHostname(apicurioRegistry);

        /* RUN TEST ACTIONS */

        // DO NOT SET ANY ROLE BASED RELATED ENVIRONMENT VARIABLE AND TEST DEFAULT BEHAVIOR
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedDisabled();

        // ENABLE ROLE BASED AUTHORIZATION BY TOKEN IN REGISTRY AND TEST IT
        // Set environment variable ROLE_BASED_AUTHZ_ENABLED of deployment to true
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuth);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabled();

        // SET ROLE BASED AUTHORIZATION SOURCE IN REGISTRY TO APPLICATION AND TEST IT
        // Set environment variable ROLE_BASED_AUTHZ_SOURCE of deployment to application
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthSource);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledAllForbidden();

        // DISABLE ADMIN OVERRIDE FEATURE IN REGISTRY AND TEST IT (SET TO DEFAULT VALUE)
        // Update environment variable value to false
        roleBasedAuthAdminOverride.setValue("false");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ENABLED of deployment to false
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthAdminOverride);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledAllForbidden();

        // ENABLE ADMIN OVERRIDE FEATURE IN REGISTRY AND TEST IT
        // Update environment variable value to true
        roleBasedAuthAdminOverride.setValue("true");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ENABLED of deployment to true
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthAdminOverride);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE INFORMATION SOURCE IN REGISTRY TO TOKEN AND TEST IT (SET TO DEFAULT VALUE)
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_FROM of deployment to token
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthAdminOverrideSource);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE INFORMATION SOURCE IN REGISTRY TO ? AND TEST IT
        // NOTE: Nothing to set here, only token is currently supported.

        // SET ADMIN OVERRIDE INFORMATION TYPE IN REGISTRY TO ROLE AND TEST IT (SET TO DEFAULT VALUE)
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_TYPE of deployment to role
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthAdminOverrideType);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE ROLE NAME IN REGISTRY TO sr-admin AND TEST IT (SET TO DEFAULT VALUE)
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE of deployment to sr-admin
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthAdminOverrideRole);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE ROLE NAME IN REGISTRY TO test-admin-role AND TEST IT
        // Update environment variable value to test-admin-role
        roleBasedAuthAdminOverrideRole.setValue("test-admin-role");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE of deployment to test-admin-role
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthAdminOverrideRole);
        // Initialize API clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions with default clients
        testRoleBasedEnabledAllForbidden();
        // Initialize clients with user-defined admin role
        initializeClients(apicurioRegistry, hostname, "-role");
        // Run test actions with user-defined admin role (other clients are still default)
        testRoleBasedEnabledOnlyAdminAllowed();

        // SET ADMIN OVERRIDE ROLE NAME IN REGISTRY TO invalid-admin-role AND TEST IT
        // Update environment variable value to invalid-admin-role
        roleBasedAuthAdminOverrideRole.setValue("invalid-admin-role");
        // Set environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_ROLE of deployment to invalid-admin-role
        ApicurioRegistryUtils.createOrReplaceEnvVar(apicurioRegistry, roleBasedAuthAdminOverrideRole);
        // Initialize clients with user-defined admin role
        initializeClients(apicurioRegistry, hostname, "-role");
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions with user-defined admin role (other clients are still default)
        testRoleBasedEnabledAllForbidden();
        // Initialize clients with default roles
        initializeClients(apicurioRegistry, hostname);
        // Run test actions with default clients
        testRoleBasedEnabledAllForbidden();


    }
}
