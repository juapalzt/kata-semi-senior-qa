package org.example.framework.core.abilities;

import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import io.restassured.specification.RequestSpecification;

/**
 * Ability wrapper to use Serenity Screenplay REST `CallAnApi` with token reuse support.
 */
public class CallApiAbility implements Ability {

    private final CallAnApi delegate;
    private String authToken;

    private CallApiAbility(CallAnApi delegate) {
        this.delegate = delegate;
    }

    public static CallApiAbility at(String baseUrl) {
        return new CallApiAbility(CallAnApi.at(baseUrl));
    }

    public CallApiAbility withToken(String token) {
        this.authToken = token;
        return this;
    }

    public String getToken() {
        return authToken;
    }

    public CallAnApi getDelegate() {
        return delegate;
    }

    /**
     * Enrich a given RequestSpecification with the stored auth token (Bearer) when present.
     */
    public RequestSpecification enrich(RequestSpecification request) {
        if (authToken != null && !authToken.isEmpty()) {
            request.header("Authorization", "Bearer " + authToken);
        }
        return request;
    }

    /**
     * Convenience: obtain this ability from an actor.
     */
    public static CallApiAbility as(Actor actor) {
        return actor.abilityTo(CallApiAbility.class);
    }
}
