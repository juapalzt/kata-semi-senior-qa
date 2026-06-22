package org.example.framework.ui.locators;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class ContactListLocators {

    private ContactListLocators() {}

    public static final Target CONTACTS_TABLE = Target.the("contacts table").located(By.cssSelector("table.contacts, table"));
    public static final Target ADD_CONTACT_BUTTON = Target.the("add contact button").located(By.id("add-contact"));
    public static final Target LOGOUT_BUTTON = Target.the("logout button").located(By.id("logout"));
}
