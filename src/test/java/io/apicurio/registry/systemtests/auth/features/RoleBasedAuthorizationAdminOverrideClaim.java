package io.apicurio.registry.systemtests.auth.features;

import io.apicur.registry.v1.ApicurioRegistry3;
import io.apicur.registry.v1.apicurioregistry3spec.app.Env;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class RoleBasedAuthorizationAdminOverrideClaim extends RoleBasedAuthorizationAdminOverride {
    public static void testRoleBasedAuthorizationAdminOverrideClaim(
            ApicurioRegistry3 apicurioRegistry,
            String claim,
            String claimValue,
            String adminSuffix,
            boolean isAdminAllowed
    ) {
        /* RUN PRE-TEST ACTIONS */

        // PREPARE NECESSARY VARIABLES
        // Initialize environment variable list for test
        List<Env> envVarList = new ArrayList<>();
        // Add basic environment variable to enable role based authorization into list
        envVarList.add(new Env() {{
            setName("ROLE_BASED_AUTHZ_ENABLED");
            setValue("true");
        }});
        // Add environment variable to set authorization source to application into list
        envVarList.add(new Env() {{
            setName("ROLE_BASED_AUTHZ_SOURCE");
            setValue("application");
        }});
        // Add environment variable to enable admin override into list
        envVarList.add(new Env() {{
            setName("APICURIO_AUTH_ADMIN_OVERRIDE_ENABLED");
            setValue("true");
        }});
        //
        // Environment variable REGISTRY_AUTH_ADMIN_OVERRIDE_FROM to set admin override information source should be set
        // to token by default because only token is currently supported. We do not need to set it here.
        //
        // Add environment variable to set type of information used to determine if user is admin to claim into list
        envVarList.add(new Env() {{
            setName("APICURIO_AUTH_ADMIN_OVERRIDE_TYPE");
            setValue("claim");
        }});
        // If claim should not be default
        if (!claim.equals("default")) {
            // Add environment variable to set name of admin override claim into list
            envVarList.add(new Env() {{
                setName("APICURIO_AUTH_ADMIN_OVERRIDE_CLAIM");
                setValue(claim);
            }});
        }
        // If claim value should not be default
        if (!claimValue.equals("default")) {
            // Add environment variable to set value of admin override claim into list
            envVarList.add(new Env() {{
                setName("APICURIO_AUTH_ADMIN_OVERRIDE_CLAIM_VALUE");
                setValue(claimValue);
            }});
        }

        // GET REGISTRY HOSTNAME
        // Wait for readiness of registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));
        // Get registry hostname
        String hostname = ApicurioRegistryUtils.getApicurioRegistryHostname(apicurioRegistry);

        /* RUN TEST ACTIONS */

        // SET ENVIRONMENT FOR TEST, INITIALIZE API CLIENTS AND TEST EXPECTED RESULT
        // Set environment variables of deployment
        ApicurioRegistryUtils.createOrReplaceEnvVars(apicurioRegistry, envVarList);
        // Initialize API clients
        initializeClients(apicurioRegistry, hostname, adminSuffix);
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());
        // Run test actions
        if (isAdminAllowed) {
            // Test case when admin client should be allowed to do any action,
            // but other clients can do nothing
            testRoleBasedEnabledOnlyAdminAllowed();
        } else {
            // Test case when all clients (including admin) has forbidden access
            testRoleBasedEnabledAllForbidden();
        }
    }
}
