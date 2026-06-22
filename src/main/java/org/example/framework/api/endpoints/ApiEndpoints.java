package org.example.framework.api.endpoints;

/**
 * Centraliza las rutas/paths de los endpoints del API.
 */
public final class ApiEndpoints {

    private ApiEndpoints() {
        // util class
    }

    public static final String AUTH_LOGIN = "/api/users/login";
    public static final String USERS = "/api/users";
    public static final String CONTACTS = "/api/contacts";
}
