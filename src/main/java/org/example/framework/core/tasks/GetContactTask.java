package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.example.framework.api.endpoints.ApiEndpoints;
import org.example.framework.core.abilities.CallApiAbility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.framework.domain.models.ContactResponse;

public class GetContactTask implements Task {

    private final String contactId;

    public GetContactTask(String contactId) {
        this.contactId = contactId;
    }

    public static GetContactTask withId(String contactId) {
        return Tasks.instrumented(GetContactTask.class, contactId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        final CallApiAbility apiAbility = extractCallApiAbility(actor);

        String resource = ApiEndpoints.CONTACTS + "/" + contactId;
        actor.attemptsTo(Get.resource(resource).with(request -> {
            if (apiAbility != null) apiAbility.enrich(request);
            return request;
        }));

        Response resp = LastResponse.received().answeredBy(actor);
        actor.remember("lastResponse", resp);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ContactResponse cr = mapper.readValue(resp.getBody().asString(), ContactResponse.class);
            actor.remember("contactResponse", cr);
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
