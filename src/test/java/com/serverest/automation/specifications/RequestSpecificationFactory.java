package com.serverest.automation.specifications;

import com.serverest.automation.config.ConfigurationManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecificationFactory {

    private RequestSpecificationFactory() {
    }

    public static RequestSpecification unauthenticated() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification authenticated(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .log(LogDetail.ALL)
                .build();
    }
}