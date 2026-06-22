package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;

public class OpenContactList implements Task {

    public static OpenContactList page() {
        return Tasks.instrumented(OpenContactList.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String baseUrl = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        actor.attemptsTo(Open.url(baseUrl + "/contactList"));
    }
}
