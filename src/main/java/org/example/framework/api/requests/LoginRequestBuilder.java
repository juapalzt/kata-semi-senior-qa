package org.example.framework.api.requests;

import org.example.framework.domain.models.LoginRequest;
import java.util.HashMap;
import java.util.Map;

public final class LoginRequestBuilder {

    private String username;
    private String password;
    private final Map<String, String> headers = new HashMap<>();
    private String authToken;

    private LoginRequestBuilder() {}

    public static LoginRequestBuilder aLoginRequest() {
        return new LoginRequestBuilder();
    }

    public LoginRequestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginRequestBuilder withHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public LoginRequestBuilder withAuthToken(String token) {
        this.authToken = token;
        return this;
    }

    public LoginRequest buildLoginRequest() {
        return LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
    }

    public Map<String, String> buildHeaders() {
        Map<String, String> combined = new HashMap<>(headers);
        combined.put("Content-Type", "application/json");
        if (authToken != null && !authToken.isEmpty()) {
            combined.put("Authorization", "Bearer " + authToken);
        }
        return combined;
    }
}
