package com.serverest.automation.tests.users;

import com.serverest.automation.services.UserService;
import com.serverest.automation.tests.base.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API de Usuários")
@Feature("Consulta de usuários")
public class GetUsersTest extends BaseTest {

    private final UserService userService = new UserService();

    @Test
    @Story("Listar todos os usuários")
    @Description("Valida que a API retorna com sucesso a lista de usuários cadastrados.")
    @Severity(SeverityLevel.NORMAL)
    void shouldListUsersSuccessfully() {

        Response response = userService.getAll();

        assertThat(response.statusCode())
                .as("O status code da consulta deve ser 200")
                .isEqualTo(200);

        assertThat(response.jsonPath().getInt("quantidade"))
                .as("A quantidade de usuários não deve ser negativa")
                .isGreaterThanOrEqualTo(0);

        assertThat(response.jsonPath().getList("usuarios"))
                .as("A lista de usuários deve existir")
                .isNotNull();
    }
}