package clia.front.events;

import javafx.event.EventType;

/**
 * Defines an abstract skeleton for the {@link Event} class.
 */
public abstract class AbstractEvent extends javafx.event.Event {
    public static final EventType<AbstractEvent> ANY =
            new EventType<>(javafx.event.Event.ANY, "ANY");

    public static final EventType<AbstractEvent> INVALID_VALUE =
            new EventType<>(AbstractEvent.ANY, "INVALID_VALUE");

    public AbstractEvent(EventType<? extends javafx.event.Event> eventType) {
        super(eventType);
    }

    public abstract void invokeHandler(EventHandler handler);
}
