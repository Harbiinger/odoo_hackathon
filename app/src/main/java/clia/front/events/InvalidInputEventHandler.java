package clia.front.events;

import javafx.event.EventHandler;

/**
 * Defines an event handler for the {@link InvalidInputEvent}.
 *
 * Usage example :
 * [NODE].addEventHandler(InvalidInputEvent.[EVENT_NAME], event -> {
 *     [some code to execute when receiving the event]
 * });
 * where [NODE] is the targeted node and [EVENT_NAME] is the exact name of the event. [EVENT_NAME] must be a public,
 * static attribute of the {@link AbstractInvalidInputEvent} class.
 */
public abstract class InvalidInputEventHandler implements EventHandler<InvalidInputEvent> {
    @Override
    public void handle(InvalidInputEvent event) {
        event.invokeHandler(this);
    }

    public abstract void onEvent();
}
