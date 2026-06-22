package org.example.framework.ui.locators;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class AddContactLocators {

    private AddContactLocators() {}

    public static final Target FIRST_NAME = Target.the("first name field").located(By.id("firstName"));
    public static final Target LAST_NAME = Target.the("last name field").located(By.id("lastName"));
    public static final Target BIRTHDATE = Target.the("birthdate field").located(By.id("birthdate"));
    public static final Target EMAIL = Target.the("email field").located(By.id("email"));
    public static final Target PHONE = Target.the("phone field").located(By.id("phone"));
    public static final Target STREET1 = Target.the("street1 field").located(By.id("street1"));
    public static final Target STREET2 = Target.the("street2 field").located(By.id("street2"));
    public static final Target CITY = Target.the("city field").located(By.id("city"));
    public static final Target STATE = Target.the("state/province field").located(By.id("stateProvince"));
    public static final Target POSTAL = Target.the("postal code field").located(By.id("postalCode"));
    public static final Target COUNTRY = Target.the("country field").located(By.id("country"));
    public static final Target SUBMIT = Target.the("submit button").located(By.id("submit"));
    public static final Target CANCEL = Target.the("cancel button").located(By.id("cancel"));
}
