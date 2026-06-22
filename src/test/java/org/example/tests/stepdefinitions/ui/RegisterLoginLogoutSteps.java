package org.example.tests.stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Open;
import org.example.framework.core.tasks.Register;
import org.example.framework.core.tasks.Login;
import org.example.framework.core.tasks.Logout;
import org.example.framework.core.questions.HomePageQuestion;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.ui.pages.LoginPage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterLoginLogoutSteps {

    private Actor actor = ActorManager.getMainActor();
    private String registeredEmail;

    @Given("que un usuario nuevo quiere registrarse")
    public void usuario_nuevo_quiere_registrarse() {
        String baseUrl = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        actor.can(BrowseTheWeb.with(net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver()));
        actor.attemptsTo(Open.url(baseUrl));
        actor.attemptsTo(
                Click.on(LoginPage.SIGNUP_LINK)
        );
    }

    @When("completa el formulario de registro con nombre {string} email {string} password {string}")
    public void completa_formulario_registro(String name, String email, String password) {
        long uniqueSuffix = System.currentTimeMillis();
        this.registeredEmail = email.replace("@", "+" + uniqueSuffix + "@");
        actor.attemptsTo(Register.withData(name, registeredEmail, password));
    }

    @Then("el registro debe ser exitoso")
    public void registro_exitoso() {
        assertTrue(HomePageQuestion.isAuthenticated().answeredBy(actor));
    }

    @When("el usuario intenta iniciar sesión con email {string} y password {string}")
    public void usuario_inicia_sesion(String email, String password) {
        String loginEmail = registeredEmail != null ? registeredEmail : email;
        String baseUrl = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        actor.attemptsTo(Open.url(baseUrl + "/login"));
        actor.attemptsTo(Login.withCredentials(loginEmail, password));
    }

    @Then("el usuario debe verse autenticado en la pantalla de inicio")
    public void usuario_autenticado() {
        assertTrue(HomePageQuestion.isAuthenticated().answeredBy(actor));
    }

    @When("el usuario cierra sesión")
    public void usuario_cierra_sesion() {
        actor.attemptsTo(Logout.now());
    }

    @Then("el usuario no debe estar autenticado")
    public void usuario_no_autenticado() {
        assertFalse(HomePageQuestion.isAuthenticated().answeredBy(actor));
    }
}
