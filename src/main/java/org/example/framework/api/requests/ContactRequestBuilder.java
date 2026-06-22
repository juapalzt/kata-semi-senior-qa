package org.example.framework.api.requests;

import org.example.framework.domain.models.ContactRequest;

public final class ContactRequestBuilder {

    private String name;
    private String email;
    private String phone;

    private ContactRequestBuilder() {}

    public static ContactRequestBuilder aContactRequest() {
        return new ContactRequestBuilder();
    }

    public ContactRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ContactRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ContactRequestBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public ContactRequest buildCreateContactRequest() {
        return ContactRequest.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();
    }
}
