package org.example.tests.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "org.example.tests.stepdefinitions",
    plugin = {"pretty"},
    monochrome = true
)
public class RunnerTest {
    // Main Serenity + Cucumber runner for the project
}
