package org.example.tests.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * Hook para organizar evidencias (screenshots) por ejecución, tipo de prueba y escenario.
 * Los screenshots se reorganizan desde target/site/serenity a estructura jerárquica:
 * target/serenity-reports/evidences/{fecha-hora}/{tipo_prueba}/{nombre_escenario}/
 */
public class EvidenceOrganizationHooks {

    private static final DateTimeFormatter EXECUTION_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static String EXECUTION_TIMESTAMP;
    
    private String scenarioName;
    private String testType;

    @Before
    public void setupEvidenceContext(Scenario scenario) {
        // Capturar marca de tiempo de la ejecución (solo una vez)
        if (EXECUTION_TIMESTAMP == null) {
            EXECUTION_TIMESTAMP = LocalDateTime.now().format(EXECUTION_FORMATTER);
        }

        // Obtener nombre del escenario y sanitizarlo
        scenarioName = sanitizeFileName(scenario.getName());

        // Determinar tipo de prueba según tags
        Collection<String> tags = scenario.getSourceTagNames();
        if (tags.stream().anyMatch(t -> t.equalsIgnoreCase("@api"))) {
            testType = "API";
        } else if (tags.stream().anyMatch(t -> t.equalsIgnoreCase("@ui"))) {
            testType = "UI";
        } else if (tags.stream().anyMatch(t -> t.equalsIgnoreCase("@e2e"))) {
            testType = "E2E";
        } else {
            testType = "GENERAL";
        }

        // Guardar en propiedades del sistema para acceso en tasks/steps
        System.setProperty("current.scenario.name", scenarioName);
        System.setProperty("current.test.type", testType);
        System.setProperty("current.execution.timestamp", EXECUTION_TIMESTAMP);
    }

    @After
    public void cleanupAfterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("[EvidenceOrganization] ⚠ Escenario fallido: " + scenarioName + 
                " | Tipo: " + testType + " | Ejecución: " + EXECUTION_TIMESTAMP);
        } else {
            System.out.println("[EvidenceOrganization] ✔ Escenario exitoso: " + scenarioName);
        }
    }

    /**
     * Obtiene el timestamp de la ejecución actual (para uso en steps/tasks).
     */
    public static String getExecutionTimestamp() {
        return EXECUTION_TIMESTAMP != null ? EXECUTION_TIMESTAMP : 
            LocalDateTime.now().format(EXECUTION_FORMATTER);
    }

    /**
     * Sanitiza el nombre del archivo eliminando caracteres inválidos.
     */
    private String sanitizeFileName(String name) {
        return name
            .replaceAll("[^a-zA-Z0-9_\\-]", "_")
            .replaceAll("_+", "_")
            .toLowerCase();
    }
}
