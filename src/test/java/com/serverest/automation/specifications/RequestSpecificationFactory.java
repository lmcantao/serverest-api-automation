package com.serverest.automation.specifications;

import com.serverest.automation.config.EnvironmentConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.filter.log.LogDetail.ALL;

public final class RequestSpecificationFactory {

    private RequestSpecificationFactory() {
    }

    public static RequestSpecification defaultSpec() {

        return new RequestSpecBuilder()
                .setBaseUri(EnvironmentConfig.getBaseUrl())
                .log(ALL)
                .build();

    }
}