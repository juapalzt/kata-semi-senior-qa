package org.example.tests.runners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Post-procesador de reportes de Serenity que reorganiza screenshots en estructura jerárquica.
 * Lee los JSON reports de Serenity y reorganiza los PNG asociados en:
 * target/serenity-reports/evidences/{fecha-hora}/{tipo}/{escenario}/
 * 
 * Uso: java -cp ... org.example.tests.runners.SerenityReportOrganizer
 */
public class SerenityReportOrganizer {

    private static final String SERENITY_REPORTS_PATH = "target/site/serenity";
    private static final String EVIDENCE_BASE_PATH = "target/serenity-reports/evidences";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private static final Map<String, String> FEATURE_TO_TYPE = new HashMap<>();
    static {
        FEATURE_TO_TYPE.put("_api_", "API");
        FEATURE_TO_TYPE.put("_login", "UI");
        FEATURE_TO_TYPE.put("_register", "UI");
        FEATURE_TO_TYPE.put("_dashboard", "UI");
        FEATURE_TO_TYPE.put("_contacts", "UI");
        FEATURE_TO_TYPE.put("_e2e", "E2E");
    }

    public static void main(String[] args) {
        System.out.println("[SerenityReportOrganizer] Iniciando reorganización de evidencias...");
        
        try {
            String executionTimestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            File serenityDir = new File(SERENITY_REPORTS_PATH);
            
            if (!serenityDir.exists()) {
                System.err.println("[SerenityReportOrganizer] ERROR: No se encontró " + SERENITY_REPORTS_PATH);
                return;
            }

            // Leer todos los archivos JSON de reportes
            File[] jsonFiles = serenityDir.listFiles((dir, name) -> name.endsWith(".json"));
            
            if (jsonFiles == null || jsonFiles.length == 0) {
                System.out.println("[SerenityReportOrganizer] No se encontraron reportes JSON");
                return;
            }

            System.out.println("[SerenityReportOrganizer] Encontrados " + jsonFiles.length + " reportes");

            for (File jsonFile : jsonFiles) {
                processReport(jsonFile, executionTimestamp);
            }

            System.out.println("[SerenityReportOrganizer] ✔ Reorganización completada");
            System.out.println("[SerenityReportOrganizer] Evidencias en: " + EVIDENCE_BASE_PATH);

        } catch (Exception e) {
            System.err.println("[SerenityReportOrganizer] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Procesa un archivo JSON de reporte y reorganiza sus screenshots.
     */
    private static void processReport(File jsonFile, String executionTimestamp) throws IOException {
        String jsonContent = new String(Files.readAllBytes(jsonFile.toPath()));
        
        // Extraer información del nombre del archivo JSON
        String fileName = jsonFile.getName();
        String testType = determineTestType(fileName);
        String scenarioName = extractScenarioName(fileName);

        // Crear directorio de evidencias para este escenario
        String evidenceDir = String.format(
            "%s/%s/%s/%s",
            EVIDENCE_BASE_PATH,
            executionTimestamp,
            testType,
            scenarioName
        );

        Path evidencePath = Files.createDirectories(Paths.get(evidenceDir));

        // Buscar referencias a imágenes en el JSON
        List<String> imageHashes = extractImageHashes(jsonContent);

        System.out.println("[SerenityReportOrganizer] " + scenarioName + " (" + testType + ") - " + 
            imageHashes.size() + " screenshots");

        // Copiar imágenes al directorio de evidencias
        for (String hash : imageHashes) {
            File sourceImage = new File(SERENITY_REPORTS_PATH, hash + ".png");
            if (sourceImage.exists()) {
                Path destImage = evidencePath.resolve(hash + ".png");
                Files.copy(sourceImage.toPath(), destImage);
                System.out.println("  └─ Copiado: " + destImage.getFileName());
            }
        }
    }

    /**
     * Determina el tipo de prueba basado en el nombre del archivo JSON.
     */
    private static String determineTestType(String fileName) {
        for (Map.Entry<String, String> entry : FEATURE_TO_TYPE.entrySet()) {
            if (fileName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "GENERAL";
    }

    /**
     * Extrae el nombre del escenario del nombre del archivo JSON de Serenity.
     * Formato típico: {hash}_{tipo}_{nombre_archivo_feature}.json
     */
    private static String extractScenarioName(String fileName) {
        // Eliminar extensión .json
        String withoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        
        // El nombre del escenario está entre el último guion bajo y el final
        // Ej: 11708c59c265f519925f0d78e56258ae_omma_windows_ui_sl_contacts_ui
        String[] parts = withoutExt.split("_sl_");
        if (parts.length > 1) {
            return parts[1]
                .replaceAll("_", " ")
                .replaceAll("windows", "")
                .trim();
        }
        
        return withoutExt
            .replaceAll("[^a-zA-Z0-9 ]", " ")
            .replaceAll("\\s+", " ")
            .trim()
            .replaceAll(" ", "_")
            .toLowerCase();
    }

    /**
     * Extrae los hashes de imágenes del contenido JSON.
     * Busca referencias como: "screenshot": "abc123def456.png"
     */
    private static List<String> extractImageHashes(String jsonContent) {
        List<String> hashes = new ArrayList<>();
        
        // Patrón simple: buscar hashes que parecen ser nombres de archivo PNG
        // Formato: cadena alfanumérica de 64 caracteres (hash SHA256 típico de Serenity)
        String[] parts = jsonContent.split("\"");
        for (String part : parts) {
            if (part.matches("[a-f0-9]{64}")) {
                hashes.add(part);
            }
        }
        
        return hashes.stream().distinct().collect(Collectors.toList());
    }
}
