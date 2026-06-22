package org.example.framework.api.responses;

import io.restassured.response.Response;

public final class ResponseExtractors {

    private ResponseExtractors() {}

    public static String getToken(Response response) {
        if (response == null) {
            return null;
        }
        try {
            return response.path("token");
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String getContactId(Response response) {
        if (response == null) {
            return null;
        }
        try {
            return response.path("id");
        } catch (Exception ignored) {
            return null;
        }
    }

    public static <T> T getField(Response response, String jsonPath) {
        if (response == null || jsonPath == null) {
            return null;
        }
        try {
            return response.path(jsonPath);
        } catch (Exception ignored) {
            return null;
        }
    }
}
