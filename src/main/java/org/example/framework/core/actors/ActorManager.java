package org.example.framework.core.actors;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.Ability;

/**
 * ActorManager defines lifecycle operations and conventions for actor reuse.
 *
 * Responsibilities:
 * - Initialize and teardown the Serenity Stage
 * - Provide helper methods to get the "main" actor or named actors
 * - Central place to avoid duplicate actor creation and to reset state between scenarios
 */
public final class ActorManager {

    private static final String DEFAULT_ACTOR = "Principal";
    private static volatile boolean initialized = false;

    private ActorManager() {}

    public static synchronized void initStage() {
        if (!initialized) {
            OnStage.setTheStage(new OnlineCast());
            initialized = true;
        }
    }

    public static Actor getMainActor() {
        initStage();
        return ActorFactory.getActor(DEFAULT_ACTOR);
    }

    public static Actor getActor(String name) {
        initStage();
        return ActorFactory.getActor(name);
    }

    public static Actor createAndConfigureActor(String name, java.util.function.Consumer<Actor> configurator) {
        initStage();
        return ActorFactory.createActor(name, configurator);
    }

    public static void giveAbilityTo(String actorName, Ability ability) {
        Actor actor = getActor(actorName);
        ActorFactory.giveAbility(actor, ability);
    }

    public static synchronized void clearAllActors() {
        try {
            OnStage.drawTheCurtain();
        } catch (Exception ignored) {}
        ActorFactory.clearAll();
        initialized = false;
    }
}
