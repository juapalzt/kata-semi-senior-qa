package org.example.framework.core.exceptions;

public class AuthenticationException extends ApiException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
