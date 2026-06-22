package org.example.framework.ui.locators;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class ContactDetailsLocators {

    private ContactDetailsLocators() {}

    public static final Target NAME = Target.the("contact name").located(By.id("contactName"));
    public static final Target EMAIL = Target.the("contact email").located(By.id("contactEmail"));
    public static final Target PHONE = Target.the("contact phone").located(By.id("contactPhone"));
    public static final Target ADDRESS = Target.the("contact address").located(By.id("contactAddress"));
    public static final Target EDIT_BUTTON = Target.the("edit contact button").located(By.id("edit-contact"));
    public static final Target DELETE_BUTTON = Target.the("delete contact button").located(By.id("delete"));
    public static final Target RETURN_BUTTON = Target.the("return to list button").located(By.id("return"));
}
