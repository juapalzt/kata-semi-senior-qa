package org.example.tests.stepdefinitions;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Open;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.questions.HomePageQuestion;
import org.example.framework.core.tasks.AddUserApiTask;
import org.example.framework.core.tasks.Login;
import io.restassured.response.Response;
import org.junit.Assert;

public class AddUserApiSteps {

    @When("crea un nuevo usuario vía API con datos válidos")
    public void crea_un_nuevo_usuario_via_api_con_datos_validos() {
        Actor actor = ActorManager.getMainActor();
        String firstName = "Test";
        String lastName = "User";
        String email = "test+" + System.currentTimeMillis() + "@fake.com";
        String password = "myPassword";
        actor.remember("createdEmail", email);
        actor.remember("createdPassword", password);
        actor.attemptsTo(AddUserApiTask.withData(firstName, lastName, email, password));
    }

    @When("el usuario inicia sesión con las credenciales creadas por API")
    public void el_usuario_inicia_sesion_con_las_credenciales_creadas_por_api() {
        Actor actor = ActorManager.getMainActor();
        String email = actor.recall("createdEmail");
        String password = actor.recall("createdPassword");
        Assert.assertNotNull("No se encontró el email creado", email);
        Assert.assertNotNull("No se encontró la contraseña creada", password);

        String baseUrl = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        actor.attemptsTo(Open.url(baseUrl + "/login"));
        actor.attemptsTo(Login.withCredentials(email, password));
    }

    @Then("el login UI debe ser exitoso")
    public void el_login_ui_debe_ser_exitoso() {
        Actor actor = ActorManager.getMainActor();
        Assert.assertTrue("El usuario no está autenticado después del login UI",
                HomePageQuestion.isAuthenticated().answeredBy(actor));
    }

    @Then("el adduser debe devolver código 200")
    public void el_adduser_debe_devolver_codigo_200() {
        Actor actor = ActorManager.getMainActor();
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull("No response remembered", resp);
        int status = resp.getStatusCode();
        System.out.println("[AddUserApi] status=" + status);
        System.out.println("[AddUserApi] response=" + resp.getBody().asString());
        Assert.assertTrue("Expected 200 or 201 but was: " + status, status == 200 || status == 201);
    }

    @Then("la respuesta contendrá el email creado")
    public void la_respuesta_contendra_el_email_creado() {
        Actor actor = ActorManager.getMainActor();
        Response resp = actor.recall("addUserResponse");
        Assert.assertNotNull("No response remembered", resp);
        String responseBody = resp.getBody().asString();
        System.out.println("[AddUserApi] verify response=" + responseBody);
        String expectedEmail = actor.recall("createdEmail");
        Assert.assertTrue("Response body should contain created email", responseBody.contains(expectedEmail));
    }
}
