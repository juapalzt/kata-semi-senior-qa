package org.example.framework.api.clients;

/**
 * Base class for API clients. Concrete clients should extend this to provide typed operations.
 */
public abstract class ApiClient {

    protected final String baseUrl;

    protected ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
