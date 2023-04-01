package clia.front.actions;

/**
 * Represents actions that must be done in the GUI. These actions can be result of back-end commands execution.
 * For example :
 *  - the user inputs 'clear'
 *  - the GUI sends the command to the analyzer
 *  - the analyzer returns an action 'Actions.CLEAR'
 *  - the GUI takes action because it received 'Actions.CLEAR'
 */
public enum Actions {
    NONE, CLEAR, ERROR_NOT_ENOUGH_ARGS, ERROR_COMMAND_NOT_FOUND, DISPLAY_CONTENT_OF_FILE, DISPLAY_DIRECTORY,
    DISPLAY_MAIL_INFO, DISPLAY_MAIL_CONTENT
}
