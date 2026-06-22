package org.example.framework.core.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Actor;
import io.restassured.response.Response;

/**
 * Question que valida que la última respuesta contenga un token y el código 200.
 */
public class TokenQuestion implements Question<Boolean> {

    public static TokenQuestion isValid() {
        return new TokenQuestion();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            Response resp = actor.recall("lastResponse");
            if (resp == null) {
                return false;
            }

            int status = resp.getStatusCode();
            String token = resp.jsonPath().getString("token");

            return status == 200 && token != null && !token.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
