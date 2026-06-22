package org.example.tests.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.CreateContactTask;
import org.example.framework.core.tasks.GetContactTask;
import org.example.framework.core.tasks.DeleteContactTask;
import org.example.framework.core.tasks.UpdateContactTask;
import org.example.framework.core.tasks.LoginApiTask;
import org.example.framework.domain.models.ContactRequest;
import org.example.framework.domain.models.ContactResponse;
import io.restassured.response.Response;
import org.junit.Assert;

public class ContactsSteps {

    private Actor actor = ActorManager.getMainActor();

    @Given("the authentication service is available")
    public void auth_service_available() {
        // health checks could be added
    }

    @When("the user authenticates with valid credentials")
    public void user_authenticates() {
        String user = System.getProperty("test.user", "user@example.com");
        String pass = System.getProperty("test.password", "password123");
        actor.attemptsTo(LoginApiTask.withCredentials(user, pass));
    }

    @When("the user creates a new contact")
    public void user_creates_contact() {
        ContactRequest req = ContactRequest.builder()
                .name("Test Contact")
                .email("test+" + System.currentTimeMillis() + "@example.com")
                .phone("+34123456789")
                .build();

        actor.attemptsTo(CreateContactTask.withPayload(req));
    }

    @Then("the contact is created successfully")
    public void contact_created() {
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull(resp);
        Assert.assertTrue(resp.getStatusCode() == 201 || resp.getStatusCode() == 200);
        String id = actor.recall("contactId");
        Assert.assertNotNull("contactId not stored", id);
    }

    @When("the user retrieves the created contact")
    public void user_gets_contact() {
        String id = actor.recall("contactId");
        actor.attemptsTo(GetContactTask.withId(id));
    }

    @Then("the contact details are returned")
    public void contact_details_returned() {
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull(resp);
        Assert.assertEquals(200, resp.getStatusCode());
    }

    @When("the user updates the created contact name to {string}")
    public void user_updates_contact_name(String updatedName) {
        String id = actor.recall("contactId");
        ContactResponse created = actor.recall("contactResponse");
        Assert.assertNotNull("Created contact response is missing", created);

        ContactRequest request = ContactRequest.builder()
                .name(updatedName)
                .email(created.getEmail())
                .phone(created.getPhone())
                .build();

        actor.attemptsTo(UpdateContactTask.withIdAndPayload(id, request));
    }

    @Then("the contact update is accepted")
    public void contact_update_accepted() {
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull(resp);
        Assert.assertEquals(200, resp.getStatusCode());
    }

    @Then("the contact name should be {string}")
    public void contact_name_should_be(String expectedName) {
        ContactResponse response = actor.recall("contactResponse");
        Assert.assertNotNull(response);
        Assert.assertEquals(expectedName, response.getName());
    }

    @When("the user deletes the created contact")
    public void user_deletes_contact() {
        String id = actor.recall("contactId");
        actor.attemptsTo(DeleteContactTask.withId(id));
    }

    @Then("the contact is removed successfully")
    public void contact_removed() {
        Response resp = actor.recall("lastResponse");
        Assert.assertNotNull(resp);
        Assert.assertTrue(resp.getStatusCode() == 200 || resp.getStatusCode() == 204);
    }
}
