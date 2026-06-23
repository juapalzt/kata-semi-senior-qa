package org.example.tests.stepdefinitions.ui;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.Login;
import org.example.framework.core.questions.HomePageQuestion;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Step definitions for Login UI scenarios in Spanish.
 * Supports both valid and invalid login flows.
 */
public class LoginUIStepsSpanish {

    private final Actor actor = ActorManager.getMainActor();

    /**
     * Step: "Dado que el usuario se encuentra en la página de inicio de sesión"
     * Opens the login page
     */
    @Dado("que el usuario se encuentra en la página de inicio de sesión")
    public void que_el_usuario_se_encuentra_en_la_pagina_de_inicio_de_sesion() {
        String baseUrl = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        actor.attemptsTo(Open.url(baseUrl + "/login"));
    }

    /**
     * Step: "Cuando ingresa credenciales válidas"
     * Enters valid credentials from system properties
     */
    @Cuando("ingresa credenciales válidas")
    public void ingresa_credenciales_validas() {
        String user = System.getProperty("test.user", "user@example.com");
        String pass = System.getProperty("test.password", "password123");
        actor.attemptsTo(Login.withCredentials(user, pass));
    }

    /**
     * Step: "Cuando ingresa credenciales inválidas"
     * Enters invalid credentials that should fail authentication
     */
    @Cuando("ingresa credenciales inválidas")
    public void ingresa_credenciales_invalidas() {
        String invalidEmail = "invalid" + System.currentTimeMillis() + "@fake.com";
        String invalidPassword = "wrongpassword123";
        actor.attemptsTo(Login.withCredentials(invalidEmail, invalidPassword));
    }

    /**
     * Step: "Y presiona el botón iniciar sesión"
     * This is already covered by Login.withCredentials() which includes the click action
     */
    @Cuando("presiona el botón iniciar sesión")
    public void presiona_el_boton_iniciar_sesion() {
        // The Login.withCredentials task already includes this action
        // This step exists for clarity in the feature file
    }

    /**
     * Step: "Entonces debe visualizar la lista de contactos"
     * Verifies that the contact list is visible (indicating successful authentication)
     */
    @Entonces("debe visualizar la lista de contactos")
    public void debe_visualizar_la_lista_de_contactos() {
        assertTrue("La lista de contactos no es visible", 
            HomePageQuestion.isAuthenticated().answeredBy(actor));
    }

    /**
     * Step: "Y debe visualizar el botón de cerrar sesión"
     * Verifies that the logout button is visible
     */
    @Entonces("debe visualizar el botón de cerrar sesión")
    public void debe_visualizar_el_boton_de_cerrar_sesion() {
        // Verify logout button is visible
        Target logoutButton = Target.the("logout button").located(By.id("logout"));
        assertTrue("El botón de logout no es visible", logoutButton.resolveFor(actor).isVisible());
    }

    /**
     * Step: "Entonces debe visualizar un mensaje de error"
     * Verifies that an error message is displayed after failed login
     */
    @Entonces("debe visualizar un mensaje de error")
    public void debe_visualizar_un_mensaje_de_error() {
        var driver = BrowseTheWeb.as(actor).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Target errorBox = Target.the("error container").located(By.cssSelector("#error"));
        boolean errorVisible;
        String errorText = "";

        try {
            errorVisible = wait.until(ExpectedConditions.visibilityOf(errorBox.resolveFor(actor))) != null;
            if (errorVisible) {
                errorText = errorBox.resolveFor(actor).getText().trim();
            }
        } catch (TimeoutException timeout) {
            errorVisible = false;
        }

        assertTrue("El elemento #error no apareció en 5 segundos", errorVisible);
        System.out.println("[UI Error Validation] Mensaje capturado: " + errorText);
    }

    /**
     * Step: "Y debe permanecer en la página de inicio de sesión"
     * Verifies that the user remains on the login page (URL check)
     */
    @Entonces("debe permanecer en la página de inicio de sesión")
    public void debe_permanecer_en_la_pagina_de_inicio_de_sesion() {
        String currentUrl = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
        // Verify we're still on login page - not redirected
        assertTrue("Se redireccionó desde la página de login", 
            currentUrl.contains("/login"));
    }

    /**
     * Step: "Y el usuario no está autenticado"
     * Verifies that the user is NOT authenticated
     */
    @Entonces("el usuario no está autenticado")
    public void el_usuario_no_esta_autenticado() {
        assertFalse("El usuario debería no estar autenticado después de fallo en login",
            HomePageQuestion.isAuthenticated().answeredBy(actor));
    }

}
