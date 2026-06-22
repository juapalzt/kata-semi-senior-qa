package org.example.tests.stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Open;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.Login;
import org.example.framework.core.questions.HomePageQuestion;

import static org.junit.Assert.assertTrue;

public class LoginUIStepsEnglish {

    private final Actor actor = ActorManager.getMainActor();

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        String baseUrl = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        actor.attemptsTo(Open.url(baseUrl + "/login"));
    }

    @When("the user logs in with valid credentials")
    public void the_user_logs_in_with_valid_credentials() {
        String user = System.getProperty("test.user", "user@example.com");
        String pass = System.getProperty("test.password", "password123");
        actor.attemptsTo(Login.withCredentials(user, pass));
    }

    @Then("the user should see the dashboard")
    public void the_user_should_see_the_dashboard() {
        assertTrue(HomePageQuestion.isAuthenticated().answeredBy(actor));
    }
}
