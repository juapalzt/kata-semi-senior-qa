package org.example.framework.ui.locators;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class RegisterLocators {

    private RegisterLocators() {}

    public static final Target FIRST_NAME = Target.the("first name field").located(By.id("firstName"));
    public static final Target LAST_NAME = Target.the("last name field").located(By.id("lastName"));
    public static final Target EMAIL = Target.the("register email field").located(By.id("email"));
    public static final Target PASSWORD = Target.the("register password field").located(By.id("password"));
    public static final Target SUBMIT = Target.the("register submit button").located(By.id("submit"));
    public static final Target CANCEL = Target.the("register cancel button").located(By.id("cancel"));
}
