package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.framework.core.utils.ApiLogger;
import org.example.framework.api.endpoints.ApiEndpoints;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.example.framework.api.endpoints.ApiEndpoints;
import org.example.framework.domain.models.LoginRequest;
import org.example.framework.domain.models.LoginResponse;
import org.example.framework.core.abilities.CallApiAbility;

/**
 * Task that performs an authentication call to the API and stores the response in the actor's memory.
 */
public class LoginApiTask implements Task {

    private final String username;
    private final String password;

    public LoginApiTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static LoginApiTask withCredentials(String username, String password) {
        return Tasks.instrumented(LoginApiTask.class, username, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        LoginRequest payload = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        // Ensure the actor has the required REST abilities; attach if missing to
        // avoid NullPointerException when Serenity's Post interaction looks up CallAnApi.
        String apiBase = System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        try {
            if (actor.abilityTo(net.serenitybdd.screenplay.rest.abilities.CallAnApi.class) == null) {
                actor.can(net.serenitybdd.screenplay.rest.abilities.CallAnApi.at(apiBase));
            }
        } catch (Exception ignored) {
            actor.can(net.serenitybdd.screenplay.rest.abilities.CallAnApi.at(apiBase));
        }

        CallApiAbility apiAbility = extractCallApiAbility(actor);
        if (apiAbility == null) {
            CallApiAbility ability = CallApiAbility.at(apiBase);
            String token = System.getProperty("auth.token");
            if (token != null && !token.isEmpty()) {
                ability.withToken(token);
            }
            actor.can(ability);
            apiAbility = ability;
        }

        final CallApiAbility finalApiAbility = apiAbility;

        actor.attemptsTo(Post.to(ApiEndpoints.AUTH_LOGIN).with(request -> {
            if (finalApiAbility != null) {
                finalApiAbility.enrich(request);
            }
            request.contentType("application/json").body(payload);
            return request;
        }));

        Response response = LastResponse.received().answeredBy(actor);
        actor.remember("lastResponse", response);

        // Parse response and extract token; store token in actor memory and in CallApiAbility
        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse loginResp = mapper.readValue(response.getBody().asString(), LoginResponse.class);
            if (loginResp != null && loginResp.getToken() != null && !loginResp.getToken().isEmpty()) {
                actor.remember("authToken", loginResp.getToken());
                actor.remember("loginResponse", loginResp);
                try {
                    CallApiAbility ability = CallApiAbility.as(actor);
                    if (ability != null) {
                        ability.withToken(loginResp.getToken());
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        // Log request and response for debugging (writes to target/serenity-reports/evidences/...)
        try {
            String scenario = System.getProperty("current.scenario.name", "login");
            ApiLogger.logRequestResponse(apiBase + ApiEndpoints.AUTH_LOGIN, payload, response, scenario);
        } catch (Exception ignored) {
        }
    }

    private <T extends Actor> CallApiAbility extractCallApiAbility(T actor) {
        try {
            return CallApiAbility.as(actor);
        } catch (Exception ignored) {
            return null;
        }
    }
}
