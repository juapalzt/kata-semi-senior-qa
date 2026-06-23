package org.example.framework.shared.utils;

import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.Actor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilería para capturar y guardar screenshots de forma organizada.
 * Las evidencias se guardan en: target/serenity-reports/evidences/{ejecución}/{tipo}/{escenario}/
 */
public class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("HHmmss_SSS");

    /**
     * Captura un screenshot y lo guarda en el directorio de evidencias del escenario actual.
     * @param actor Actor que contiene la capacidad BrowseTheWeb
     * @param stepName Nombre del paso o descripción del screenshot
     * @return Ruta del archivo guardado
     */
    public static String captureScreenshot(Actor actor, String stepName) {
        try {
            String evidenceDir = getScenarioEvidenceDirectory();
            Files.createDirectories(Paths.get(evidenceDir));

            WebDriver driver = BrowseTheWeb.as(actor).getDriver();
            if (driver instanceof TakesScreenshot) {
                // Wait 1 second before taking evidence screenshots so the UI tiene tiempo de estabilizar.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
                byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);

                String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
                String fileName = sanitizeFileName(stepName) + "_" + timestamp + ".png";
                String filePath = Paths.get(evidenceDir, fileName).toString();

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(screenshotBytes);
                }

                System.out.println("[Screenshot] Guardado: " + filePath);
                return filePath;
            }
        } catch (IOException e) {
            System.err.println("[Screenshot] Error capturando screenshot: " + e.getMessage());
        }
        return null;
    }

    /**
     * Captura un screenshot en caso de error en un paso.
     * @param actor Actor que contiene el driver
     * @param stepName Nombre del paso donde falló
     * @param error Excepción que causó el error
     */
    public static void captureErrorScreenshot(Actor actor, String stepName, Throwable error) {
        try {
            String evidenceDir = getScenarioEvidenceDirectory();
            Files.createDirectories(Paths.get(evidenceDir));

            WebDriver driver = BrowseTheWeb.as(actor).getDriver();
            if (driver instanceof TakesScreenshot) {
                // Wait 1 second before taking error screenshots so the UI tiene tiempo de estabilizar.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
                byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);

                String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
                String fileName = "ERROR_" + sanitizeFileName(stepName) + "_" + timestamp + ".png";
                String filePath = Paths.get(evidenceDir, fileName).toString();

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(screenshotBytes);
                }

                System.out.println("[Screenshot-Error] Guardado: " + filePath);
                System.out.println("[Screenshot-Error] Causa: " + error.getMessage());
            }
        } catch (IOException e) {
            System.err.println("[Screenshot-Error] Error capturando screenshot: " + e.getMessage());
        }
    }

    /**
     * Obtiene la ruta del directorio de evidencias del escenario actual.
     */
    public static String getEvidenceDirectory() {
        return getScenarioEvidenceDirectory();
    }

    private static String getScenarioEvidenceDirectory() {
        String baseEvidenceDir = System.getProperty("evidence.directory", "target/serenity-reports/evidences");
        String executionTimestamp = System.getProperty("current.execution.timestamp");
        String testType = System.getProperty("current.test.type");
        String scenarioName = System.getProperty("current.scenario.name");

        if (executionTimestamp != null && !executionTimestamp.isBlank()
                && testType != null && !testType.isBlank()
                && scenarioName != null && !scenarioName.isBlank()) {
            return Paths.get(baseEvidenceDir, executionTimestamp, testType, scenarioName).toString();
        }
        return baseEvidenceDir;
    }

    /**
     * Sanitiza nombres para usarlos como nombres de archivo.
     */
    private static String sanitizeFileName(String name) {
        return name
            .replaceAll("[^a-zA-Z0-9_\\-]", "_")
            .replaceAll("_+", "_")
            .toLowerCase();
    }
}
