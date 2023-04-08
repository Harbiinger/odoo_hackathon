package clia.front.events;

/**
 * Defines an event corresponding to invalid input. Receiving this event means that some user input was sent
 * (i.e. the user entered some input and clicked a "send", "confirm", "ok", etc... button) but didn't match certain
 * formats, requirements, values, etc... A node that would be targeted by this event is a node that should be updated or
 * have a certain behavior when the user input that caused the trigger of the event is invalid.
 *
 * For example :
 * You define a text field where the user must enter three letters. You have a "confirm" button and some text saying
 * that the user input is invalid (four letters long, containing number, etc...). When the user clicks the confirm
 * button, and you detect that the input is invalid, you will send an InvalidUserInput event to the text message.
 *
 * Note that the node receiving the event must have an appropriate event handler for that type of event. In the above
 * example, an event handling response could be to set the visibility of the text message to true, in order to show
 * the text to the user indicating that his input was invalid.
 */
public class Event extends AbstractEvent {
    public Event() {
        super(AbstractEvent.INVALID_VALUE);
    }

    @Override
    public void invokeHandler(EventHandler handler) {
        handler.onEvent();
    }
}