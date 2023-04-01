package clia.front.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Defines an abstract skeleton for the {@link InvalidInputEvent} class.
 */
public abstract class AbstractInvalidInputEvent extends Event {
    public static final EventType<AbstractInvalidInputEvent> ANY =
            new EventType<>(Event.ANY, "ANY");

    public static final EventType<AbstractInvalidInputEvent> INVALID_VALUE =
            new EventType<>(AbstractInvalidInputEvent.ANY, "INVALID_VALUE");

    public AbstractInvalidInputEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public abstract void invokeHandler(InvalidInputEventHandler handler);
}
