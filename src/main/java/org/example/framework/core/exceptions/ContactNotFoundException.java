package org.example.framework.core.exceptions;

public class ContactNotFoundException extends ApiException {

    public ContactNotFoundException(String message) {
        super(message);
    }

    public ContactNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
