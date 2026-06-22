package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import org.example.framework.ui.pages.LoginPage;

/**
 * UI Task to perform login on the web page.
 * Usage: actor.attemptsTo(Login.withCredentials(email, password));
 */
public class Login implements Task {

    private final String email;
    private final String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Login withCredentials(String email, String password) {
        return Tasks.instrumented(Login.class, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(email).into(LoginPage.EMAIL),
                Enter.theValue(password).into(LoginPage.PASSWORD),
                Click.on(LoginPage.LOGIN_BUTTON)
        );
    }
}
