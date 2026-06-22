package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.example.framework.api.endpoints.ApiEndpoints;
import org.example.framework.core.abilities.CallApiAbility;

public class DeleteContactTask implements Task {

    private final String contactId;

    public DeleteContactTask(String contactId) {
        this.contactId = contactId;
    }

    public static DeleteContactTask withId(String contactId) {
        return Tasks.instrumented(DeleteContactTask.class, contactId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        final CallApiAbility apiAbility = extractCallApiAbility(actor);

        String resource = ApiEndpoints.CONTACTS + "/" + contactId;
        actor.attemptsTo(Delete.from(resource).with(request -> {
            if (apiAbility != null) apiAbility.enrich(request);
            return request;
        }));

        Response resp = LastResponse.received().answeredBy(actor);
        actor.remember("lastResponse", resp);
    }

    private <T extends Actor> CallApiAbility extractCallApiAbility(T actor) {
        try {
            return CallApiAbility.as(actor);
        } catch (Exception ignored) {
            return null;
        }
    }
}
