package io.apicurio.registry.systemtests.auth.features;

import io.apicurio.registry.operator.api.model.ApicurioRegistry;
import io.apicurio.registry.systemtests.client.ApicurioRegistryApiClient;
import io.apicurio.registry.systemtests.client.ArtifactType;
import io.apicurio.registry.systemtests.client.AuthMethod;
import io.apicurio.registry.systemtests.framework.ApicurioRegistryUtils;
import io.apicurio.registry.systemtests.framework.CompatibilityLevel;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.framework.DeploymentUtils;
import io.apicurio.registry.systemtests.framework.KeycloakUtils;
import io.apicurio.registry.systemtests.framework.RuleType;
import io.apicurio.registry.systemtests.framework.ValidityLevel;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class RoleBasedAuthorizationToken {
    public static void testRoleBasedAuthorizationToken(ApicurioRegistry apicurioRegistry) {
        /* RUN PRE-TEST ACTIONS */

        // GET REGISTRY HOSTNAME
        // Wait for readiness of registry hostname
        Assertions.assertTrue(ApicurioRegistryUtils.waitApicurioRegistryHostnameReady(apicurioRegistry));
        // Get registry hostname
        String hostname = ApicurioRegistryUtils.getApicurioRegistryHostname(apicurioRegistry);

        // GET ADMIN API CLIENT
        // Create admin API client
        ApicurioRegistryApiClient adminClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update admin API client with it
        adminClient.setToken(
                KeycloakUtils.getAccessToken(apicurioRegistry, Constants.SSO_ADMIN_USER, Constants.SSO_USER_PASSWORD)
        );
        // Set authentication method to token for admin client
        adminClient.setAuthMethod(AuthMethod.TOKEN);

        // GET DEVELOPER API CLIENT
        // Create developer API client
        ApicurioRegistryApiClient developerClient = new ApicurioRegistryApiClient(hostname);
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
        ApicurioRegistryApiClient readonlyClient = new ApicurioRegistryApiClient(hostname);
        // Get access token from Keycloak and update readonly API client with it
        readonlyClient.setToken(
                KeycloakUtils.getAccessToken(apicurioRegistry, Constants.SSO_READONLY_USER, Constants.SSO_USER_PASSWORD)
        );
        // Set authentication method to token for readonly client
        readonlyClient.setAuthMethod(AuthMethod.TOKEN);

        // WAIT FOR API AVAILABILITY
        Assertions.assertTrue(adminClient.waitServiceAvailable());

        // PREPARE NECESSARY VARIABLES
        // Get registry deployment
        Deployment deployment = Kubernetes.getDeployment(
                apicurioRegistry.getMetadata().getNamespace(),
                apicurioRegistry.getMetadata().getName() + "-deployment"
        );
        // Define artifact group ID for all users
        String groupId = "roleBasedAuthorizationTokenTest";
        // Define artifact ID prefix for all users
        String id = "role-based-authorization-token-test";
        // Define artifact ID for admin user
        String adminId = id + "-admin";
        // Define artifact ID for developer user
        String developerId = id + "-developer";
        // Define artifact ID for readonly user
        String readonlyId = id + "-readonly";
        // Define artifact ID suffix for second artifact of the same user
        String secondId = "-second";
        // Define artifact ID suffix for third artifact of the same user
        String thirdId = "-third";
        // Define artifact type for all artifacts
        ArtifactType type = ArtifactType.JSON;
        // Define artifact initial content for all artifacts
        String initialContent = "{}";
        // Define artifact updated content for all artifacts
        String updatedContent = "{\"key\":\"id\"}";
        // Define second artifact updated content for all artifacts
        String secondUpdatedContent = "{\"id\":\"key\"}";
        // Define third artifact updated content for all artifacts
        String thirdUpdatedContent = "{\"key\":\"value\"}";
        // Define variable for list of rules used in test
        List<String> ruleList;

        /* RUN TEST ACTIONS */

        // TEST DEFAULT VALUE OF ROLE BASED AUTHORIZATION BY TOKEN (false)
        // TODO: Check list of rules/artifacts when rule/artifact disabled/deleted
        // TODO: Check value of global rule after update
        // TODO: Check value of artifact rule after update
        // TODO: Check content of artifact when updated

        // --- GLOBAL VALIDITY RULE
        ValidityLevel validityLevel = ValidityLevel.SYNTAX_ONLY;

        // --- global validity rule by admin
        // Check that API returns 204 No Content when enabling global validity rule by admin
        Assertions.assertTrue(adminClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating global validity rule by admin
        Assertions.assertTrue(adminClient.updateGlobalValidityRule(validityLevel));
        // Check that API returns 200 OK when getting global validity rule by admin
        Assertions.assertNotNull(adminClient.getGlobalValidityRule());
        // Check that API returns 204 No Content when disabling global validity rule by admin
        Assertions.assertTrue(adminClient.disableGlobalValidityRule());

        // --- global validity rule by developer
        // Check that API returns 204 No Content when enabling global validity rule by developer
        Assertions.assertTrue(developerClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = developerClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating global validity rule by developer
        Assertions.assertTrue(developerClient.updateGlobalValidityRule(validityLevel));
        // Check that API returns 200 OK when getting global validity rule by developer
        Assertions.assertNotNull(developerClient.getGlobalValidityRule());
        // Check that API returns 204 No Content when disabling global validity rule by developer
        Assertions.assertTrue(developerClient.disableGlobalValidityRule());

        // --- global validity rule by readonly
        // Check that API returns 204 No Content when enabling global validity rule by developer
        Assertions.assertTrue(readonlyClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = readonlyClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating global validity rule by developer
        Assertions.assertTrue(readonlyClient.updateGlobalValidityRule(validityLevel));
        // Check that API returns 200 OK when getting global validity rule by developer
        Assertions.assertNotNull(readonlyClient.getGlobalValidityRule());
        // Check that API returns 204 No Content when disabling global validity rule by developer
        Assertions.assertTrue(readonlyClient.disableGlobalValidityRule());

        // --- GLOBAL COMPATIBILITY RULE
        CompatibilityLevel compatibilityLevel = CompatibilityLevel.BACKWARD_TRANSITIVE;

        // --- global compatibility rule by admin
        // Check that API returns 204 No Content when enabling global compatibility rule by admin
        Assertions.assertTrue(adminClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 200 OK when updating global compatibility rule by admin
        Assertions.assertTrue(adminClient.updateGlobalCompatibilityRule(compatibilityLevel));
        // Check that API returns 200 OK when getting global compatibility rule by admin
        Assertions.assertNotNull(adminClient.getGlobalCompatibilityRule());
        // Check that API returns 204 No Content when disabling global compatibility rule by admin
        Assertions.assertTrue(adminClient.disableGlobalCompatibilityRule());

        // --- global compatibility rule by developer
        // Check that API returns 204 No Content when enabling global compatibility rule by developer
        Assertions.assertTrue(developerClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = developerClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 200 OK when updating global compatibility rule by developer
        Assertions.assertTrue(developerClient.updateGlobalCompatibilityRule(compatibilityLevel));
        // Check that API returns 200 OK when getting global compatibility rule by developer
        Assertions.assertNotNull(developerClient.getGlobalCompatibilityRule());
        // Check that API returns 204 No Content when disabling global compatibility rule by developer
        Assertions.assertTrue(developerClient.disableGlobalCompatibilityRule());

        // --- global compatibility rule by readonly
        // Check that API returns 204 No Content when enabling global compatibility rule by developer
        Assertions.assertTrue(readonlyClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = readonlyClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 200 OK when updating global compatibility rule by developer
        Assertions.assertTrue(readonlyClient.updateGlobalCompatibilityRule(compatibilityLevel));
        // Check that API returns 200 OK when getting global compatibility rule by developer
        Assertions.assertNotNull(readonlyClient.getGlobalCompatibilityRule());
        // Check that API returns 204 No Content when disabling global compatibility rule by developer
        Assertions.assertTrue(readonlyClient.disableGlobalCompatibilityRule());

        // --- LIST ACTION
        // Check that API returns 200 OK when listing artifacts by admin
        Assertions.assertNotNull(adminClient.listArtifacts());
        // Check that API returns 200 OK when listing artifacts by developer
        Assertions.assertNotNull(developerClient.listArtifacts());
        // Check that API returns 200 OK when listing artifacts by readonly
        Assertions.assertNotNull(readonlyClient.listArtifacts());

        // --- CREATE ACTION
        // Check that API returns 200 OK when creating artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId));
        // Check that API returns 200 OK when creating artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId));
        // Check that API returns 200 OK when creating artifact by readonly
        Assertions.assertTrue(readonlyClient.createArtifact(groupId, readonlyId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(readonlyClient.listArtifacts().contains(groupId, readonlyId));
        // Check that API returns 200 OK when creating second artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId + secondId));
        // Check that API returns 200 OK when creating second artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId + secondId));
        // Check that API returns 200 OK when creating artifact by readonly
        Assertions.assertTrue(readonlyClient.createArtifact(groupId, readonlyId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(readonlyClient.listArtifacts().contains(groupId, readonlyId + secondId));
        // Check that API returns 200 OK when creating third artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId + thirdId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId + thirdId));
        // Check that API returns 200 OK when creating third artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId + thirdId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId + thirdId));
        // Check that API returns 200 OK when creating third artifact by readonly
        Assertions.assertTrue(readonlyClient.createArtifact(groupId, readonlyId + thirdId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(readonlyClient.listArtifacts().contains(groupId, readonlyId + thirdId));

        // --- ARTIFACT VALIDITY RULE
        validityLevel = ValidityLevel.NONE;

        // --- artifact validity rule on own artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on own artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, developerId));

        // --- artifact validity rule on own artifact by readonly
        // Check that API returns 204 No Content when enabling artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(readonlyClient.getArtifactValidityRule(groupId, readonlyId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactValidityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, readonlyId));

        // --- artifact validity rule on developer artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on developer by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, developerId));

        // --- artifact validity rule on readonly artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, readonlyId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on readonly by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, readonlyId));

        // --- artifact validity rule on admin artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on admin by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on readonly artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule on readonly by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, readonlyId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on readonly by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on readonly by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact validity rule on readonly by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, readonlyId));

        // --- artifact validity rule on admin artifact by readonly
        // Check that API returns 204 No Content when enabling artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(readonlyClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on admin by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on developer artifact by readonly
        // Check that API returns 204 No Content when enabling artifact validity rule on developer by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(readonlyClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on developer by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on developer by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule on developer by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, developerId));

        // --- ARTIFACT COMPATIBILITY RULE
        compatibilityLevel = CompatibilityLevel.FORWARD;

        // --- artifact compatibility rule on own artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on own artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by developer
        Assertions.assertTrue(
                developerClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel)
        );
        // Check that API returns 200 OK when getting artifact compatibility rule by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- artifact compatibility rule on own artifact by readonly
        // Check that API returns 204 No Content when enabling artifact compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactCompatibilityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                readonlyClient.getArtifactCompatibilityRule(groupId, readonlyId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactCompatibilityRule(groupId, readonlyId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactCompatibilityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactCompatibilityRule(groupId, readonlyId));

        // --- artifact compatibility rule on developer artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on developer by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- artifact compatibility rule on readonly artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule on readonly by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, readonlyId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on readonly by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, readonlyId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on readonly by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on readonly by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, readonlyId));

        // --- artifact compatibility rule on admin artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on admin by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on readonly artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule on readonly by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, readonlyId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on readonly by developer
        Assertions.assertTrue(developerClient.updateArtifactCompatibilityRule(groupId, readonlyId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on readonly by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on readonly by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, readonlyId));

        // --- artifact compatibility rule on admin artifact by readonly
        // Check that API returns 204 No Content when enabling artifact compatibility rule on admin by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                readonlyClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on admin by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on admin by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on admin by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on developer artifact by readonly
        // Check that API returns 204 No Content when enabling artifact compatibility rule on developer by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                readonlyClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on developer by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on developer by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on developer by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- LIST ACTION ON GROUP
        // Check that API returns 200 OK when listing group artifacts by admin
        Assertions.assertNotNull(adminClient.listGroupArtifacts(groupId));
        // Check that API returns 200 OK when listing group artifacts by developer
        Assertions.assertNotNull(developerClient.listGroupArtifacts(groupId));
        // Check that API returns 200 OK when listing group artifacts by readonly
        Assertions.assertNotNull(readonlyClient.listGroupArtifacts(groupId));

        // --- READ ACTION ON OWN ARTIFACT
        // Check that API returns 200 OK when reading artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, developerId));
        // Check that API returns 200 OK when reading artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, readonlyId));

        // --- READ ACTION ON OTHER'S ARTIFACT
        // Check that API returns 200 OK when reading developer artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, developerId));
        // Check that API returns 200 OK when reading readonly artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, readonlyId));
        // Check that API returns 200 OK when reading admin artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading readonly artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, readonlyId));
        // Check that API returns 200 OK when reading admin artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading developer artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, developerId));

        // --- UPDATE ACTION ON OWN ARTIFACT
        // Check that API returns 200 OK when updating admin artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, adminId, updatedContent));
        // Check that API returns 200 OK when updating developer artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, developerId, updatedContent));
        // Check that API returns 200 OK when updating readonly artifact by readonly
        Assertions.assertTrue(readonlyClient.updateArtifact(groupId, readonlyId, updatedContent));

        // --- UPDATE ACTION ON OTHER'S ARTIFACT
        // Check that API returns 200 OK when updating developer artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, developerId, secondUpdatedContent));
        // Check that API returns 200 OK when updating readonly artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, readonlyId, secondUpdatedContent));
        // Check that API returns 200 OK when updating admin artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, adminId, secondUpdatedContent));
        // Check that API returns 200 OK when updating readonly artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, readonlyId, thirdUpdatedContent));
        // Check that API returns 200 OK when updating admin artifact by readonly
        Assertions.assertTrue(readonlyClient.updateArtifact(groupId, adminId, thirdUpdatedContent));
        // Check that API returns 200 OK when updating developer artifact by readonly
        Assertions.assertTrue(readonlyClient.updateArtifact(groupId, developerId, thirdUpdatedContent));

        // --- DELETE ACTION ON OWN ARTIFACT
        // Check that API returns 204 No Content when deleting artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, adminId));
        // Check that API returns 204 No Content when deleting artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, developerId));
        // Check that API returns 204 No Content when deleting artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, readonlyId));

        // --- DELETE ACTION ON OTHER'S ARTIFACT
        // Check that API returns 204 No Content when deleting developer artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, developerId + secondId));
        // Check that API returns 204 No Content when deleting readonly artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, readonlyId + secondId));
        // Check that API returns 204 No Content when deleting admin artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, adminId + secondId));
        // Check that API returns 204 No Content when deleting readonly artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, readonlyId + thirdId));
        // Check that API returns 204 No Content when deleting admin artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, adminId + thirdId));
        // Check that API returns 204 No Content when deleting developer artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, developerId + thirdId));


        // ENABLE ROLE BASED AUTHORIZATION BY TOKEN IN REGISTRY AND TEST IT
        // TODO: Check list of rules/artifacts when rule/artifact disabled/deleted
        // TODO: Check value of global rule after update
        // TODO: Check value of artifact rule after update
        // TODO: Check content of artifact when updated
        // Set environment variable ROLE_BASED_AUTHZ_ENABLED of deployment to true
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, new EnvVar() {{
            setName("ROLE_BASED_AUTHZ_ENABLED");
            setValue("true");
        }});
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());

        // --- GLOBAL VALIDITY RULE
        validityLevel = ValidityLevel.SYNTAX_ONLY;

        // --- global validity rule by admin
        // Check that API returns 204 No Content when enabling global validity rule by admin
        Assertions.assertTrue(adminClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating global validity rule by admin
        Assertions.assertTrue(adminClient.updateGlobalValidityRule(validityLevel));
        // Check that API returns 200 OK when getting global validity rule by admin
        Assertions.assertNotNull(adminClient.getGlobalValidityRule());
        // Check that API returns 204 No Content when disabling global validity rule by admin
        Assertions.assertTrue(adminClient.disableGlobalValidityRule());

        // --- global validity rule by developer
        // Check that API returns 403 Forbidden when enabling global validity rule by developer
        Assertions.assertTrue(developerClient.enableGlobalValidityRule(HttpStatus.SC_FORBIDDEN));
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is NOT present in list of global rules
        Assertions.assertFalse(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getGlobalValidityRule(HttpStatus.SC_NOT_FOUND));
        // SETUP: Enable global validity rule for test of developer permission
        Assertions.assertTrue(adminClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 403 Forbidden when listing global rules by developer
        Assertions.assertTrue(developerClient.listGlobalRules(HttpStatus.SC_FORBIDDEN).isEmpty());
        // Check that API returns 403 Forbidden when updating global validity rule by developer
        Assertions.assertTrue(developerClient.updateGlobalValidityRule(validityLevel, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when getting global validity rule by developer
        Assertions.assertNull(developerClient.getGlobalValidityRule(HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when disabling global validity rule by developer
        Assertions.assertTrue(developerClient.disableGlobalValidityRule(HttpStatus.SC_FORBIDDEN));
        // TEARDOWN: Disable global validity rule after test of developer permission
        Assertions.assertTrue(adminClient.disableGlobalValidityRule());

        // --- global validity rule by readonly
        // Check that API returns 403 Forbidden when enabling global validity rule by readonly
        Assertions.assertTrue(readonlyClient.enableGlobalValidityRule(HttpStatus.SC_FORBIDDEN));
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is NOT present in list of global rules
        Assertions.assertFalse(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getGlobalValidityRule(HttpStatus.SC_NOT_FOUND));
        // SETUP: Enable global validity rule for test of readonly permission
        Assertions.assertTrue(adminClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 403 Forbidden when listing global rules by readonly
        Assertions.assertTrue(readonlyClient.listGlobalRules(HttpStatus.SC_FORBIDDEN).isEmpty());
        // Check that API returns 403 Forbidden when updating global validity rule by readonly
        Assertions.assertTrue(readonlyClient.updateGlobalValidityRule(validityLevel, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when getting global validity rule by readonly
        Assertions.assertNull(readonlyClient.getGlobalValidityRule(HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when disabling global validity rule by readonly
        Assertions.assertTrue(readonlyClient.disableGlobalValidityRule(HttpStatus.SC_FORBIDDEN));
        // TEARDOWN: Disable global validity rule after test of readonly permission
        Assertions.assertTrue(adminClient.disableGlobalValidityRule());

        // --- GLOBAL COMPATIBILITY RULE
        compatibilityLevel = CompatibilityLevel.FORWARD_TRANSITIVE;

        // --- global compatibility rule by admin
        // Check that API returns 204 No Content when enabling global compatibility rule by admin
        Assertions.assertTrue(adminClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 200 OK when updating global compatibility rule by admin
        Assertions.assertTrue(adminClient.updateGlobalCompatibilityRule(compatibilityLevel));
        // Check that API returns 200 OK when getting global compatibility rule by admin
        Assertions.assertNotNull(adminClient.getGlobalCompatibilityRule());
        // Check that API returns 204 No Content when disabling global compatibility rule by admin
        Assertions.assertTrue(adminClient.disableGlobalCompatibilityRule());

        // --- global compatibility rule by developer
        // Check that API returns 403 Forbidden when enabling global compatibility rule by developer
        Assertions.assertTrue(developerClient.enableGlobalCompatibilityRule(HttpStatus.SC_FORBIDDEN));
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is NOT present in list of global rules
        Assertions.assertFalse(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getGlobalCompatibilityRule(HttpStatus.SC_NOT_FOUND));
        // SETUP: Enable global compatibility rule for test of developer permission
        Assertions.assertTrue(adminClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 403 Forbidden when listing global rules by developer
        Assertions.assertTrue(developerClient.listGlobalRules(HttpStatus.SC_FORBIDDEN).isEmpty());
        // Check that API returns 403 Forbidden when updating global compatibility rule by developer
        Assertions.assertTrue(
                developerClient.updateGlobalCompatibilityRule(compatibilityLevel, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 403 Forbidden when getting global compatibility rule by developer
        Assertions.assertNull(developerClient.getGlobalCompatibilityRule(HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when disabling global compatibility rule by developer
        Assertions.assertTrue(developerClient.disableGlobalCompatibilityRule(HttpStatus.SC_FORBIDDEN));
        // TEARDOWN: Disable global compatibility rule after test of developer permission
        Assertions.assertTrue(adminClient.disableGlobalCompatibilityRule());

        // --- global compatibility rule by readonly
        // Check that API returns 403 Forbidden when enabling global compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.enableGlobalCompatibilityRule(HttpStatus.SC_FORBIDDEN));
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is NOT present in list of global rules
        Assertions.assertFalse(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getGlobalCompatibilityRule(HttpStatus.SC_NOT_FOUND));
        // SETUP: Enable global compatibility rule for test of readonly permission
        Assertions.assertTrue(adminClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 403 Forbidden when listing global rules by readonly
        Assertions.assertTrue(readonlyClient.listGlobalRules(HttpStatus.SC_FORBIDDEN).isEmpty());
        // Check that API returns 403 Forbidden when updating global compatibility rule by readonly
        Assertions.assertTrue(
                readonlyClient.updateGlobalCompatibilityRule(compatibilityLevel, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 403 Forbidden when getting global compatibility rule by readonly
        Assertions.assertNull(readonlyClient.getGlobalCompatibilityRule(HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when disabling global compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.disableGlobalCompatibilityRule(HttpStatus.SC_FORBIDDEN));
        // TEARDOWN: Disable global compatibility rule after test of readonly permission
        Assertions.assertTrue(adminClient.disableGlobalCompatibilityRule());

        // --- LIST ACTION
        // Check that API returns 200 OK when listing artifacts by admin
        Assertions.assertNotNull(adminClient.listArtifacts());
        // Check that API returns 200 OK when listing artifacts by developer
        Assertions.assertNotNull(developerClient.listArtifacts());
        // Check that API returns 200 OK when listing artifacts by readonly
        Assertions.assertNotNull(readonlyClient.listArtifacts());

        // --- CREATE ACTION
        // Check that API returns 200 OK when creating artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId));
        // Check that API returns 200 OK when creating artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId));
        // Check that API returns 200 OK when creating second artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId + secondId));
        // Check that API returns 200 OK when creating second artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId + secondId));
        // Check that API returns 403 Forbidden when creating artifact by readonly
        Assertions.assertTrue(
                readonlyClient.createArtifact(groupId, readonlyId, type, initialContent, HttpStatus.SC_FORBIDDEN)
        );
        // Check non-creation of artifact
        Assertions.assertFalse(readonlyClient.listArtifacts().contains(groupId, readonlyId));

        // --- ARTIFACT VALIDITY RULE
        validityLevel = ValidityLevel.NONE;

        // --- artifact validity rule on own artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on own artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, developerId));

        // --- artifact validity rule on own artifact by readonly
        // Check that API returns 403 Forbidden when enabling artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 404 Not Found when listing artifact rules by readonly
        Assertions.assertTrue(readonlyClient.listArtifactRules(groupId, readonlyId, HttpStatus.SC_NOT_FOUND).isEmpty());
        // Check value of enabled rule
        Assertions.assertNull(readonlyClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when updating artifact validity rule by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 404 Not Found when getting artifact validity rule by readonly
        Assertions.assertNull(readonlyClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when disabling artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_FORBIDDEN));

        // --- artifact validity rule on developer artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on developer by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, developerId));

        // --- artifact validity rule on readonly artifact by admin
        // Check that API returns 404 Not Found when enabling artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when listing artifact rules on readonly by admin
        Assertions.assertTrue(adminClient.listArtifactRules(groupId, readonlyId, HttpStatus.SC_NOT_FOUND).isEmpty());
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when updating artifact validity rule on readonly by admin
        Assertions.assertTrue(
                adminClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 404 Not Found when getting artifact validity rule on readonly by admin
        Assertions.assertNull(adminClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when disabling artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));

        // --- artifact validity rule on admin artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on admin by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on readonly artifact by developer
        // Check that API returns 404 Not Found when enabling artifact validity rule on readonly by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when listing artifact rules on readonly by developer
        Assertions.assertTrue(
                developerClient.listArtifactRules(groupId, readonlyId, HttpStatus.SC_NOT_FOUND).isEmpty()
        );
        // Check value of enabled rule
        Assertions.assertNull(developerClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when updating artifact validity rule on readonly by developer
        Assertions.assertTrue(
                developerClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 404 Not Found when getting artifact validity rule on readonly by developer
        Assertions.assertNull(developerClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when disabling artifact validity rule on readonly by developer
        Assertions.assertTrue(
                developerClient.disableArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND)
        );

        // --- artifact validity rule on admin artifact by readonly
        // Check that API returns 403 Forbidden when enabling artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, adminId, HttpStatus.SC_FORBIDDEN));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is NOT present in list of artifact rules
        Assertions.assertFalse(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(readonlyClient.getArtifactValidityRule(groupId, adminId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when updating artifact validity rule on admin by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifactValidityRule(groupId, adminId, validityLevel, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 404 Not Found when getting artifact validity rule on admin by readonly
        Assertions.assertNull(readonlyClient.getArtifactValidityRule(groupId, adminId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when disabling artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, adminId, HttpStatus.SC_FORBIDDEN));

        // --- artifact validity rule on developer artifact by readonly
        // Check that API returns 403 Forbidden when enabling artifact validity rule on developer by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, developerId, HttpStatus.SC_FORBIDDEN));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is NOT present in list of artifact rules
        Assertions.assertFalse(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(readonlyClient.getArtifactValidityRule(groupId, developerId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when updating artifact validity rule on developer by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifactValidityRule(groupId, developerId, validityLevel, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 404 Not Found when getting artifact validity rule on developer by readonly
        Assertions.assertNull(readonlyClient.getArtifactValidityRule(groupId, developerId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when disabling artifact validity rule on developer by readonly
        Assertions.assertTrue(
                readonlyClient.disableArtifactValidityRule(groupId, developerId, HttpStatus.SC_FORBIDDEN)
        );

        // --- ARTIFACT COMPATIBILITY RULE
        compatibilityLevel = CompatibilityLevel.FORWARD;

        // --- artifact compatibility rule on own artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on own artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by developer
        Assertions.assertTrue(
                developerClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel)
        );
        // Check that API returns 200 OK when getting artifact compatibility rule by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- artifact compatibility rule on own artifact by readonly
        // Check that API returns 403 Forbidden when enabling artifact compatibility rule by readonly
        Assertions.assertTrue(
                readonlyClient.enableArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 404 Not Found when listing artifact rules by readonly
        Assertions.assertTrue(readonlyClient.listArtifactRules(groupId, readonlyId, HttpStatus.SC_NOT_FOUND).isEmpty());
        // Check value of enabled rule
        Assertions.assertNull(
                developerClient.getArtifactCompatibilityRule(groupId, developerId, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 403 Forbidden when updating artifact compatibility rule by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifactCompatibilityRule(
                        groupId,
                        readonlyId,
                        compatibilityLevel,
                        HttpStatus.SC_FORBIDDEN
                )
        );
        // Check that API returns 404 Not Found when getting artifact compatibility rule by readonly
        Assertions.assertNull(
                readonlyClient.getArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 403 Forbidden when disabling artifact compatibility rule by readonly
        Assertions.assertTrue(
                readonlyClient.disableArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_FORBIDDEN)
        );

        // --- artifact compatibility rule on developer artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on developer by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- artifact compatibility rule on readonly artifact by admin
        // Check that API returns 404 Not Found when enabling artifact compatibility rule on readonly by admin
        Assertions.assertTrue(
                adminClient.enableArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 404 Not Found when listing artifact rules on readonly by admin
        Assertions.assertTrue(adminClient.listArtifactRules(groupId, readonlyId, HttpStatus.SC_NOT_FOUND).isEmpty());
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when updating artifact compatibility rule on readonly by admin
        Assertions.assertTrue(
                adminClient.updateArtifactCompatibilityRule(
                        groupId,
                        readonlyId,
                        compatibilityLevel,
                        HttpStatus.SC_NOT_FOUND
                )
        );
        // Check that API returns 404 Not Found when getting artifact compatibility rule on readonly by admin
        Assertions.assertNull(adminClient.getArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when disabling artifact compatibility rule on readonly by admin
        Assertions.assertTrue(
                adminClient.disableArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND)
        );

        // --- artifact compatibility rule on admin artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on admin by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on readonly artifact by developer
        // Check that API returns 404 Not Found when enabling artifact compatibility rule on readonly by developer
        Assertions.assertTrue(
                developerClient.enableArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 404 Not Found when listing artifact rules on readonly by developer
        Assertions.assertTrue(
                developerClient.listArtifactRules(groupId, readonlyId, HttpStatus.SC_NOT_FOUND).isEmpty()
        );
        // Check value of enabled rule
        Assertions.assertNull(developerClient.getArtifactValidityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 404 Not Found when updating artifact compatibility rule on readonly by developer
        Assertions.assertTrue(
                developerClient.updateArtifactCompatibilityRule(
                        groupId,
                        readonlyId,
                        compatibilityLevel,
                        HttpStatus.SC_NOT_FOUND
                )
        );
        // Check that API returns 404 Not Found when getting artifact compatibility rule on readonly by developer
        Assertions.assertNull(
                developerClient.getArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 404 Not Found when disabling artifact compatibility rule on readonly by developer
        Assertions.assertTrue(
                developerClient.disableArtifactCompatibilityRule(groupId, readonlyId, HttpStatus.SC_NOT_FOUND)
        );

        // --- artifact compatibility rule on admin artifact by readonly
        // Check that API returns 403 Forbidden when enabling artifact compatibility rule on admin by readonly
        Assertions.assertTrue(
                readonlyClient.enableArtifactCompatibilityRule(groupId, adminId, HttpStatus.SC_FORBIDDEN)
        );
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is NOT present in list of artifact rules
        Assertions.assertFalse(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getArtifactCompatibilityRule(groupId, adminId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when updating artifact compatibility rule on admin by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifactCompatibilityRule(
                        groupId,
                        adminId,
                        compatibilityLevel,
                        HttpStatus.SC_FORBIDDEN
                )
        );
        // Check that API returns 404 Not Found when getting artifact compatibility rule on admin by readonly
        Assertions.assertNull(readonlyClient.getArtifactCompatibilityRule(groupId, adminId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when disabling artifact compatibility rule on admin by readonly
        Assertions.assertTrue(
                readonlyClient.disableArtifactCompatibilityRule(groupId, adminId, HttpStatus.SC_FORBIDDEN)
        );

        // --- artifact compatibility rule on developer artifact by readonly
        // Check that API returns 403 Forbidden when enabling artifact compatibility rule on developer by readonly
        Assertions.assertTrue(
                readonlyClient.enableArtifactCompatibilityRule(groupId, developerId, HttpStatus.SC_FORBIDDEN)
        );
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is NOT present in list of artifact rules
        Assertions.assertFalse(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertNull(adminClient.getArtifactCompatibilityRule(groupId, adminId, HttpStatus.SC_NOT_FOUND));
        // Check that API returns 403 Forbidden when updating artifact compatibility rule on developer by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifactCompatibilityRule(
                        groupId,
                        developerId,
                        compatibilityLevel,
                        HttpStatus.SC_FORBIDDEN
                )
        );
        // Check that API returns 404 Not Found when getting artifact compatibility rule on developer by readonly
        Assertions.assertNull(
                readonlyClient.getArtifactCompatibilityRule(groupId, developerId, HttpStatus.SC_NOT_FOUND)
        );
        // Check that API returns 403 Forbidden when disabling artifact compatibility rule on developer by readonly
        Assertions.assertTrue(
                readonlyClient.disableArtifactCompatibilityRule(groupId, developerId, HttpStatus.SC_FORBIDDEN)
        );

        // --- LIST ACTION ON GROUP
        // Check that API returns 200 OK when listing group artifacts by admin
        Assertions.assertNotNull(adminClient.listGroupArtifacts(groupId));
        // Check that API returns 200 OK when listing group artifacts by developer
        Assertions.assertNotNull(developerClient.listGroupArtifacts(groupId));
        // Check that API returns 200 OK when listing group artifacts by readonly
        Assertions.assertNotNull(readonlyClient.listGroupArtifacts(groupId));

        // --- READ ACTION ON OWN ARTIFACT
        // Check that API returns 200 OK when reading artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, developerId));
        // Check that API returns 404 Not Found when reading artifact by readonly
        Assertions.assertNull(readonlyClient.readArtifactContent(groupId, readonlyId, HttpStatus.SC_NOT_FOUND));

        // --- READ ACTION ON OTHER'S ARTIFACT
        // Check that API returns 200 OK when reading developer artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, developerId));
        // Check that API returns 200 OK when reading admin artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading admin artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading developer artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, developerId));

        // --- UPDATE ACTION ON OWN ARTIFACT
        // Check that API returns 200 OK when updating admin artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, adminId, updatedContent));
        // Check that API returns 200 OK when updating developer artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, developerId, updatedContent));
        // Check that API returns 403 Forbidden when updating readonly artifact by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifact(groupId, readonlyId, updatedContent, HttpStatus.SC_FORBIDDEN)
        );

        // --- UPDATE ACTION ON OTHER'S ARTIFACT
        // Check that API returns 200 OK when updating developer artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, developerId, secondUpdatedContent));
        // Check that API returns 200 OK when updating admin artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, adminId, secondUpdatedContent));
        // Check that API returns 403 Forbidden when updating admin artifact by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifact(groupId, adminId, initialContent, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 403 Forbidden when updating developer artifact by readonly
        Assertions.assertTrue(
                readonlyClient.updateArtifact(groupId, developerId, initialContent, HttpStatus.SC_FORBIDDEN)
        );

        // --- DELETE ACTION ON OWN ARTIFACT
        // Check that API returns 204 No Content when deleting artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, adminId));
        // Check that API returns 204 No Content when deleting artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, developerId));
        // Check that API returns 403 Forbidden when deleting readonly artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, readonlyId, HttpStatus.SC_FORBIDDEN));

        // --- DELETE ACTION ON OTHER'S ARTIFACT
        // Check that API returns 403 Forbidden when deleting second admin artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, adminId + secondId, HttpStatus.SC_FORBIDDEN));
        // Check that API returns 403 Forbidden when deleting second developer artifact by readonly
        Assertions.assertTrue(
                readonlyClient.deleteArtifact(groupId, developerId + secondId, HttpStatus.SC_FORBIDDEN)
        );
        // Check that API returns 204 No Content when deleting second developer artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, developerId + secondId));
        // Check that API returns 204 No Content when deleting second admin artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, adminId + secondId));


        // DISABLE ROLE BASED AUTHORIZATION BY TOKEN IN REGISTRY AND TEST IT
        // TODO: Check list of rules/artifacts when rule/artifact disabled/deleted
        // TODO: Check value of global rule after update
        // TODO: Check value of artifact rule after update
        // TODO: Check content of artifact when updated
        // Set environment variable ROLE_BASED_AUTHZ_ENABLED of deployment to false
        DeploymentUtils.createOrReplaceDeploymentEnvVar(deployment, new EnvVar() {{
            setName("ROLE_BASED_AUTHZ_ENABLED");
            setValue("false");
        }});
        // Wait for API availability
        Assertions.assertTrue(adminClient.waitServiceAvailable());

        // --- GLOBAL VALIDITY RULE
        validityLevel = ValidityLevel.SYNTAX_ONLY;

        // --- global validity rule by admin
        // Check that API returns 204 No Content when enabling global validity rule by admin
        Assertions.assertTrue(adminClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating global validity rule by admin
        Assertions.assertTrue(adminClient.updateGlobalValidityRule(validityLevel));
        // Check that API returns 200 OK when getting global validity rule by admin
        Assertions.assertNotNull(adminClient.getGlobalValidityRule());
        // Check that API returns 204 No Content when disabling global validity rule by admin
        Assertions.assertTrue(adminClient.disableGlobalValidityRule());

        // --- global validity rule by developer
        // Check that API returns 204 No Content when enabling global validity rule by developer
        Assertions.assertTrue(developerClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = developerClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating global validity rule by developer
        Assertions.assertTrue(developerClient.updateGlobalValidityRule(validityLevel));
        // Check that API returns 200 OK when getting global validity rule by developer
        Assertions.assertNotNull(developerClient.getGlobalValidityRule());
        // Check that API returns 204 No Content when disabling global validity rule by developer
        Assertions.assertTrue(developerClient.disableGlobalValidityRule());

        // --- global validity rule by readonly
        // Check that API returns 204 No Content when enabling global validity rule by developer
        Assertions.assertTrue(readonlyClient.enableGlobalValidityRule());
        // Get list of global rules
        ruleList = readonlyClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalValidityRule(), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating global validity rule by developer
        Assertions.assertTrue(readonlyClient.updateGlobalValidityRule(validityLevel));
        // Check that API returns 200 OK when getting global validity rule by developer
        Assertions.assertNotNull(readonlyClient.getGlobalValidityRule());
        // Check that API returns 204 No Content when disabling global validity rule by developer
        Assertions.assertTrue(readonlyClient.disableGlobalValidityRule());

        // --- GLOBAL COMPATIBILITY RULE
        compatibilityLevel = CompatibilityLevel.BACKWARD_TRANSITIVE;

        // --- global compatibility rule by admin
        // Check that API returns 204 No Content when enabling global compatibility rule by admin
        Assertions.assertTrue(adminClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = adminClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 200 OK when updating global compatibility rule by admin
        Assertions.assertTrue(adminClient.updateGlobalCompatibilityRule(compatibilityLevel));
        // Check that API returns 200 OK when getting global compatibility rule by admin
        Assertions.assertNotNull(adminClient.getGlobalCompatibilityRule());
        // Check that API returns 204 No Content when disabling global compatibility rule by admin
        Assertions.assertTrue(adminClient.disableGlobalCompatibilityRule());

        // --- global compatibility rule by developer
        // Check that API returns 204 No Content when enabling global compatibility rule by developer
        Assertions.assertTrue(developerClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = developerClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 200 OK when updating global compatibility rule by developer
        Assertions.assertTrue(developerClient.updateGlobalCompatibilityRule(compatibilityLevel));
        // Check that API returns 200 OK when getting global compatibility rule by developer
        Assertions.assertNotNull(developerClient.getGlobalCompatibilityRule());
        // Check that API returns 204 No Content when disabling global compatibility rule by developer
        Assertions.assertTrue(developerClient.disableGlobalCompatibilityRule());

        // --- global compatibility rule by readonly
        // Check that API returns 204 No Content when enabling global compatibility rule by developer
        Assertions.assertTrue(readonlyClient.enableGlobalCompatibilityRule());
        // Get list of global rules
        ruleList = readonlyClient.listGlobalRules();
        // Check that API returns 200 OK when listing global rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of global rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getGlobalCompatibilityRule(), CompatibilityLevel.BACKWARD);
        // Check that API returns 200 OK when updating global compatibility rule by developer
        Assertions.assertTrue(readonlyClient.updateGlobalCompatibilityRule(compatibilityLevel));
        // Check that API returns 200 OK when getting global compatibility rule by developer
        Assertions.assertNotNull(readonlyClient.getGlobalCompatibilityRule());
        // Check that API returns 204 No Content when disabling global compatibility rule by developer
        Assertions.assertTrue(readonlyClient.disableGlobalCompatibilityRule());

        // --- LIST ACTION
        // Check that API returns 200 OK when listing artifacts by admin
        Assertions.assertNotNull(adminClient.listArtifacts());
        // Check that API returns 200 OK when listing artifacts by developer
        Assertions.assertNotNull(developerClient.listArtifacts());
        // Check that API returns 200 OK when listing artifacts by readonly
        Assertions.assertNotNull(readonlyClient.listArtifacts());

        // --- CREATE ACTION
        // Check that API returns 200 OK when creating artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId));
        // Check that API returns 200 OK when creating artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId));
        // Check that API returns 200 OK when creating artifact by readonly
        Assertions.assertTrue(readonlyClient.createArtifact(groupId, readonlyId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(readonlyClient.listArtifacts().contains(groupId, readonlyId));
        // Check that API returns 200 OK when creating second artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId + secondId));
        // Check that API returns 200 OK when creating second artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId + secondId));
        // Check that API returns 200 OK when creating artifact by readonly
        Assertions.assertTrue(readonlyClient.createArtifact(groupId, readonlyId + secondId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(readonlyClient.listArtifacts().contains(groupId, readonlyId + secondId));
        // Check that API returns 200 OK when creating third artifact by admin
        Assertions.assertTrue(adminClient.createArtifact(groupId, adminId + thirdId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(adminClient.listArtifacts().contains(groupId, adminId + thirdId));
        // Check that API returns 200 OK when creating third artifact by developer
        Assertions.assertTrue(developerClient.createArtifact(groupId, developerId + thirdId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(developerClient.listArtifacts().contains(groupId, developerId + thirdId));
        // Check that API returns 200 OK when creating third artifact by readonly
        Assertions.assertTrue(readonlyClient.createArtifact(groupId, readonlyId + thirdId, type, initialContent));
        // Check creation of artifact
        Assertions.assertTrue(readonlyClient.listArtifacts().contains(groupId, readonlyId + thirdId));

        // --- ARTIFACT VALIDITY RULE
        validityLevel = ValidityLevel.NONE;

        // --- artifact validity rule on own artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on own artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, developerId));

        // --- artifact validity rule on own artifact by readonly
        // Check that API returns 204 No Content when enabling artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(readonlyClient.getArtifactValidityRule(groupId, readonlyId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactValidityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact validity rule by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, readonlyId));

        // --- artifact validity rule on developer artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on developer by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule on developer by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, developerId));

        // --- artifact validity rule on readonly artifact by admin
        // Check that API returns 204 No Content when enabling artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.enableArtifactValidityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by admin
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(adminClient.getArtifactValidityRule(groupId, readonlyId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on readonly by admin
        Assertions.assertNotNull(adminClient.getArtifactValidityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact validity rule on readonly by admin
        Assertions.assertTrue(adminClient.disableArtifactValidityRule(groupId, readonlyId));

        // --- artifact validity rule on admin artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on admin by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule on admin by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on readonly artifact by developer
        // Check that API returns 204 No Content when enabling artifact validity rule on readonly by developer
        Assertions.assertTrue(developerClient.enableArtifactValidityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by developer
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(developerClient.getArtifactValidityRule(groupId, readonlyId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on readonly by developer
        Assertions.assertTrue(developerClient.updateArtifactValidityRule(groupId, readonlyId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on readonly by developer
        Assertions.assertNotNull(developerClient.getArtifactValidityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact validity rule on readonly by developer
        Assertions.assertTrue(developerClient.disableArtifactValidityRule(groupId, readonlyId));

        // --- artifact validity rule on admin artifact by readonly
        // Check that API returns 204 No Content when enabling artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(readonlyClient.getArtifactValidityRule(groupId, adminId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactValidityRule(groupId, adminId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on admin by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactValidityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact validity rule on admin by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, adminId));

        // --- artifact validity rule on developer artifact by readonly
        // Check that API returns 204 No Content when enabling artifact validity rule on developer by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactValidityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by readonly
        Assertions.assertNotNull(ruleList);
        // Check that validity rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.VALIDITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(readonlyClient.getArtifactValidityRule(groupId, developerId), ValidityLevel.FULL);
        // Check that API returns 200 OK when updating artifact validity rule on developer by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactValidityRule(groupId, developerId, validityLevel));
        // Check that API returns 200 OK when getting artifact validity rule on developer by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactValidityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact validity rule on developer by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactValidityRule(groupId, developerId));

        // --- ARTIFACT COMPATIBILITY RULE
        compatibilityLevel = CompatibilityLevel.FORWARD;

        // --- artifact compatibility rule on own artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on own artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by developer
        Assertions.assertTrue(
                developerClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel)
        );
        // Check that API returns 200 OK when getting artifact compatibility rule by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- artifact compatibility rule on own artifact by readonly
        // Check that API returns 204 No Content when enabling artifact compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactCompatibilityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                readonlyClient.getArtifactCompatibilityRule(groupId, readonlyId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactCompatibilityRule(groupId, readonlyId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactCompatibilityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactCompatibilityRule(groupId, readonlyId));

        // --- artifact compatibility rule on developer artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on developer by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on developer by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- artifact compatibility rule on readonly artifact by admin
        // Check that API returns 204 No Content when enabling artifact compatibility rule on readonly by admin
        Assertions.assertTrue(adminClient.enableArtifactCompatibilityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = adminClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by admin
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                adminClient.getArtifactCompatibilityRule(groupId, readonlyId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on readonly by admin
        Assertions.assertTrue(adminClient.updateArtifactCompatibilityRule(groupId, readonlyId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on readonly by admin
        Assertions.assertNotNull(adminClient.getArtifactCompatibilityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on readonly by admin
        Assertions.assertTrue(adminClient.disableArtifactCompatibilityRule(groupId, readonlyId));

        // --- artifact compatibility rule on admin artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on admin by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on admin by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on readonly artifact by developer
        // Check that API returns 204 No Content when enabling artifact compatibility rule on readonly by developer
        Assertions.assertTrue(developerClient.enableArtifactCompatibilityRule(groupId, readonlyId));
        // Get list of artifact rules
        ruleList = developerClient.listArtifactRules(groupId, readonlyId);
        // Check that API returns 200 OK when listing artifact rules on readonly by developer
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                developerClient.getArtifactCompatibilityRule(groupId, readonlyId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on readonly by developer
        Assertions.assertTrue(developerClient.updateArtifactCompatibilityRule(groupId, readonlyId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on readonly by developer
        Assertions.assertNotNull(developerClient.getArtifactCompatibilityRule(groupId, readonlyId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on readonly by developer
        Assertions.assertTrue(developerClient.disableArtifactCompatibilityRule(groupId, readonlyId));

        // --- artifact compatibility rule on admin artifact by readonly
        // Check that API returns 204 No Content when enabling artifact compatibility rule on admin by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactCompatibilityRule(groupId, adminId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, adminId);
        // Check that API returns 200 OK when listing artifact rules on admin by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                readonlyClient.getArtifactCompatibilityRule(groupId, adminId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on admin by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactCompatibilityRule(groupId, adminId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on admin by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactCompatibilityRule(groupId, adminId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on admin by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactCompatibilityRule(groupId, adminId));

        // --- artifact compatibility rule on developer artifact by readonly
        // Check that API returns 204 No Content when enabling artifact compatibility rule on developer by readonly
        Assertions.assertTrue(readonlyClient.enableArtifactCompatibilityRule(groupId, developerId));
        // Get list of artifact rules
        ruleList = readonlyClient.listArtifactRules(groupId, developerId);
        // Check that API returns 200 OK when listing artifact rules on developer by readonly
        Assertions.assertNotNull(ruleList);
        // Check that compatibility rule is present in list of artifact rules
        Assertions.assertTrue(ruleList.contains(RuleType.COMPATIBILITY.name()));
        // Check value of enabled rule
        Assertions.assertEquals(
                readonlyClient.getArtifactCompatibilityRule(groupId, developerId),
                CompatibilityLevel.BACKWARD
        );
        // Check that API returns 200 OK when updating artifact compatibility rule on developer by readonly
        Assertions.assertTrue(readonlyClient.updateArtifactCompatibilityRule(groupId, developerId, compatibilityLevel));
        // Check that API returns 200 OK when getting artifact compatibility rule on developer by readonly
        Assertions.assertNotNull(readonlyClient.getArtifactCompatibilityRule(groupId, developerId));
        // Check that API returns 204 No Content when disabling artifact compatibility rule on developer by readonly
        Assertions.assertTrue(readonlyClient.disableArtifactCompatibilityRule(groupId, developerId));

        // --- LIST ACTION ON GROUP
        // Check that API returns 200 OK when listing group artifacts by admin
        Assertions.assertNotNull(adminClient.listGroupArtifacts(groupId));
        // Check that API returns 200 OK when listing group artifacts by developer
        Assertions.assertNotNull(developerClient.listGroupArtifacts(groupId));
        // Check that API returns 200 OK when listing group artifacts by readonly
        Assertions.assertNotNull(readonlyClient.listGroupArtifacts(groupId));

        // --- READ ACTION ON OWN ARTIFACT
        // Check that API returns 200 OK when reading artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, developerId));
        // Check that API returns 200 OK when reading artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, readonlyId));

        // --- READ ACTION ON OTHER'S ARTIFACT
        // Check that API returns 200 OK when reading developer artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, developerId));
        // Check that API returns 200 OK when reading readonly artifact by admin
        Assertions.assertNotNull(adminClient.readArtifactContent(groupId, readonlyId));
        // Check that API returns 200 OK when reading admin artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading readonly artifact by developer
        Assertions.assertNotNull(developerClient.readArtifactContent(groupId, readonlyId));
        // Check that API returns 200 OK when reading admin artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, adminId));
        // Check that API returns 200 OK when reading developer artifact by readonly
        Assertions.assertNotNull(readonlyClient.readArtifactContent(groupId, developerId));

        // --- UPDATE ACTION ON OWN ARTIFACT
        // Check that API returns 200 OK when updating admin artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, adminId, updatedContent));
        // Check that API returns 200 OK when updating developer artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, developerId, updatedContent));
        // Check that API returns 200 OK when updating readonly artifact by readonly
        Assertions.assertTrue(readonlyClient.updateArtifact(groupId, readonlyId, updatedContent));

        // --- UPDATE ACTION ON OTHER'S ARTIFACT
        // Check that API returns 200 OK when updating developer artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, developerId, secondUpdatedContent));
        // Check that API returns 200 OK when updating readonly artifact by admin
        Assertions.assertTrue(adminClient.updateArtifact(groupId, readonlyId, secondUpdatedContent));
        // Check that API returns 200 OK when updating admin artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, adminId, secondUpdatedContent));
        // Check that API returns 200 OK when updating readonly artifact by developer
        Assertions.assertTrue(developerClient.updateArtifact(groupId, readonlyId, thirdUpdatedContent));
        // Check that API returns 200 OK when updating admin artifact by readonly
        Assertions.assertTrue(readonlyClient.updateArtifact(groupId, adminId, thirdUpdatedContent));
        // Check that API returns 200 OK when updating developer artifact by readonly
        Assertions.assertTrue(readonlyClient.updateArtifact(groupId, developerId, thirdUpdatedContent));

        // --- DELETE ACTION ON OWN ARTIFACT
        // Check that API returns 204 No Content when deleting artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, adminId));
        // Check that API returns 204 No Content when deleting artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, developerId));
        // Check that API returns 204 No Content when deleting artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, readonlyId));

        // --- DELETE ACTION ON OTHER'S ARTIFACT
        // Check that API returns 204 No Content when deleting developer artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, developerId + secondId));
        // Check that API returns 204 No Content when deleting readonly artifact by admin
        Assertions.assertTrue(adminClient.deleteArtifact(groupId, readonlyId + secondId));
        // Check that API returns 204 No Content when deleting admin artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, adminId + secondId));
        // Check that API returns 204 No Content when deleting readonly artifact by developer
        Assertions.assertTrue(developerClient.deleteArtifact(groupId, readonlyId + thirdId));
        // Check that API returns 204 No Content when deleting admin artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, adminId + thirdId));
        // Check that API returns 204 No Content when deleting developer artifact by readonly
        Assertions.assertTrue(readonlyClient.deleteArtifact(groupId, developerId + thirdId));
    }
}