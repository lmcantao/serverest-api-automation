package com.serverest.automation.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

public class HealthCheckTest {

    @Test
    void shouldListUsers() {

        RestAssured
                .given()
                .baseUri("https://serverest.dev")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("usuarios", notNullValue());

    }

}