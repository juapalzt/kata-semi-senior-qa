package org.example.framework.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ApiLogger {

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void logRequestResponse(String endpoint, Object requestBody, Response response, String scenarioName) {
        try {
            String execTs = System.getProperty("current.execution.timestamp");
            if (execTs == null || execTs.isEmpty()) {
                execTs = LocalDateTime.now().format(TS);
            }

            String testType = System.getProperty("current.test.type", "API");
            String sanitizedScenario = (scenarioName != null && !scenarioName.isEmpty()) ? scenarioName : System.getProperty("current.scenario.name", "scenario");

            Path baseDir = Paths.get(System.getProperty("user.dir"), "target", "serenity-reports", "evidences", execTs, testType, sanitizedScenario);
            Files.createDirectories(baseDir);

            Path out = baseDir.resolve(sanitizedScenario + ".txt");

            Map<String, Object> toWrite = new HashMap<>();
            toWrite.put("requestEndpoint", endpoint);
            toWrite.put("request", requestBody);
            if (response != null) {
                toWrite.put("statusCode", response.getStatusCode());
                try {
                    String body = response.getBody().asString();
                    // Try to parse JSON else store raw
                    try {
                        Object parsed = MAPPER.readValue(body, Object.class);
                        toWrite.put("response", parsed);
                    } catch (Exception e) {
                        toWrite.put("response", body);
                    }
                } catch (Exception e) {
                    toWrite.put("response", "<no-response-body>");
                }
            }

            // Optionally include auth token if set
            String token = System.getProperty("auth.token");
            if (token != null && !token.isEmpty()) {
                toWrite.put("authorization", "Bearer " + token);
            }

            String pretty = MAPPER.writeValueAsString(toWrite);
            Files.writeString(out, pretty);

        } catch (IOException e) {
            System.err.println("[ApiLogger] Failed to write request/response: " + e.getMessage());
        }
    }
}
