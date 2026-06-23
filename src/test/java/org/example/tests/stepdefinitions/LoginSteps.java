package org.example.tests.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.LoginApiTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.example.framework.domain.models.LoginResponse;
import org.junit.Assert;

public class LoginSteps {

    @Given("el servicio de autenticación está disponible")
    @Given("que el servicio de autenticación está disponible")
    public void el_servicio_de_autenticacion_esta_disponible() {
        // Optionally implement a health check. For now assume available.
    }

    @When("el usuario solicita autenticación con credenciales válidas")
    @When("el usuario se autentica con credenciales válidas")
    public void el_usuario_solicita_autenticacion_con_credenciales_validas() {
        Actor actor = ActorManager.getMainActor();
        String user = System.getProperty("test.user", "user@example.com");
        String pass = System.getProperty("test.password", "password123");
        actor.attemptsTo(LoginApiTask.withCredentials(user, pass));
    }

    @Then("la respuesta tendrá el código 200")
    public void la_respuesta_tendra_el_codigo_200() {
        Actor actor = ActorManager.getMainActor();
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull("No response remembered", resp);
        Assert.assertEquals(200, resp.getStatusCode());
    }

    @Then("la respuesta contendrá un token de autenticación")
    public void la_respuesta_contendra_un_token_de_autenticacion() {
        Actor actor = ActorManager.getMainActor();
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull("No response remembered", resp);

        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse loginResp = mapper.readValue(resp.getBody().asString(), LoginResponse.class);
            Assert.assertNotNull("Token is missing", loginResp.getToken());
            Assert.assertFalse("Token is empty", loginResp.getToken().isEmpty());
            actor.remember("authToken", loginResp.getToken());
            actor.remember("loginResponse", loginResp);
            // Propagate token into the actor's CallApiAbility if present so future requests reuse it
            try {
                org.example.framework.core.abilities.CallApiAbility ability = org.example.framework.core.abilities.CallApiAbility.as(actor);
                if (ability != null) {
                    ability.withToken(loginResp.getToken());
                }
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse login response", e);
        }
    }
}
