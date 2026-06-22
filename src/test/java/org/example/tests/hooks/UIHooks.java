package org.example.tests.hooks;

import io.cucumber.java.Before;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class UIHooks {

    @Before
    public void clearBrowserState() {
        try {
            WebDriver driver = ThucydidesWebDriverSupport.getDriver();
            // Navegar a una página en blanco para tener acceso a JavaScript
            driver.navigate().to("about:blank");
            
            // Limpiar localStorage y sessionStorage
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.localStorage.clear();");
            js.executeScript("window.sessionStorage.clear();");
            
            // Limpiar cookies
            driver.manage().deleteAllCookies();
        } catch (Exception e) {
            // El driver aún no está inicializado, no hay problema
        }
    }
}
