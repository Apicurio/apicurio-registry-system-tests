package io.apicurio.registry.systemtests.framework;


public class Constants {
    public static final String CATALOG_NAME = "testsuite-operators";
    public static final String CATALOG_NAMESPACE = "openshift-marketplace";
    public static final String KAFKA_CONNECT = "kafka-connect-for-registry";
    public static final String KAFKA = "kafka-for-registry";
    public static final String KAFKA_USER = "kafka-user-for-registry";
    public static final String KAFKA_SUBSCRIPTION_NAME = "kafka-subscription";
    public static final String KAFKA_CSV_PREFIX = "amqstreams";
    public static final String REGISTRY = "registry";
    public static final String REGISTRY_SUBSCRIPTION = "registry-subscription";
    public static final String REGISTRY_CSV_PREFIX = "service-registry-operator";
    public static final String SSO_ADMIN_CLIENT_ID = "admin-cli";
    public static final String SSO_ADMIN_USER = "registry-admin"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_CLIENT_API = "registry-client-api"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_CLIENT_UI = "registry-client-ui"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_DEVELOPER_USER = "registry-developer"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_NAME = "registry-sso"; // Defined in kubefiles/keycloak.yaml
    public static final String SSO_NO_ROLE_USER = "registry-no-role-user"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_READONLY_USER = "registry-user"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_REALM = "registry"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_REALM_ADMIN = "master"; // Default Keycloak admin realm name
    public static final String SSO_SCOPE = "user-attributes"; // Defined in configs/user-attribute-client-scope.json
    public static final String SSO_TEST_CLIENT_API = "test-client-api"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_USER_PASSWORD = "changeme"; // Defined in kubefiles/keycloak-realm.yaml
    public static final String SSO_SUBSCRIPTION = "sso-subscription";
    public static final String TESTSUITE_NAMESPACE = "testsuite-namespace";

