package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.example.framework.api.endpoints.ApiEndpoints;
import org.example.framework.domain.models.ContactRequest;
import org.example.framework.domain.models.ContactResponse;
import org.example.framework.core.abilities.CallApiAbility;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateContactTask implements Task {

    private final ContactRequest payload;

    public CreateContactTask(ContactRequest payload) {
        this.payload = payload;
    }

    public static CreateContactTask withPayload(ContactRequest payload) {
        return Tasks.instrumented(CreateContactTask.class, payload);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        final CallApiAbility apiAbility = extractCallApiAbility(actor);

        actor.attemptsTo(Post.to(ApiEndpoints.CONTACTS).with(request -> {
            if (apiAbility != null) apiAbility.enrich(request);
            request.contentType("application/json").body(payload);
            return request;
        }));

        Response resp = LastResponse.received().answeredBy(actor);
        actor.remember("lastResponse", resp);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ContactResponse cr = mapper.readValue(resp.getBody().asString(), ContactResponse.class);
            if (cr != null && cr.getId() != null) {
                actor.remember("contactId", cr.getId());
                actor.remember("contactResponse", cr);
            }
        } catch (Exception ignored) {}
    }

    private <T extends Actor> CallApiAbility extractCallApiAbility(T actor) {
        try {
            return CallApiAbility.as(actor);
        } catch (Exception ignored) {
            return null;
        }
    }
}
