package org.example.tests.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.RegisterApiTask;
import io.restassured.response.Response;
import org.junit.Assert;

public class RegisterApiSteps {

    @Given("el servicio de registro está disponible")
    @Given("que el servicio de registro está disponible")
    public void el_servicio_de_registro_esta_disponible() {
        // Optionally implement a health check. For now assume available.
    }

    @When("el usuario solicita registro con datos válidos")
    public void el_usuario_solicita_registro_con_datos_validos() {
        Actor actor = ActorManager.getMainActor();
        
        String username = "juan_" + System.currentTimeMillis();
        String email = "juan+" + System.currentTimeMillis() + "@example.com";
        String password = "P@ssw0rd123";
        
        actor.remember("registeredUsername", username);
        actor.remember("registeredEmail", email);
        actor.remember("registeredPassword", password);
        
        actor.attemptsTo(RegisterApiTask.withData(username, email, password));
    }

    @Then("el registro debe devolver código 200")
    public void el_registro_debe_devolver_codigo_200() {
        Actor actor = ActorManager.getMainActor();
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull("No response remembered", resp);
        Assert.assertEquals("Status code should be 200", 200, resp.getStatusCode());
    }

    @Then("la respuesta deberá contener los datos del usuario registrado")
    public void la_respuesta_debera_contener_los_datos_del_usuario_registrado() {
        Actor actor = ActorManager.getMainActor();
        Response resp = actor.recall("registerResponse");
        Assert.assertNotNull("No response remembered", resp);
        
        String username = actor.recall("registeredUsername");
        String email = actor.recall("registeredEmail");
        
        String responseBody = resp.getBody().asString();
        Assert.assertNotNull("Response body should not be null", responseBody);
        Assert.assertFalse("Response body should not be empty", responseBody.isEmpty());
        
        // Verify that the response contains expected data
        // The exact assertions depend on the API response structure
        Assert.assertTrue("Response should contain username or email", 
            responseBody.contains(username) || responseBody.contains(email));
    }
}
