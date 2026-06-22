package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import org.example.framework.ui.pages.ContactsPage;

public class DeleteContact implements Task {

    private final String email;

    public DeleteContact(String email) {
        this.email = email;
    }

    public static DeleteContact withEmail(String email) {
        return Tasks.instrumented(DeleteContact.class, email);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(ContactsPage.deleteButtonForEmail(email))
        );
    }
}
