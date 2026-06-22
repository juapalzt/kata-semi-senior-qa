package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import org.example.framework.ui.pages.ContactsPage;
import org.example.framework.ui.pages.EditContactPage;

public class EditContact implements Task {

    private final String email;
    private final String newFirstName;
    private final String newLastName;

    public EditContact(String email, String newFirstName, String newLastName) {
        this.email = email;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
    }

    public static EditContact withEmail(String email, String newFirstName, String newLastName) {
        return Tasks.instrumented(EditContact.class, email, newFirstName, newLastName);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(ContactsPage.editButtonForEmail(email)),
                Enter.theValue(newFirstName).into(EditContactPage.FIRST_NAME),
                Enter.theValue(newLastName).into(EditContactPage.LAST_NAME),
                Click.on(EditContactPage.SAVE_CONTACT_BUTTON)
        );
    }
}
