package com.serverest.automation.tests.base;

import com.serverest.automation.services.UserService;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest {

    private final UserService userService = new UserService();

    private final List<String> createdUserIds = new ArrayList<>();

    protected void registerUserForCleanup(String userId) {
        if (userId != null && !userId.isBlank()) {
            createdUserIds.add(userId);
        }
    }

    @AfterEach
    void cleanupCreatedUsers() {

        for (String userId : createdUserIds) {

            Response response = userService.delete(userId);

            if (response.statusCode() != 200) {
                System.err.printf(
                        "Não foi possível remover o usuário de teste com ID %s. Status recebido: %d%n",
                        userId,
                        response.statusCode()
                );
            }
        }

        createdUserIds.clear();
    }
}