package org.example.tests.stepdefinitions.ui;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.Login;
import org.example.framework.core.tasks.Logout;
import org.example.framework.ui.pages.ContactsPage;
import org.example.framework.ui.pages.HomePage;
import org.example.framework.ui.pages.LoginPage;
import org.example.framework.ui.pages.RegisterPage;
import org.example.framework.ui.locators.RegisterLocators;

import static org.junit.Assert.assertTrue;

public class UsersUIStepDefinitions {

    private final Actor actor = ActorManager.getMainActor();
    private String registeredEmail;
    private String registeredPassword;
    private String registeredName;

    private String baseUrl() {
        return System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
    }

    private void ensureBrowserAbility() {
        if (!BrowseTheWeb.as(actor).getDriver().toString().contains("null")) {
            return;
        }
        actor.can(BrowseTheWeb.with(net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver()));
    }

    @Dado("que el usuario ingresa a la página Login")
    public void que_el_usuario_ingresa_a_la_pagina_Login() {
        actor.attemptsTo(Open.url(baseUrl() + "/login"));
    }

    @Cuando("selecciona {string}")
    public void selecciona_el_link_de_registro(String linkText) {
        actor.attemptsTo(Click.on(LoginPage.SIGNUP_LINK));
    }

    @Entonces("debe visualizar la página Add User")
    public void debe_visualizar_la_pagina_Add_User() {
        assertTrue("La página Add User no se muestra",
                RegisterPage.FIRST_NAME.resolveFor(actor).isVisible());
    }

    @Cuando("diligencia los datos obligatorios")
    public void diligencia_los_datos_obligatorios() {
        long uniqueSuffix = System.currentTimeMillis();
        this.registeredName = "TestUser" + uniqueSuffix;
        this.registeredEmail = String.format("user+%d@example.com", uniqueSuffix);
        this.registeredPassword = "Password123!";

        actor.attemptsTo(
                Enter.theValue(registeredName).into(RegisterPage.FIRST_NAME),
                Enter.theValue(registeredName).into(RegisterPage.LAST_NAME),
                Enter.theValue(registeredEmail).into(RegisterPage.EMAIL),
                Enter.theValue(registeredPassword).into(RegisterPage.PASSWORD)
        );
    }

    @Cuando("presiona Submit")
    public void presiona_submit() {
        try {
            if (RegisterPage.REGISTER_BUTTON.resolveFor(actor).isVisible()) {
                actor.attemptsTo(Click.on(RegisterPage.REGISTER_BUTTON));
                return;
            }
        } catch (Exception ignored) {
            // Continue to login button if register is not visible
        }
        actor.attemptsTo(Click.on(LoginPage.LOGIN_BUTTON));
    }

    @Entonces("debe visualizar Contact List")
    public void debe_visualizar_contact_list() {
        assertTrue("La lista de contactos no se muestra",
                ContactsPage.ADD_CONTACT_BUTTON.resolveFor(actor).isVisible());
    }

    @Entonces("debe visualizar Add a New Contact")
    public void debe_visualizar_add_a_new_contact() {
        assertTrue("El botón Add a New Contact no se muestra",
                ContactsPage.ADD_CONTACT_BUTTON.resolveFor(actor).isVisible());
    }

    @Entonces("debe visualizar Logout")
    public void debe_visualizar_logout() {
        assertTrue("El botón de Logout no se muestra",
                HomePage.LOGOUT_BUTTON.resolveFor(actor).isVisible());
    }

    @Dado("que el usuario se encuentra en Add User")
    public void que_el_usuario_se_encuentra_en_Add_User() {
        actor.attemptsTo(Open.url(baseUrl() + "/signup"));
    }

    @Cuando("presiona Cancel")
    public void presiona_cancel() {
        actor.attemptsTo(Click.on(RegisterLocators.CANCEL));
    }

    @Dado("que existe un usuario registrado en la UI")
    public void que_existe_un_usuario_registrado_en_la_UI() {
        long uniqueSuffix = System.currentTimeMillis();
        this.registeredName = "ExistingUser" + uniqueSuffix;
        this.registeredEmail = String.format("user+%d@example.com", uniqueSuffix);
        this.registeredPassword = "Password123!";

        actor.attemptsTo(Open.url(baseUrl() + "/login"));
        actor.attemptsTo(Click.on(LoginPage.SIGNUP_LINK));
        actor.attemptsTo(
                Enter.theValue(registeredName).into(RegisterPage.FIRST_NAME),
                Enter.theValue(registeredName).into(RegisterPage.LAST_NAME),
                Enter.theValue(registeredEmail).into(RegisterPage.EMAIL),
                Enter.theValue(registeredPassword).into(RegisterPage.PASSWORD),
                Click.on(RegisterPage.REGISTER_BUTTON)
        );
        actor.attemptsTo(Open.url(baseUrl() + "/login"));
    }

    @Dado("el usuario se encuentra en Login")
    public void el_usuario_se_encuentra_en_Login() {
        actor.attemptsTo(Open.url(baseUrl() + "/login"));
    }

    @Cuando("selecciona Logout")
    public void selecciona_logout() {
        actor.attemptsTo(Logout.now());
    }

    @Entonces("debe regresar a Login")
    public void debe_regresar_a_Login() {
        assertTrue("No se regresó a la página de Login",
                LoginPage.EMAIL.resolveFor(actor).isVisible());
    }
}
