package org.example.framework.ui.locators;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class LoginLocators {

    private LoginLocators() {}

    public static final Target EMAIL = Target.the("login email field").located(By.id("email"));
    public static final Target PASSWORD = Target.the("login password field").located(By.id("password"));
    public static final Target SUBMIT = Target.the("login submit button").located(By.id("submit"));
    public static final Target SIGNUP_LINK = Target.the("signup link").located(By.id("signup"));
}
