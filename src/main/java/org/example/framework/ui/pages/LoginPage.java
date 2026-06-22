package org.example.framework.ui.pages;

import net.serenitybdd.screenplay.targets.Target;
import org.example.framework.ui.locators.LoginLocators;

public class LoginPage {

    private LoginPage() {}

    public static final Target EMAIL = LoginLocators.EMAIL;
    public static final Target PASSWORD = LoginLocators.PASSWORD;
    public static final Target LOGIN_BUTTON = LoginLocators.SUBMIT;
    public static final Target SIGNUP_LINK = LoginLocators.SIGNUP_LINK;
}
