package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import org.example.framework.ui.components.ContactFormComponent;
import org.example.framework.domain.models.ContactRequest;
import net.serenitybdd.screenplay.actions.Enter;
import org.example.framework.ui.pages.AddContactPage;
import org.example.framework.ui.pages.ContactsPage;

public class AddContact implements Task {

    private final String firstName;
    private final String lastName;
    private final String birthdate;
    private final String email;
    private final String phone;
    private final String address1;
    private final String address2;
    private final String city;
    private final String state;
    private final String postalCode;
    private final String country;

    public AddContact(String firstName, String lastName, String birthdate, String email, String phone,
                      String address1, String address2, String city, String state,
                      String postalCode, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public static AddContact withData(String firstName, String lastName, String birthdate, String email, String phone,
                                      String address1, String address2, String city, String state,
                                      String postalCode, String country) {
        return Tasks.instrumented(AddContact.class, firstName, lastName, birthdate, email, phone,
                address1, address2, city, state, postalCode, country);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(ContactsPage.ADD_CONTACT_BUTTON)
        );

        // Use the reusable ContactFormComponent to fill and submit the basic fields
        ContactRequest req = ContactRequest.builder()
            .name((firstName == null ? "" : firstName) + (lastName == null ? "" : " " + lastName)).email(email).phone(phone).build();

        actor.attemptsTo(
            ContactFormComponent.fill(req),
            ContactFormComponent.submit()
        );
    }
}
