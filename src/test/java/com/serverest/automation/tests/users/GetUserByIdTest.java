package com.serverest.automation.tests.users;

import com.serverest.automation.factories.UserFactory;
import com.serverest.automation.models.User;
import com.serverest.automation.services.UserService;
import com.serverest.automation.tests.base.BaseTest;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API de Usuários")
@Feature("Consulta de usuários")
public class GetUserByIdTest extends BaseTest {

    private final UserService userService = new UserService();

    @Test
    @Story("Consultar usuário por ID")
    @Description("Cria um usuário e valida que seus dados podem ser consultados pelo ID retornado.")
    @Severity(SeverityLevel.CRITICAL)
    void shouldGetUserByIdSuccessfully() {

        User user = UserFactory.validUser();

        Response createResponse = userService.create(user);

        assertThat(createResponse.statusCode())
                .as("A pré-condição de criação do usuário deve retornar status 201")
                .isEqualTo(201);

        String userId = createResponse.jsonPath().getString("_id");

        assertThat(userId)
                .as("O ID do usuário criado deve ser retornado")
                .isNotBlank();

        Response getResponse = userService.getById(userId);

        getResponse.then()
        .body(matchesJsonSchemaInClasspath(
                "get-user-by-id-schema.json"
        ));

        assertThat(getResponse.statusCode())
                .as("O status code da consulta deve ser 200")
                .isEqualTo(200);

        assertThat(getResponse.jsonPath().getString("_id"))
                .as("O ID retornado deve corresponder ao usuário criado")
                .isEqualTo(userId);

        assertThat(getResponse.jsonPath().getString("nome"))
                .as("O nome retornado deve corresponder ao usuário criado")
                .isEqualTo(user.getNome());

        assertThat(getResponse.jsonPath().getString("email"))
                .as("O e-mail retornado deve corresponder ao usuário criado")
                .isEqualTo(user.getEmail());

        assertThat(getResponse.jsonPath().getString("administrador"))
                .as("O perfil de administrador deve corresponder ao usuário criado")
                .isEqualTo(user.getAdministrador());
    }

    @Test
    @Story("Consultar usuário com ID inexistente")
    @Description("Valida que a API retorna erro ao consultar um usuário que não existe.")
    @Severity(SeverityLevel.NORMAL)
    void shouldNotGetNonexistentUser() {

        String nonexistentUserId = "usuario9inexist0";

        Response response = userService.getById(nonexistentUserId);

        assertThat(response.statusCode())
                .as("A consulta de um usuário inexistente deve retornar status 400")
                .isEqualTo(400);

        assertThat(response.jsonPath().getString("message"))
                .as("A API deve informar que o usuário não foi encontrado")
                .isEqualTo("Usuário não encontrado");
 }
}