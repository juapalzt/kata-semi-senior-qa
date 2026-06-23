package org.example.framework.ui.components;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import org.example.framework.domain.models.ContactRequest;
import org.example.framework.ui.locators.AddContactLocators;

public final class ContactFormComponent {

    private ContactFormComponent() {}

    public static Task fill(ContactRequest request) {
        return Tasks.instrumented(FillContact.class, request);
    }

    public static Task submit() {
        return Tasks.instrumented(SubmitContact.class);
    }

    public static class FillContact implements Task {
        private final ContactRequest request;

        public FillContact(ContactRequest request) {
            this.request = request;
        }

        @Override
        public <T extends Actor> void performAs(T actor) {
            String name = request.getName() == null ? "" : request.getName();
            String email = request.getEmail() == null ? "" : request.getEmail();
            String phone = request.getPhone() == null ? "" : request.getPhone();

            actor.attemptsTo(
                    Enter.theValue(name).into(AddContactLocators.FIRST_NAME),
                    Enter.theValue(email).into(AddContactLocators.EMAIL),
                    Enter.theValue(phone).into(AddContactLocators.PHONE)
            );
        }
    }

    public static class SubmitContact implements Task {
        public SubmitContact() {}

        @Override
        public <T extends Actor> void performAs(T actor) {
            actor.attemptsTo(Click.on(AddContactLocators.SUBMIT));
        }
    }
}
