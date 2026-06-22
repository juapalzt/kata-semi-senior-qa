package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import org.example.framework.ui.pages.ContactsPage;

public class ViewContact implements Task {

    private final String email;

    public ViewContact(String email) {
        this.email = email;
    }

    public static ViewContact withEmail(String email) {
        return Tasks.instrumented(ViewContact.class, email);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(ContactsPage.viewButtonForEmail(email))
        );
    }
}
