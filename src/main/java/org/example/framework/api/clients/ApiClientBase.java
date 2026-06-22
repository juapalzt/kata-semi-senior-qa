package org.example.framework.api.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Cliente HTTP reutilizable basado en Rest Assured.
 * Soporta GET, POST, PUT y DELETE y serializa objetos a JSON usando Jackson.
 */
public class ApiClientBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientBase.class);

    private final String baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiClientBase() {
        this(System.getProperty("api.base.url", "https://thinking-tester-contact-list.herokuapp.com"));
    }

    public ApiClientBase(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private RequestSpecification given(Map<String, ?> headers, Map<String, ?> queryParams) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .relaxedHTTPSValidation()
                .contentType("application/json");

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(req::header);
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            req.queryParams(queryParams);
        }

        return req;
    }

    public Response get(String path) {
        return get(path, null, null);
    }

    public Response get(String path, Map<String, ?> headers, Map<String, ?> queryParams) {
        return given(headers, queryParams)
                .when()
                .get(path)
                .andReturn();
    }

    public Response post(String path, Object body) {
        return post(path, body, null);
    }

    public Response post(String path, Object body, Map<String, ?> headers) {
        try {
            String payload = body instanceof String ? (String) body : objectMapper.writeValueAsString(body);
            return given(headers, null)
                    .body(payload)
                    .when()
                    .post(path)
                    .andReturn();
        } catch (Exception e) {
            LOGGER.error("Error serializing POST body", e);
            throw new RuntimeException(e);
        }
    }

    public Response put(String path, Object body) {
        return put(path, body, null);
    }

    public Response put(String path, Object body, Map<String, ?> headers) {
        try {
            String payload = body instanceof String ? (String) body : objectMapper.writeValueAsString(body);
            return given(headers, null)
                    .body(payload)
                    .when()
                    .put(path)
                    .andReturn();
        } catch (Exception e) {
            LOGGER.error("Error serializing PUT body", e);
            throw new RuntimeException(e);
        }
    }

    public Response delete(String path) {
        return delete(path, null);
    }

    public Response delete(String path, Map<String, ?> headers) {
        return given(headers, null)
                .when()
                .delete(path)
                .andReturn();
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
