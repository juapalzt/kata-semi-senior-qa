package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Put;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.example.framework.api.endpoints.ApiEndpoints;
import org.example.framework.core.abilities.CallApiAbility;
import org.example.framework.domain.models.ContactRequest;
import org.example.framework.domain.models.ContactResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UpdateContactTask implements Task {

    private final String contactId;
    private final ContactRequest payload;

    public UpdateContactTask(String contactId, ContactRequest payload) {
        this.contactId = contactId;
        this.payload = payload;
    }

    public static UpdateContactTask withIdAndPayload(String contactId, ContactRequest payload) {
        return Tasks.instrumented(UpdateContactTask.class, contactId, payload);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        final CallApiAbility apiAbility = extractCallApiAbility(actor);

        String resource = ApiEndpoints.CONTACTS + "/" + contactId;
        actor.attemptsTo(Put.to(resource).with(request -> {
            if (apiAbility != null) apiAbility.enrich(request);
            request.contentType("application/json").body(payload);
            return request;
        }));

        Response resp = LastResponse.received().answeredBy(actor);
        actor.remember("lastResponse", resp);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ContactResponse updated = mapper.readValue(resp.getBody().asString(), ContactResponse.class);
            if (updated != null) {
                actor.remember("contactResponse", updated);
            }
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
