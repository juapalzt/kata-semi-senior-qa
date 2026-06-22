package org.example.framework.api.requests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public final class ApiRequestExecutor {

    private ApiRequestExecutor() {}

    public static Response post(String url, Object payload, Map<String, String> headers) {
        RequestSpecification request = RestAssured.given();
        attachHeaders(request, headers);
        return request.contentType("application/json").body(payload).post(url).andReturn();
    }

    public static Response get(String url, Map<String, String> headers) {
        RequestSpecification request = RestAssured.given();
        attachHeaders(request, headers);
        return request.get(url).andReturn();
    }

    public static Response put(String url, Object payload, Map<String, String> headers) {
        RequestSpecification request = RestAssured.given();
        attachHeaders(request, headers);
        return request.contentType("application/json").body(payload).put(url).andReturn();
    }

    public static Response delete(String url, Map<String, String> headers) {
        RequestSpecification request = RestAssured.given();
        attachHeaders(request, headers);
        return request.delete(url).andReturn();
    }

    private static void attachHeaders(RequestSpecification request, Map<String, String> headers) {
        if (headers != null) {
            request.headers(headers);
        }
    }
}
