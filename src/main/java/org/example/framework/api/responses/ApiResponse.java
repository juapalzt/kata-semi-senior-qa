package org.example.framework.api.responses;

import io.restassured.response.Response;

public class ApiResponse<T> {

    private final Response rawResponse;
    private final T body;

    public ApiResponse(Response rawResponse, T body) {
        this.rawResponse = rawResponse;
        this.body = body;
    }

    public Response getRawResponse() {
        return rawResponse;
    }

    public int getStatusCode() {
        return rawResponse.getStatusCode();
    }

    public String getBodyAsString() {
        return rawResponse.getBody().asString();
    }

    public <R> R asObject(Class<R> type) {
        return rawResponse.as(type);
    }

    public T getBody() {
        return body;
    }
}
