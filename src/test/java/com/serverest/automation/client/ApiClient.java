package com.serverest.automation.client;

import com.serverest.automation.specifications.RequestSpecificationFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {

    public Response get(String endpoint) {
        return given()
                .spec(RequestSpecificationFactory.unauthenticated())
                .when()
                .get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return given()
                .spec(RequestSpecificationFactory.unauthenticated())
                .body(body)
                .when()
                .post(endpoint);
    }

    public Response put(String endpoint, Object body) {
        return given()
                .spec(RequestSpecificationFactory.unauthenticated())
                .body(body)
                .when()
                .put(endpoint);
    }

    public Response delete(String endpoint) {
        return given()
                .spec(RequestSpecificationFactory.unauthenticated())
                .when()
                .delete(endpoint);
    }
}