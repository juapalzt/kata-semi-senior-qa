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
        String apiBase = System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com");

        boolean isApiOnly = tags.stream().anyMatch(t -> t.equalsIgnoreCase("@api")) &&
                !tags.stream().anyMatch(t -> t.equalsIgnoreCase("@ui")) &&
                !tags.stream().anyMatch(t -> t.equalsIgnoreCase("@e2e"));

        boolean isUiOnly = tags.stream().anyMatch(t -> t.equalsIgnoreCase("@ui")) &&
                !tags.stream().anyMatch(t -> t.equalsIgnoreCase("@e2e"));

        boolean isE2e = tags.stream().anyMatch(t -> t.equalsIgnoreCase("@e2e"));

        // For API scenarios (with or without UI/E2E), always attach API abilities
        if (tags.stream().anyMatch(t -> t.equalsIgnoreCase("@api")) || isE2e) {
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, CallAnApi.at(apiBase));
            CallApiAbility ability = CallApiAbility.at(apiBase);
            String token = System.getProperty("auth.token");
            if (token != null && !token.isEmpty()) {
                ability.withToken(token);
            }
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, ability);
        }

        // For UI scenarios: Initialize BrowseTheWeb immediately ONLY if:
        // - It's @ui (and not @e2e) OR
        // - It's a combined @ui + @api (not @e2e) scenario
        // For @e2e: Delay BrowseTheWeb initialization until after API calls
        if (isUiOnly || (tags.stream().anyMatch(t -> t.equalsIgnoreCase("@ui")) && !isE2e)) {
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, BrowseTheWeb.with(ThucydidesWebDriverSupport.getDriver()));
            try {
                ThucydidesWebDriverSupport.getDriver().manage().window().maximize();
            } catch (Exception ignored) {
            }
        }
        // For @e2e: do NOT initialize BrowseTheWeb yet; it will be added when UI steps begin

        // Default behavior: if no explicit tags at all
        if (!tags.stream().anyMatch(t -> t.equalsIgnoreCase("@api") || t.equalsIgnoreCase("@ui") || t.equalsIgnoreCase("@e2e"))) {
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, CallAnApi.at(apiBase));
            CallApiAbility ability = CallApiAbility.at(apiBase);
            String token = System.getProperty("auth.token");
            if (token != null && !token.isEmpty()) {
                ability.withToken(token);
            }
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, ability);
            ActorManager.giveAbilityTo(DEFAULT_ACTOR_NAME, BrowseTheWeb.with(ThucydidesWebDriverSupport.getDriver()));
            try {
                ThucydidesWebDriverSupport.getDriver().manage().window().maximize();
            } catch (Exception ignored) {
            }
        }

        // Store scenario type for reference in steps
        if (isApiOnly) {
            System.setProperty("scenario.type", "API_ONLY");
        } else if (isUiOnly) {
            System.setProperty("scenario.type", "UI_ONLY");
        } else if (isE2e) {
            System.setProperty("scenario.type", "E2E");
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
        // Close WebDriver only if scenario used UI (@ui or @e2e that opened the browser)
        String scenarioType = System.getProperty("scenario.type", "UNKNOWN");
        if (!scenarioType.equals("API_ONLY")) {
            try {
                ThucydidesWebDriverSupport.closeDriver();
            } catch (Exception ignored) {
            }
        }

        try {
            OnStage.drawTheCurtain();
        } catch (Exception ignored) {
        }
    }
}
