package org.example.framework.core.actors;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Lightweight ActorFactory that centralizes creation and basic configuration of Actors.
 *
 * - Creates actors via Serenity `OnStage` to keep integration with the Screenplay runtime.
 * - Keeps a registry to avoid accidental duplication when code bypasses `OnStage`.
 */
public final class ActorFactory {

    private static final Map<String, Actor> REGISTRY = new ConcurrentHashMap<>();

    private ActorFactory() {}

    public static void ensureStage() {
        try {
            OnStage.setTheStage(new OnlineCast());
        } catch (Exception ignored) {
            // idempotent
        }
    }

    public static Actor getActor(String name) {
        ensureStage();
        return REGISTRY.computeIfAbsent(name, n -> OnStage.theActorCalled(n));
    }

    public static Actor createActor(String name, Consumer<Actor> configurator) {
        ensureStage();
        return REGISTRY.computeIfAbsent(name, n -> {
            Actor actor = OnStage.theActorCalled(n);
            if (configurator != null) {
                configurator.accept(actor);
            }
            return actor;
        });
    }

    public static void registerActor(Actor actor) {
        if (actor != null) {
            REGISTRY.putIfAbsent(actor.getName(), actor);
        }
    }

    public static void clearActor(String name) {
        REGISTRY.remove(name);
    }

    public static void clearAll() {
        REGISTRY.clear();
    }

    public static void giveAbility(Actor actor, Ability ability) {
        if (actor != null && ability != null) {
            actor.can(ability);
        }
    }
}
