package clia.front.events;

import javafx.event.Event;
import javafx.event.EventTarget;

import java.util.Collection;

/**
 * Defines an event manager with static methods to send events to single or multiple targets.
 * Encapsulates event management and sending.
 */
public class EventsManager {
    public static void sendEventTo(Event event, EventTarget target) {
        Event.fireEvent(target, event);
    }

    public static void sendEventToAll(Event event, EventTarget... targets) {
        for (EventTarget target : targets) {
            Event.fireEvent(target, event);
        }
    }

    public static void sendEventToAll(Event event, Collection<EventTarget> targets) {
        for (EventTarget target : targets) {
            Event.fireEvent(target, event);
        }
    }
}
