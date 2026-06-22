package org.example.framework.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Utility class for reusable API response validations.
 * <p>
 * Stateless helper methods for REST Assured responses that can be used directly
 * from Serenity Screenplay Tasks or API tests.
 */
public final class ApiValidations {

    private ApiValidations() {
        throw new UnsupportedOperationException("ApiValidations is a utility class and cannot be instantiated");
    }

    public static void validateStatusCode(Response response, int expected) {
        MatcherAssert.assertThat(
                "Expected HTTP status code to be " + expected + " but was " + response.getStatusCode(),
                response.getStatusCode(),
                equalTo(expected)
        );
    }

    public static void validateResponseTime(Response response, long maxMs) {
        long elapsed = response.getTime();
        MatcherAssert.assertThat(
                "Expected response time to be less than or equal to " + maxMs + " ms but was " + elapsed + " ms",
                elapsed,
                lessThanOrEqualTo(maxMs)
        );
    }

    public static void validateHeaderEquals(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        MatcherAssert.assertThat(
                "Expected header '" + headerName + "' to equal '" + expectedValue + "' but was '" + actualValue + "'",
                actualValue,
                equalTo(expectedValue)
        );
    }

    public static void validateHeaderContains(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        MatcherAssert.assertThat(
                "Expected header '" + headerName + "' to contain '" + expectedValue + "' but was '" + actualValue + "'",
                actualValue,
                containsString(expectedValue)
        );
    }

    public static void validateFieldNotNull(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to be present and not null",
                value,
                notNullValue()
        );
    }

    public static void validateFieldEquals(Response response, String jsonPath, Object expected) {
        Object actual = response.jsonPath().get(jsonPath);
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to equal '" + expected + "' but was '" + actual + "'",
                actual,
                equalTo(expected)
        );
    }

    public static void validateFieldContains(Response response, String jsonPath, String expected) {
        String actual = response.jsonPath().getString(jsonPath);
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to contain '" + expected + "' but was '" + actual + "'",
                actual,
                containsString(expected)
        );
    }

    public static void validateFieldType(Response response, String jsonPath, Class<?> expectedType) {
        Object value = response.jsonPath().get(jsonPath);
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to be of type '" + expectedType.getSimpleName() + "' but was '" + (value == null ? "null" : value.getClass().getSimpleName()) + "'",
                value,
                notNullValue()
        );
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to be of type '" + expectedType.getSimpleName() + "'",
                value.getClass().getName(),
                equalTo(expectedType.getName())
        );
    }

    public static void validateFieldNotBlank(Response response, String jsonPath) {
        String value = response.jsonPath().getString(jsonPath);
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to be present and not blank",
                value,
                notNullValue()
        );
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to be non-empty",
                value.trim().isEmpty(),
                equalTo(false)
        );
    }

    public static void validateJwtFormat(String token) {
        MatcherAssert.assertThat("Expected JWT token to be present", token, notNullValue());
        MatcherAssert.assertThat("Expected JWT token to be non-empty", token.trim().isEmpty(), equalTo(false));
        MatcherAssert.assertThat(
                "Expected JWT token to contain three segments separated by dots",
                token,
                matchesPattern("^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$")
        );
    }

    public static void validateResponseHasJsonContentType(Response response) {
        validateHeaderContains(response, "Content-Type", "application/json");
    }

    public static void validateNoPasswordField(Response response) {
        String body = response.getBody().asString();
        MatcherAssert.assertThat(
                "Response body should not expose password fields",
                body.toLowerCase(),
                not(containsString("password"))
        );
    }

    public static void validateNoStackTrace(Response response) {
        String body = response.getBody().asString();
        MatcherAssert.assertThat(
                "Response body should not expose stack traces",
                body,
                not(containsString("Exception"))
        );
        MatcherAssert.assertThat(
                "Response body should not expose stack traces",
                body,
                not(containsString("at "))
        );
    }

    public static void validateJsonDateFormat(Response response, String jsonPath, String datePattern) {
        String dateValue = response.jsonPath().getString(jsonPath);
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to be present and not null",
                dateValue,
                notNullValue()
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        MatcherAssert.assertThat(
                "Expected JSON field '" + jsonPath + "' to match date format " + datePattern,
                LocalDate.parse(dateValue, formatter),
                notNullValue()
        );
    }

    public static void validateJsonFieldsExactly(Response response, Set<String> expectedFields) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, ?> bodyMap = mapper.readValue(response.getBody().asString(), Map.class);
            MatcherAssert.assertThat(
                    "Expected JSON object to contain exactly the expected fields",
                    bodyMap.keySet(),
                    equalTo(expectedFields)
            );
        } catch (Exception e) {
            throw new AssertionError("Failed to parse response body as JSON for exact-field validation", e);
        }
    }

    public static void validateResponseContainsBearerAuthorization(Response response) {
        validateHeaderContains(response, "Authorization", "Bearer ");
    }

    public static void validateBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        MatcherAssert.assertThat(
                "Expected response body to be non-empty",
                body == null ? "" : body.trim(),
                not(equalTo(""))
        );
    }

    public static void validateJsonSchema(Response response, String schemaPath) {
        Objects.requireNonNull(schemaPath, "Schema path must not be null");
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
    }

    public static void validateSuccessResponse(Response response) {
        validateStatusCode(response, 200);
    }

    public static void validateCreatedResponse(Response response) {
        validateStatusCode(response, 201);
    }

    public static void validateErrorResponse(Response response) {
        int statusCode = response.getStatusCode();
        MatcherAssert.assertThat(
                "Expected HTTP error response (4xx or 5xx) but was " + statusCode,
                statusCode,
                greaterThanOrEqualTo(400)
        );
    }
}
