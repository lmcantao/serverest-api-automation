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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API de Usuários")
@Feature("Exclusão de usuários")
public class DeleteUserTest extends BaseTest {

    private final UserService userService = new UserService();

    @Test
    @Story("Excluir um usuário existente")
    @Description(
            "Cria um usuário, realiza sua exclusão e consulta novamente " +
            "para confirmar que o recurso não existe mais."
    )
    @Severity(SeverityLevel.CRITICAL)
    void shouldDeleteUserSuccessfully() {

        // Cria um usuário para garantir independência do teste
        User user = UserFactory.validUser();

        Response createResponse = userService.create(user);

        assertThat(createResponse.statusCode())
                .as("A pré-condição de criação deve retornar status 201")
                .isEqualTo(201);

        String userId = createResponse.jsonPath().getString("_id");

        assertThat(userId)
                .as("O ID do usuário criado deve ser retornado")
                .isNotBlank();

        // Exclui o usuário criado
        Response deleteResponse = userService.delete(userId);

        // Valida a resposta da exclusão
        assertThat(deleteResponse.statusCode())
                .as("A exclusão deve retornar status 200")
                .isEqualTo(200);

        assertThat(deleteResponse.jsonPath().getString("message"))
                .as("A API deve confirmar a exclusão")
                .isEqualTo("Registro excluído com sucesso");

        // Validação adicional: confirma que o recurso não existe mais
        Response getResponse = userService.getById(userId);

        assertThat(getResponse.statusCode())
                .as("A consulta de um usuário excluído deve retornar status 400")
                .isEqualTo(400);

        assertThat(getResponse.jsonPath().getString("message"))
                .as("A API deve informar que o usuário não foi encontrado")
                .isEqualTo("Usuário não encontrado");
    }

    @Test
    @Story("Excluir usuário com ID inexistente")
    @Description(
            "Valida o comportamento da API ao tentar excluir " +
            "um usuário que não existe."
    )
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleDeletionOfNonexistentUser() {

        String nonexistentUserId = "usuario-inexistente-123456789";

        Response response = userService.delete(nonexistentUserId);

        assertThat(response.statusCode())
                .as("A exclusão de um usuário inexistente deve retornar status 200")
                .isEqualTo(200);

        assertThat(response.jsonPath().getString("message"))
                .as("A API deve informar que nenhum registro foi excluído")
                .isEqualTo("Nenhum registro excluído");
    }
}