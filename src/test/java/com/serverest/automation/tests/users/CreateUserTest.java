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
@Feature("Criação de usuários")
public class CreateUserTest extends BaseTest {

    private final UserService userService = new UserService();

    @Test
    @Story("Criar um novo usuário")
    @Description("Valida que um usuário com dados válidos pode ser criado com sucesso.")
    @Severity(SeverityLevel.CRITICAL)
    void shouldCreateUserSuccessfully() {

        User user = UserFactory.validUser();

        Response response = userService.create(user);

        response.then()
        .body(matchesJsonSchemaInClasspath(
                "create-user-schema.json"
        ));

        assertThat(response.statusCode())
                .as("O status code da criação deve ser 201")
                .isEqualTo(201);

        assertThat(response.jsonPath().getString("message"))
                .as("A mensagem de sucesso deve ser retornada")
                .isEqualTo("Cadastro realizado com sucesso");

        assertThat(response.jsonPath().getString("_id"))
                .as("O ID do usuário criado deve ser retornado")
                .isNotBlank();
    }

    @Test
    @Story("Impedir criação de usuário com e-mail duplicado")
    @Description(
            "Valida que a API não permite cadastrar dois usuários " +
            "com o mesmo endereço de e-mail."
    )
    @Severity(SeverityLevel.CRITICAL)
    void shouldNotCreateUserWithDuplicateEmail() {

        User user = UserFactory.validUser();

        Response firstResponse = userService.create(user);

        assertThat(firstResponse.statusCode())
                .as("A pré-condição de criação do primeiro usuário deve retornar status 201")
                .isEqualTo(201);

        assertThat(firstResponse.jsonPath().getString("_id"))
                .as("O ID do primeiro usuário deve ser retornado")
                .isNotBlank();

        Response duplicateResponse = userService.create(user);

        assertThat(duplicateResponse.statusCode())
                .as("A tentativa de cadastrar e-mail duplicado deve retornar status 400")
                .isEqualTo(400);

        assertThat(duplicateResponse.jsonPath().getString("message"))
                .as("A API deve informar que o e-mail já está sendo usado")
                .isEqualTo("Este email já está sendo usado");
    }

    @Test
    @Story("Impedir criação de usuário sem campos obrigatórios")
    @Description(
            "Valida que a API rejeita a criação de um usuário " +
            "quando os campos obrigatórios não são informados."
    )
    @Severity(SeverityLevel.CRITICAL)
    void shouldNotCreateUserWithoutRequiredFields() {

        User invalidUser = UserFactory.emptyUser();

        Response response = userService.create(invalidUser);

        assertThat(response.statusCode())
                .as("A criação sem campos obrigatórios deve retornar status 400")
                .isEqualTo(400);

        assertThat(response.jsonPath().getString("nome"))
                .as("A API deve informar erro para o campo nome")
                .isNotBlank();

        assertThat(response.jsonPath().getString("email"))
                .as("A API deve informar erro para o campo e-mail")
                .isNotBlank();

        assertThat(response.jsonPath().getString("password"))
                .as("A API deve informar erro para o campo password")
                .isNotBlank();

        assertThat(response.jsonPath().getString("administrador"))
                .as("A API deve informar erro para o campo administrador")
                .isNotBlank();
    }
}