    public static final String SSO_DB_SECRET_NAME = "keycloak-db-secret";
    public static final String DB_NAME = "postgresql";
    public static final String DB_NAMESPACE = "postgresql";
    public static final String DB_PORT_NAME = "postgresql";
    public static final String DB_MOUNT_PATH = "postgresql";
    public static final String DB_PASSWORD = "password";
    public static final String DB_USERNAME = "username";
    public static final String TRUSTSTORE_SECRET_NAME = "mytruststore-secret";
    public static final String TRUSTSTORE_DATA_NAME = "myTrustStore";
    public static final String ROUTER_CERTS = "router-certs";
    public static final String OAUTH_KAFKA_NAME = "kafka1";
    public static final String HTTPS_SECRET_NAME = "https-cert-secret";
    public static final String HTTPS_SECRET_CRT = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURpVENDQW5HZ0F3SUJBZ0lVRm" +
            "lJY0Z5ckRlZUZVRlltWTBKaTJjU0hjdEdrd0RRWUpLb1pJaHZjTkFRRUwKQlFBd1ZERUxNQWtHQTFVRUJoTUNRMW94RXpBUkJnTlZCQW" +
            "dNQ2xOdmJXVXRVM1JoZEdVeERUQUxCZ05WQkFjTQpCRUp5Ym04eElUQWZCZ05WQkFvTUdFbHVkR1Z5Ym1WMElGZHBaR2RwZEhNZ1VIUj" +
            "VJRXgwWkRBZUZ3MHlOREE1Ck1ETXdPVEEwTlRKYUZ3MHpOREE1TURFd09UQTBOVEphTUZReEN6QUpCZ05WQkFZVEFrTmFNUk13RVFZRF" +
            "ZRUUkKREFwVGIyMWxMVk4wWVhSbE1RMHdDd1lEVlFRSERBUkNjbTV2TVNFd0h3WURWUVFLREJoSmJuUmxjbTVsZENCWAphV1JuYVhSek" +
            "lGQjBlU0JNZEdRd2dnRWlNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0SUJEd0F3Z2dFS0FvSUJBUURjClZsYVBDZFlzT0hhQ2tnaVJLdnNFeT" +
            "hQcnJ3OGdISjNwNUxwbEdsTDlTSERUOG1tRkcyeFd5RzVmVmpzbXJubnQKMWFzNlhRQTVNMnBzYXhxUTdvakwvRnpBbFpIV0YrWlV5dn" +
            "pFTnhVTE5ENHpUeFR5MlUwZkZTdkpianRSckgzNwpEZ29zWVBBRTNzSDgwVW5YT2xad3A1c2d0bE1wOHMrZEZRamJGMHpSRDNyZ1NyaT" +
            "llUGk5bUpjNEJGVWZyV2hZCjhselRWYXdnZ29iUzE0N2h4dE9qUlNTY0IwSWoyUG03emg0TFB1eDFYTDllQ3JWOUxjSkFzUHRtUnZaU2" +
            "laSnYKMFh0bTRDOXE1OGRjZzNVR2pSOXF0QTJHcjgvUER1Undsenl3ajZKc0JYMUpWcDMzNUdwYlNtdHBhdkJrblM5TgpDa3J3Q0xlST" +
            "FhWUM1T1c0cHZjZkFnTUJBQUdqVXpCUk1CMEdBMVVkRGdRV0JCUTdCT0VhQ2V5ZXhPa3Q3VDZICjlWbm9NMEp0QURBZkJnTlZIU01FR0" +
            "RBV2dCUTdCT0VhQ2V5ZXhPa3Q3VDZIOVZub00wSnRBREFQQmdOVkhSTUIKQWY4RUJUQURBUUgvTUEwR0NTcUdTSWIzRFFFQkN3VUFBNE" +
            "lCQVFBYy92eWhadmxJaTlha29jMUI3OVpHMmgySgpvL1R2SnhvYjdGMlBKVUUwQVgxSWhpOUhRemxYR2NFTkpVWUJscG5LT1kzbjRiK0" +
            "c1RTVxNWpQODk4UGZ1czVDCnRrYjIwWEtNT2VpaWNIOU9Yb2tKWVlLd3Qvd3pkMDkzL0tIRUFxVVRTYzFZbzZZL3dTL1plL1ovZGJkaH" +
            "I5Z3QKeXpRMnBhZkJVQ3ZRVHBnbXlxbERSL2JQak5DY01FOEhySlFLbzVBMDBvYjMzTlp2bCtCT1VJN3JCOFJaSmJwVApKdXNrbkRXVU" +
            "s1SGRvUE9BbUNZVnpDcnF2aFJoTnlEbGN4aWFDeTNDbmVhaE5NOENwQkRhNEw1U2xUWnNOSDNqCkY5eUwvU1ZwRkkvY3RYQTNSNDlDMj" +
            "BxSXppdXgyenFZVGpsTEUxaFdpTXJQNUdzUEpucWVZeDhrekduagotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==";
    public static final String HTTPS_SECRET_KEY = "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2Z0lCQURBTkJna3Foa2lHOX" +
            "cwQkFRRUZBQVNDQktnd2dnU2tBZ0VBQW9JQkFRRGNWbGFQQ2RZc09IYUMKa2dpUkt2c0V5OFBycnc4Z0hKM3A1THBsR2xMOVNIRFQ4bW" +
            "1GRzJ4V3lHNWZWanNtcm5udDFhczZYUUE1TTJwcwpheHFRN29qTC9GekFsWkhXRitaVXl2ekVOeFVMTkQ0elR4VHkyVTBmRlN2SmJqdF" +
            "JySDM3RGdvc1lQQUUzc0g4CjBVblhPbFp3cDVzZ3RsTXA4cytkRlFqYkYwelJEM3JnU3JpOWVQaTltSmM0QkZVZnJXaFk4bHpUVmF3Z2" +
            "dvYlMKMTQ3aHh0T2pSU1NjQjBJajJQbTd6aDRMUHV4MVhMOWVDclY5TGNKQXNQdG1SdlpTaVpKdjBYdG00QzlxNThkYwpnM1VHalI5cX" +
            "RBMkdyOC9QRHVSd2x6eXdqNkpzQlgxSlZwMzM1R3BiU210cGF2QmtuUzlOQ2tyd0NMZUkxYVlDCjVPVzRwdmNmQWdNQkFBRUNnZ0VBUm" +
            "ova3d5QXd2OUtVdmxhUEVyR3diTHdHMERyMVNKaTNKRjUvcTBKMFdUcmkKZGtEZ3NjYW5pUmZJVU84dVZzdmtKaGNzeTZQaE1WdWFQOH" +
            "o2SUdGZERpUjg4ZzM5L0ZPTVpWWit0cFNWU2dnegpHS3JMRkE2Q1R6bXh4encwLzNwT2hGL2FrSUdycFBIY3laMTB2SUZtRSs3c1A4Yk" +
            "J4eXNhVkl2alVlOGcxN3BQCktwQlB4aFJFOVJQR2RUa2hjWlFEOFNuSy92ZE1pV0NtNGVlRE5CUTE5S1o0MmQ1UWU4dGdibS8xck93VV" +
            "labnQKS0tvT2dYUkJYWWtvOGNVNEVJbmFzVGpvbllYZkVVSWUyT0Z6MkVRaFJ2THEzRlorVXJQcVVjOHpqTE85SGpOOApZbFl3SkRqRG" +
            "5lbmFubkl5UzBEUVNqREJ1allFNlplMDY0SDZ1WU55QVFLQmdRRDRjamV0RVpJeHdSTzFhdGFzCkwrQThuMWYzWnFrT1N4U1IwRnZ4QX" +
            "RoVm9LUXFTWk1IaG5yNUtFQmlTYWlITmJkTXJqNXd2Z3ZPbllSMHIybTYKUmhWSUZWWVNvdDE0bUJwNmIxTHg5dUtCMHV5WVhNTmlOam" +
            "hJbjZ2WTcvQXZCU2M3VHN5UkhvdlZNQjN3YmYrTwpnYjdiSHBaQ00vZXI4OHRTV2p6QVBXbHBBUUtCZ1FEakNWVzBBV3NGcGxmNURpZU" +
            "RmSnJpSUd4RU5vZStzbU1pCnRzK3k4dVdZMStDVkt1ZEV1SmVla0pWOHhYK3dkMGNUYVBTc05NMGNVL1hFY0h6OHFVREJ2dVBTRFVIOH" +
            "RLd3gKN0hhRmtHSXRPelBnSjdCOENoY21pSnAyYWJyaWR1dzk5bGVYZU8wUVBjUWdON05FT0NDQmpMRnFrODk4RlJaLwpyVHBiRTZOQU" +
            "h3S0JnUUNSNHVmRWphUE42WFU4T3k4eFRxK2FpV3FyRUxKR2ZWbm53WFNya2lNY0xNY1pPYmpIClczVk1aeEZmWmFjN05oU2JSMW5NOU" +
            "J1VXlMUGxuTGMxdFQ5NzQyTjJjQUlpZFJaZlBJMTFIYWFsMndnbzg1ZWIKVFFGQnk5aXArMEtMS2JoK3YrZXBjMGxpOXUwdHFEbS9JWH" +
            "JEeWpNclRyNnBUdS9lUExTZG9iSUxBUUtCZ0JJYQpxblRpTUJDUlJTNmRERldWMkJ2MERlNGFreFNIMFJQeGsvcG5HQnZxQ3dTYUdUc0" +
            "hwVCtGdEFYeEVjK1drMXBlCjRqUGhPZ0hxU0F6VUU0TnFVN21mYVRkVXkxQkZiLzNESjJoYkxSa0NRWTY4VzN4b3FaUHZETElvbURoNj" +
            "F6ZlIKcndackpDeEpZSnhaRkxoNTZVMnJWSEoxT3ZSZ0VoMDRTUkQrTGVSdEFvR0JBTmxvU0s1NWpZWDVnUGI1MktLRQpXemljY09GOE" +
            "FHV2s1V0Z6NDFTY2pTVktyckZLRFFEL3ZJQk0ycE0rMy9jaE5PaWNLc1krMnp5VWxPQ2RvaHVIClhkd3BxYmI4M0R4K3U5NmFXaWpqbz" +
            "BhNzB3bmczZ1VqRXQzUC9hQTFxYXVRSmUwUmJOT0JLTnpiOXVUeVp1OGcKSFJKaDJXWFAzQ0wrU0xZN1EwZElNS0E4Ci0tLS0tRU5EIF" +
            "BSSVZBVEUgS0VZLS0tLS0K";

    // TODO: Move other constants here too?
    // PostgreSQL port
    // PostgreSQL username
    // PostgreSQL password
    // PostgreSQL admin password
    // PostgreSQL database name
    // PostgreSQL image
    // PostgreSQL volume size
    // Default KafkaSQL values
    // Catalog source display name
    // Catalog source publisher
    // Catalog source source type
    // Catalog source pod label
}
