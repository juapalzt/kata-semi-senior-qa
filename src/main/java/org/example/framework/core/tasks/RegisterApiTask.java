package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.example.framework.api.endpoints.ApiEndpoints;
import org.example.framework.domain.models.UserRequest;
import org.example.framework.core.abilities.CallApiAbility;
import org.example.framework.core.utils.ApiLogger;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Task that performs a user registration call to the API and stores the response in the actor's memory.
 */
public class RegisterApiTask implements Task {

    private final String username;
    private final String email;
    private final String password;

    public RegisterApiTask(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static RegisterApiTask withData(String username, String email, String password) {
        return Tasks.instrumented(RegisterApiTask.class, username, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        UserRequest payload = UserRequest.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        String apiBase = System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        try {
            if (actor.abilityTo(net.serenitybdd.screenplay.rest.abilities.CallAnApi.class) == null) {
                actor.can(net.serenitybdd.screenplay.rest.abilities.CallAnApi.at(apiBase));
            }
        } catch (Exception ignored) {
            actor.can(net.serenitybdd.screenplay.rest.abilities.CallAnApi.at(apiBase));
        }

        CallApiAbility apiAbility = extractCallApiAbility(actor);

        actor.attemptsTo(Post.to(ApiEndpoints.USERS).with(request -> {
            if (apiAbility != null) {
                apiAbility.enrich(request);
            }
            request.contentType("application/json").body(payload);
            return request;
        }));

        Response response = LastResponse.received().answeredBy(actor);
        actor.remember("lastResponse", response);
        actor.remember("registerResponse", response);
        // Log request/response for easier debugging
        try {
            String scenario = System.getProperty("current.scenario.name", "register");
            ApiLogger.logRequestResponse(apiBase + ApiEndpoints.USERS, payload, response, scenario);
        } catch (Exception ignored) {
        }
    }

    /**
     * Extracts the CallApiAbility from the actor, handling cases where it might not be present.
     */
    private CallApiAbility extractCallApiAbility(Actor actor) {
        try {
            return CallApiAbility.as(actor);
        } catch (Exception e) {
            return null;
        }
    }
}
