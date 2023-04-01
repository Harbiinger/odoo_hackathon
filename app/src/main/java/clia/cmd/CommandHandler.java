package clia.cmd;

import clia.back.fs.Users;
import clia.front.actions.Action;
import clia.front.actions.Actions;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CommandHandler {
    /**
     * Handles the execution of the given command.
     * @param command The command to execute
     */
    public static Action handle(Command command) throws IOException, ParseException {

        switch (command.getCmd()) {
            case BROWSER -> {}
            case LS -> {
                return new Action(Actions.DISPLAY_DIRECTORY);
            }

            case OPEN -> {}
            case SU -> {
                if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS);
                if (command.getArgs().size() > 1) return new Action(Actions.ERROR_TOO_MANY_ARGS);
                if (Users.getUser(command.getArgs().get(0)) == null) return new Action(Actions.INVALID_USERNAME, command.getArgs());
                return new Action(Actions.CHANGE_USER, command.getArgs());
            }
            case WHOAMI -> {
                return new Action(Actions.DISPLAY_CURRENT_USER);
            }
            case CAT -> {
                if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS);
                // TODO : replace null by arraylist of new File(args[i])
                /*
                 ArrayList<File> files = new ArrayList<>();
                 for (String name : command.getArgs()) {
                     GET FILES FROM THEIR NAME (files.append())
                 }
                 */
                return new Action(Actions.DISPLAY_CONTENT_OF_FILE);
            }
            case MAIL -> {
                if (command.getArgs().size() == 0) {
                    return new Action(Actions.DISPLAY_MAIL_INFO);
                }
                return new Action(Actions.DISPLAY_MAIL_CONTENT, command.getArgs());
            }
            case NOT_FOUND -> {
                return new Action(Actions.ERROR_COMMAND_NOT_FOUND);
            }
            case SHUTDOWN -> {}
            case REBOOT -> {}
            case CLEAR -> {
                return new Action(Actions.CLEAR);
            }
        }
        return new Action(Actions.NONE);
    }
}
