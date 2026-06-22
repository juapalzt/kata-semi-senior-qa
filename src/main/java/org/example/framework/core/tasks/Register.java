package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Tasks;
import org.example.framework.ui.pages.RegisterPage;

public class Register implements Task {

    private final String firstName;
    private final String email;
    private final String password;

    public Register(String firstName, String email, String password) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public static Register withData(String firstName, String email, String password) {
        return Tasks.instrumented(Register.class, firstName, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(firstName).into(RegisterPage.FIRST_NAME),
                Enter.theValue(firstName).into(RegisterPage.LAST_NAME),
                Enter.theValue(email).into(RegisterPage.EMAIL),
                Enter.theValue(password).into(RegisterPage.PASSWORD),
                Click.on(RegisterPage.REGISTER_BUTTON)
        );
    }
}
