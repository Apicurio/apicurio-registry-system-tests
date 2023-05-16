package io.apicurio.registry.systemtests.infra;

import io.apicurio.registry.systemtests.infra.matrix.Auth;
import io.apicurio.registry.systemtests.infra.matrix.KafkaSecurity;
import io.apicurio.registry.systemtests.infra.matrix.Storage;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.apicurio.registry.systemtests.infra.matrix.Auth.OIDC;

/**
 * @author Jakub Senko <em>m@jsenko.net</em>
 */
@Builder
@Getter
public class Infra {

    private Matrix currentMatrixValues;

    private String registryBaseUrl;

    private String registryAccessToken;

    public RequestSpecification restAssuredForRegistry() {
        var request = RestAssured.given()
                .baseUri(registryBaseUrl);

        if (currentMatrixValues.getAuth() == OIDC) {
            request = request.filters((requestSpec, responseSpec, ctx) -> {
                requestSpec.header("Authorization", "Bearer " + registryAccessToken);
                return ctx.next(requestSpec, responseSpec);
            });
        }
        // TODO Basic...

        request = request.log().all(true);

        return request;
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Matrix {

        Storage storage;

        Auth auth;

        KafkaSecurity kafkaSecurity;
    }
}
