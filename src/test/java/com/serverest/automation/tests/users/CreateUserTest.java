package com.serverest.automation.tests.users;

import com.serverest.automation.factories.UserFactory;
import com.serverest.automation.models.User;
import com.serverest.automation.services.UserService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

    private final UserService userService = new UserService();

    @Test
    void shouldCreateUserSuccessfully() {

        User user = UserFactory.createDefaultUser();

        userService.create(user)
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"));

    }

}