package clia.front.events;

/**
 * Defines an event handler for the {@link Event}.
 *
 * Usage example :
 * [NODE].addEventHandler(InvalidInputEvent.[EVENT_NAME], event -> {
 *     [some code to execute when receiving the event]
 * });
 * where [NODE] is the targeted node and [EVENT_NAME] is the exact name of the event. [EVENT_NAME] must be a public,
 * static attribute of the {@link AbstractEvent} class.
 */
public abstract class EventHandler implements javafx.event.EventHandler<Event> {
    @Override
    public void handle(Event event) {
        event.invokeHandler(this);
    }

    public abstract void onEvent();
}
