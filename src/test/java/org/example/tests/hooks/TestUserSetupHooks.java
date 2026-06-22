package org.example.tests.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.RegisterApiTask;
import org.example.framework.core.tasks.LoginApiTask;
import org.example.framework.core.abilities.CallApiAbility;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.openqa.selenium.Cookie;

/**
 * Hook to ensure a test user is created and logged in before UI/E2E scenarios.
 * Runs after other global setup (order=50) so browser state clearing hooks run first.
 */
public class TestUserSetupHooks {

    @Before(order = 50)
    public void ensureTestUser(Scenario scenario) {
        boolean needsUser = scenario.getSourceTagNames().stream()
                .anyMatch(t -> t.equalsIgnoreCase("@ui") || t.equalsIgnoreCase("@e2e"));

        if (!needsUser) {
            return;
        }

        Actor actor = ActorManager.getMainActor();

        String apiBase = System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        try {
            ActorManager.giveAbilityTo(actor.getName(), CallAnApi.at(apiBase));
        } catch (Exception ignored) {}

        // Build unique credentials
        long ts = System.currentTimeMillis();
        String username = "testuser" + ts;
        String email = "test+" + ts + "@example.com";
        String password = "Passw0rd!";

        // Register via API and then login to obtain token
        actor.attemptsTo(RegisterApiTask.withData(username, email, password));
        actor.attemptsTo(LoginApiTask.withCredentials(email, password));

        // Propagate token to CallApiAbility and system property
        try {
            CallApiAbility ability = CallApiAbility.as(actor);
            if (ability != null) {
                String token = (String) actor.recall("authToken");
                if (token != null && !token.isEmpty()) {
                    ability.withToken(token);
                    System.setProperty("auth.token", token);
                }
            }
        } catch (Exception ignored) {}

        // For UI scenarios, inject token as cookie so client-side JS reads it
        try {
            String token = (String) actor.recall("authToken");
            if (token != null && !token.isEmpty()) {
                if (ThucydidesWebDriverSupport.getDriver() != null) {
                    Cookie ck = new Cookie("token", token);
                    ThucydidesWebDriverSupport.getDriver().manage().addCookie(ck);
                }
            }
        } catch (Exception ignored) {}
    }
}
