package org.example.framework.ui.pages;

import org.example.framework.ui.locators.AddContactLocators;
import net.serenitybdd.screenplay.targets.Target;

public final class AddContactPage {

    private AddContactPage() {}

    public static final Target FIRST_NAME = AddContactLocators.FIRST_NAME;
    public static final Target LAST_NAME = AddContactLocators.LAST_NAME;
    public static final Target BIRTHDATE = AddContactLocators.BIRTHDATE;
    public static final Target EMAIL = AddContactLocators.EMAIL;
    public static final Target PHONE = AddContactLocators.PHONE;
    public static final Target ADDRESS_LINE_ONE = AddContactLocators.STREET1;
    public static final Target ADDRESS_LINE_TWO = AddContactLocators.STREET2;
    public static final Target CITY = AddContactLocators.CITY;
    public static final Target STATE = AddContactLocators.STATE;
    public static final Target POSTAL_CODE = AddContactLocators.POSTAL;
    public static final Target COUNTRY = AddContactLocators.COUNTRY;
    public static final Target SAVE_CONTACT_BUTTON = AddContactLocators.SUBMIT;
}
