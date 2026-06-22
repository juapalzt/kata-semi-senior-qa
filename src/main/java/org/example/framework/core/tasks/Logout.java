package org.example.framework.core.tasks;

import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Tasks;
import org.example.framework.ui.pages.HomePage;

public class Logout implements Task {

    public static Logout now() {
        return Tasks.instrumented(Logout.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(HomePage.LOGOUT_BUTTON)
        );
    }
}
