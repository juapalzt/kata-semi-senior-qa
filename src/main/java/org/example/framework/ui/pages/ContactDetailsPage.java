package org.example.framework.ui.pages;

import net.serenitybdd.screenplay.targets.Target;
import org.example.framework.ui.locators.ContactDetailsLocators;

public final class ContactDetailsPage {

    private ContactDetailsPage() {}

    public static final Target NAME = ContactDetailsLocators.NAME;
    public static final Target EMAIL = ContactDetailsLocators.EMAIL;
    public static final Target PHONE = ContactDetailsLocators.PHONE;
    public static final Target ADDRESS = ContactDetailsLocators.ADDRESS;
    public static final Target EDIT_BUTTON = ContactDetailsLocators.EDIT_BUTTON;
    public static final Target DELETE_BUTTON = ContactDetailsLocators.DELETE_BUTTON;
    public static final Target BACK_BUTTON = ContactDetailsLocators.RETURN_BUTTON;
}
