package com.serverest.automation.services;

import com.serverest.automation.client.ApiClient;
import com.serverest.automation.constants.Endpoints;
import com.serverest.automation.models.User;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserService {

    private final ApiClient apiClient = new ApiClient();

    @Step("Criar um novo usuário")
    public Response create(User user) {
        return apiClient.post(Endpoints.USERS, user);
    }

    @Step("Listar todos os usuários")
    public Response getAll() {
        return apiClient.get(Endpoints.USERS);
    }

    @Step("Consultar usuário pelo ID: {userId}")
    public Response getById(String userId) {
        return apiClient.get(Endpoints.USERS + "/" + userId);
    }

    @Step("Atualizar usuário pelo ID: {userId}")
    public Response update(String userId, User user) {
        return apiClient.put(Endpoints.USERS + "/" + userId, user);
    }

    @Step("Excluir usuário pelo ID: {userId}")
    public Response delete(String userId) {
        return apiClient.delete(Endpoints.USERS + "/" + userId);
    }
}