package org.example.tests.stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.AddContact;
import org.example.framework.core.tasks.DeleteContact;
import org.example.framework.core.tasks.EditContact;
import org.example.framework.core.tasks.OpenContactList;
import org.example.framework.ui.pages.ContactsPage;
import org.example.framework.ui.pages.ContactDetailsPage;
import org.junit.Assert;

public class ContactsUISteps {

    private final Actor actor = ActorManager.getMainActor();

    @Given("que el usuario abre la lista de contactos")
    public void que_el_usuario_abre_la_lista_de_contactos() {
        actor.attemptsTo(OpenContactList.page());
    }

    @Given("existe un contacto con email {string}")
    public void existe_un_contacto_con_email(String email) {
        actor.attemptsTo(AddContact.withData(
                "Nombre",
                "Apellido",
                "1990-01-01",
                email,
                "+34900000000",
                "Calle Falsa 123",
                "Piso 1",
                "Madrid",
                "Madrid",
                "28001",
                "España"
        ));
    }

    @When("crea un contacto con nombre {string} apellidos {string} email {string} telefono {string} ciudad {string} pais {string}")
    public void crea_un_contacto(String firstName, String lastName, String email, String phone, String city, String country) {
        actor.attemptsTo(AddContact.withData(
                firstName,
                lastName,
                "1990-01-01",
                email,
                phone,
                "Calle Principal 1",
                "Edificio A",
                city,
                "N/A",
                "00000",
                country
        ));
    }

    @Then("ve el contacto con email {string} en la lista")
    public void ve_el_contacto_con_email_en_la_lista(String email) {
        Assert.assertTrue(ContactsPage.contactRowForEmail(email).resolveFor(actor).isVisible());
    }

    @When("visualiza el contacto con email {string}")
    public void visualiza_el_contacto_con_email(String email) {
        actor.attemptsTo(org.example.framework.core.tasks.ViewContact.withEmail(email));
    }

    @Then("ve los detalles del contacto con nombre {string} y email {string}")
    public void ve_los_detalles_del_contacto(String expectedName, String expectedEmail) {
        Assert.assertTrue(ContactDetailsPage.NAME.resolveFor(actor).getText().contains(expectedName));
        Assert.assertTrue(ContactDetailsPage.EMAIL.resolveFor(actor).getText().contains(expectedEmail));
    }

    @When("edita el contacto con email {string} para cambiar el nombre a {string}")
    public void edita_el_contacto_con_email_para_cambiar_nombre(String email, String newName) {
        String[] nameParts = newName.split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        actor.attemptsTo(EditContact.withEmail(email, firstName, lastName));
    }

    @Then("ve el contacto con email {string} actualizado con nombre {string}")
    public void ve_el_contacto_actualizado(String email, String expectedName) {
        Assert.assertTrue(ContactsPage.contactRowForEmail(email).resolveFor(actor).getText().contains(expectedName));
    }

    @When("elimina el contacto con email {string}")
    public void elimina_el_contacto_con_email(String email) {
        actor.attemptsTo(DeleteContact.withEmail(email));
    }

    @Then("no debe ver el contacto con email {string} en la lista")
    public void no_debe_ver_el_contacto_en_la_lista(String email) {
        try {
            Assert.assertFalse(ContactsPage.contactRowForEmail(email).resolveFor(actor).isVisible());
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
}
