package org.example.framework.ui.pages;

import net.serenitybdd.screenplay.targets.Target;
import org.example.framework.ui.locators.ContactListLocators;

public class ContactsPage {

    public static final Target CONTACTS_TABLE = ContactListLocators.CONTACTS_TABLE;
    public static final Target ADD_CONTACT_BUTTON = ContactListLocators.ADD_CONTACT_BUTTON;

    public static Target contactRowForEmail(String email) {
        String xpath = String.format("//table[@id='contactsTable']//tr[td/text()='%s']", email);
        return Target.the("contact row for " + email).locatedBy(xpath);
    }

    public static Target viewButtonForEmail(String email) {
        String xpath = String.format("//table[@id='contactsTable']//tr[td/text()='%s']//button[contains(@id,'view') or contains(@class,'view') or contains(normalize-space(.),'View') or contains(normalize-space(.),'Ver')]", email);
        return Target.the("view button for " + email).locatedBy(xpath);
    }

    public static Target editButtonForEmail(String email) {
        String xpath = String.format("//table[@id='contactsTable']//tr[td/text()='%s']//button[contains(@id,'edit') or contains(@class,'edit') or contains(normalize-space(.),'Edit') or contains(normalize-space(.),'Editar')]", email);
        return Target.the("edit button for " + email).locatedBy(xpath);
    }

    public static Target deleteButtonForEmail(String email) {
        String xpath = String.format("//table[@id='contactsTable']//tr[td/text()='%s']//button[contains(@id,'delete') or contains(@class,'delete') or contains(normalize-space(.),'Delete') or contains(normalize-space(.),'Eliminar')]", email);
        return Target.the("delete button for " + email).locatedBy(xpath);
    }
}
