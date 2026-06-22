package org.example.framework.api.requests;

import org.example.framework.domain.models.LoginRequest;

public final class UserRequestBuilder {

    private String username;
    private String password;
    private String email;

    private UserRequestBuilder() {}

    public static UserRequestBuilder aUserRequest() {
        return new UserRequestBuilder();
    }

    public UserRequestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginRequest buildLoginRequest() {
        return LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
    }
}
