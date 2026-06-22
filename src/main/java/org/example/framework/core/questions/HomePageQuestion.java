package org.example.framework.core.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Actor;
import org.example.framework.ui.pages.ContactsPage;

/**
 * Question to validate that the user is authenticated by checking the contact list page.
 */
public class HomePageQuestion implements Question<Boolean> {

    public static HomePageQuestion isAuthenticated() {
        return new HomePageQuestion();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return ContactsPage.ADD_CONTACT_BUTTON.resolveFor(actor).isVisible()
                    || ContactsPage.CONTACTS_TABLE.resolveFor(actor).isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
