package org.example.framework.api.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.example.framework.domain.models.LoginResponse;

public final class LoginResponseMapper {

    private LoginResponseMapper() {}

    public static LoginResponse from(Response response) {
        if (response == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody().asString(), LoginResponse.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
