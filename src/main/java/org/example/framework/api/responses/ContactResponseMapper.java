package org.example.framework.api.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.example.framework.domain.models.ContactResponse;

public final class ContactResponseMapper {

    private ContactResponseMapper() {}

    public static ContactResponse from(Response response) {
        if (response == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody().asString(), ContactResponse.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
