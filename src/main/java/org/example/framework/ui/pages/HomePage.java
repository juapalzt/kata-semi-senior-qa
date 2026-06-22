package org.example.framework.ui.pages;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class HomePage {

    private HomePage() {}

    public static final Target DASHBOARD_TITLE = Target.the("dashboard title").located(By.id("dashboard-title"));
    public static final Target USER_NAME = Target.the("user name").located(By.id("user-name"));
    public static final Target LOGOUT_BUTTON = Target.the("logout button").located(By.id("logout"));
}
