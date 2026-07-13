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
@Feature("Atualização de usuários")
public class UpdateUserTest extends BaseTest {

    private final UserService userService = new UserService();

    @Test
    @Story("Atualizar um usuário existente")
    @Description(
            "Cria um usuário, atualiza seus dados e consulta novamente " +
            "para confirmar que as alterações foram persistidas."
    )
    @Severity(SeverityLevel.CRITICAL)
    void shouldUpdateUserSuccessfully() {

        // Cria um usuário para garantir independência do teste
        User originalUser = UserFactory.validUser();

        Response createResponse = userService.create(originalUser);

        assertThat(createResponse.statusCode())
                .as("A pré-condição de criação deve retornar status 201")
                .isEqualTo(201);

        String userId = createResponse.jsonPath().getString("_id");

        assertThat(userId)
                .as("O ID do usuário criado deve ser retornado")
                .isNotBlank();

        // Gera novos dados e atualiza o usuário
        User updatedUser = UserFactory.validUser();

        Response updateResponse = userService.update(userId, updatedUser);

        // Valida a resposta do PUT
        assertThat(updateResponse.statusCode())
                .as("A atualização deve retornar status 200")
                .isEqualTo(200);

        assertThat(updateResponse.jsonPath().getString("message"))
                .as("A API deve confirmar a atualização")
                .isEqualTo("Registro alterado com sucesso");

        // Validação adicional: consulta novamente o recurso
        Response getResponse = userService.getById(userId);

        assertThat(getResponse.statusCode())
                .as("A consulta do usuário atualizado deve retornar status 200")
                .isEqualTo(200);

        assertThat(getResponse.jsonPath().getString("nome"))
                .as("O nome deve ter sido atualizado")
                .isEqualTo(updatedUser.getNome());

        assertThat(getResponse.jsonPath().getString("email"))
                .as("O e-mail deve ter sido atualizado")
                .isEqualTo(updatedUser.getEmail());

        assertThat(getResponse.jsonPath().getString("password"))
                .as("A senha deve ter sido atualizada")
                .isEqualTo(updatedUser.getPassword());

        assertThat(getResponse.jsonPath().getString("administrador"))
                .as("O perfil de administrador deve ter sido atualizado")
                .isEqualTo(updatedUser.getAdministrador());
    }

    @Test
    @Story("Impedir atualização com dados inválidos")
    @Description(
        "Cria um usuário válido e valida que a API rejeita " +
        "a tentativa de atualização sem os campos obrigatórios."
    )
    @Severity(SeverityLevel.CRITICAL)
    void shouldNotUpdateUserWithInvalidData() {

        User originalUser = UserFactory.validUser();

        Response createResponse = userService.create(originalUser);

        assertThat(createResponse.statusCode())
                .as("A pré-condição de criação deve retornar status 201")
                .isEqualTo(201);

        String userId = createResponse.jsonPath().getString("_id");

        assertThat(userId)
                .as("O ID do usuário criado deve ser retornado")
                .isNotBlank();

        User invalidUser = User.builder().build();

        Response updateResponse = userService.update(userId, invalidUser);

        assertThat(updateResponse.statusCode())
                .as("A atualização sem campos obrigatórios deve retornar status 400")
                .isEqualTo(400);

        assertThat(updateResponse.jsonPath().getString("nome"))
                .as("A API deve retornar erro para o campo nome")
                .isNotBlank();

        assertThat(updateResponse.jsonPath().getString("email"))
                .as("A API deve retornar erro para o campo e-mail")
                .isNotBlank();

        assertThat(updateResponse.jsonPath().getString("password"))
                .as("A API deve retornar erro para o campo password")
                .isNotBlank();

        assertThat(updateResponse.jsonPath().getString("administrador"))
                .as("A API deve retornar erro para o campo administrador")
                .isNotBlank();
    }
}