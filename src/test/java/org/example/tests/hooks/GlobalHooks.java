package org.example.tests.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.example.framework.core.actors.ActorManager;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.example.framework.core.abilities.CallApiAbility;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;

import java.util.Collection;

/**
 * Global Cucumber hooks for Serenity Screenplay tests.
 * - Initializes the actor and abilities per scenario
 * - Provides before/after step hooks (placeholders for logging/screenshots)
 * - Cleans up WebDriver and actor state after scenario
 */
public class GlobalHooks {

    private static final String DEFAULT_ACTOR_NAME = "Principal";

    @Before
    public void beforeScenario(Scenario scenario) {
        // Initialize the stage and primary actor using ActorManager to ensure
        // the ActorFactory registry is used and abilities are attached to the
        // same actor instance that tests will retrieve later.
        OnStage.setTheStage(new OnlineCast());
        Actor actor = ActorManager.createAndConfigureActor(DEFAULT_ACTOR_NAME, a -> {});

        // Determine tags to configure abilities
        Collection<String> tags = scenario.getSourceTagNames();

        // If scenario is API, give both CallAnApi and CallApiAbility (wraps CallAnApi)
        if (tags.stream().anyMatch(t -> t.equalsIgnoreCase("@api"))) {
            String apiBase = System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com");
            // Attach CallAnApi and our wrapper CallApiAbility to the registered actor
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, CallAnApi.at(apiBase));
            CallApiAbility ability = CallApiAbility.at(apiBase);
            String token = System.getProperty("auth.token");
            if (token != null && !token.isEmpty()) {
                ability.withToken(token);
            }
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, ability);
        }

        // If scenario is UI, give BrowseTheWeb ability with the Serenity-managed driver
        if (tags.stream().anyMatch(t -> t.equalsIgnoreCase("@ui"))) {
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, BrowseTheWeb.with(ThucydidesWebDriverSupport.getDriver()));
        }

        // Default behavior: if no explicit tag, attach both abilities as a safe default
        if (!tags.stream().anyMatch(t -> t.equalsIgnoreCase("@api") || t.equalsIgnoreCase("@ui"))) {
            String apiBase = System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com");
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, CallAnApi.at(apiBase));
            CallApiAbility ability = CallApiAbility.at(apiBase);
            String token = System.getProperty("auth.token");
            if (token != null && !token.isEmpty()) {
                ability.withToken(token);
            }
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, ability);
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, BrowseTheWeb.with(ThucydidesWebDriverSupport.getDriver()));
        }
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        // Placeholder: can add logging or step-level setup
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        // Placeholder: can capture step screenshots or attach artifacts
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Clean up WebDriver and actor state
        try {
            ThucydidesWebDriverSupport.closeDriver();
        } catch (Exception ignored) {
        }

        try {
            OnStage.drawTheCurtain();
        } catch (Exception ignored) {
        }
    }
}
