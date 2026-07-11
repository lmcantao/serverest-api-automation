package com.serverest.automation.services;

import com.serverest.automation.client.ApiClient;
import com.serverest.automation.constants.Endpoints;
import com.serverest.automation.models.User;
import io.restassured.response.Response;

public class UserService {

    private final ApiClient apiClient = new ApiClient();

    public Response create(User user) {
        return apiClient.post(Endpoints.USERS, user);
    }

}