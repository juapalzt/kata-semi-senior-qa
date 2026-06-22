package org.example.tests.stepdefinitions.e2e;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.example.framework.core.tasks.CreateContactTask;
import org.example.framework.core.tasks.DeleteContactTask;
import org.example.framework.domain.models.ContactRequest;
import org.example.framework.ui.pages.ContactsPage;
import org.example.framework.core.actors.ActorManager;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ContactsE2ESteps {

    private Actor actor = ActorManager.getMainActor();

    @Given("creo un contacto por API con nombre {string} email {string} phone {string}")
    public void creo_contacto_api(String name, String email, String phone) {
        ContactRequest payload = ContactRequest.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        actor.attemptsTo(CreateContactTask.withPayload(payload));
    }

    @When("abro la interfaz de contactos en la UI")
    public void abro_interfaz_contactos_ui() {
        String base = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        ThucydidesWebDriverSupport.getDriver().get(base + "/contactList");
        actor.can(BrowseTheWeb.with(ThucydidesWebDriverSupport.getDriver()));
    }

    @Then("debo ver el contacto con email {string} en la lista")
    public void debo_ver_contacto(String email) {
        assertTrue(ContactsPage.contactRowForEmail(email).resolveFor(actor).isVisible());
    }

    @When("elimino el contacto por API")
    public void elimino_contacto_api() {
        String id = actor.recall("contactId");
        actor.attemptsTo(DeleteContactTask.withId(id));
    }

    @Then("no debo ver el contacto con email {string} en la lista")
    public void no_debo_ver_contacto(String email) {
        try {
            boolean visible = ContactsPage.contactRowForEmail(email).resolveFor(actor).isVisible();
            assertFalse(visible);
        } catch (Exception e) {
            // element not found -> expected
            assertTrue(true);
        }
    }
}
