package io.apicurio.registry.systemtests.oauthkafka;

import io.apicurio.registry.systemtests.TestBaseOAuthKafka;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class OAuthKafkaTests extends TestBaseOAuthKafka {
    @Test
    @Tag("smoke")
    @Tag("oauthkafka")
    public void testRegistryOAuthKafka() throws InterruptedException {
        deployOAuthKafkaTestRegistry();
    }
}
