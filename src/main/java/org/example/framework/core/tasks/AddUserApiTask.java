package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.example.framework.core.abilities.CallApiAbility;

/**
 * Task to add a user via API POST /users
 */
public class AddUserApiTask implements Task {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public AddUserApiTask(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public static AddUserApiTask withData(String firstName, String lastName, String email, String password) {
        return Tasks.instrumented(AddUserApiTask.class, firstName, lastName, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Build payload as a simple map to let RestAssured serialize
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("firstName", firstName);
        payload.put("lastName", lastName);
        payload.put("email", email);
        payload.put("password", password);

        String apiBase = System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        try {
            if (actor.abilityTo(net.serenitybdd.screenplay.rest.abilities.CallAnApi.class) == null) {
                actor.can(net.serenitybdd.screenplay.rest.abilities.CallAnApi.at(apiBase));
            }
        } catch (Exception ignored) {
            actor.can(net.serenitybdd.screenplay.rest.abilities.CallAnApi.at(apiBase));
        }

        CallApiAbility apiAbility = extractCallApiAbility(actor);

        actor.attemptsTo(Post.to("/users").with(request -> {
            if (apiAbility != null) {
                apiAbility.enrich(request);
            }
            request.contentType("application/json").body(payload);
            return request;
        }));

        Response response = LastResponse.received().answeredBy(actor);
        actor.remember("lastResponse", response);
        actor.remember("addUserResponse", response);
    }

    private CallApiAbility extractCallApiAbility(Actor actor) {
        try {
            return CallApiAbility.as(actor);
        } catch (Exception e) {
            return null;
        }
    }
}
