package clia.cmd;

import clia.back.fs.Users;
import clia.front.actions.Action;
import clia.front.actions.Actions;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class CommandHandler {
    /**
     * Handles the execution of the given command.
     * @param command The command to execute
     */
    public static Action handle(Command command) {
        switch (command.getCmd()) {
            case BROWSER -> {}
            case LS -> {
                return new Action(Actions.DISPLAY_DIRECTORY, command.isRoot());
            }
            case OPEN -> {}
            case SU -> {
                if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
                if (command.getArgs().size() > 1) return new Action(Actions.ERROR_TOO_MANY_ARGS, command.isRoot());
                if (Users.getUser(command.getArgs().get(0)) == null) return new Action(Actions.INVALID_USERNAME, command.getArgs(), command.isRoot());
                return new Action(Actions.CHANGE_USER, command.getArgs(), command.isRoot());
            }
            case WHOAMI -> {
                return new Action(Actions.DISPLAY_CURRENT_USER, command.isRoot());
            }
            case CAT -> {
                if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
                return new Action(Actions.DISPLAY_CONTENT_OF_FILE, command.getArgs(), command.isRoot());
            }
            case MAIL -> {
                if (command.getArgs().size() < 1) return new Action(Actions.DISPLAY_MAIL_INFO, command.isRoot());
                for (String s : command.getArgs()) {
                    if ((command.isRoot() || command.getIssuer() == Users.Manager) && (s.equals("3") || s.equals("4"))) return new Action(Actions.DISPLAY_MAIL_CONTENT, command.getArgs(), true);
                    if (!command.isRoot() && command.getIssuer() == Users.Employee && (s.equals("1") || s.equals("2"))) return new Action(Actions.DISPLAY_MAIL_CONTENT, command.getArgs());
                    if (!command.isRoot() && command.getIssuer() == Users.Employee && (s.equals("3") || s.equals("4"))) return new Action(Actions.INSUFFICIENT_PERMISSION);
                    return new Action(Actions.INVALID_MAIL_ID, command.getArgs());
                }
                return new Action(Actions.DISPLAY_MAIL_CONTENT, command.getArgs(), command.isRoot());
            }
            case NOT_FOUND -> {
                return new Action(Actions.ERROR_COMMAND_NOT_FOUND, command.isRoot());
            }
            case SHUTDOWN -> {
                return new Action(Actions.SHUTDOWN, command.isRoot());
            }
            case REBOOT -> {
                return new Action(Actions.REBOOT, command.isRoot());
            }
            case CLEAR -> {
                return new Action(Actions.CLEAR, command.isRoot());
            }
            case CRONTAB -> {
                if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
                if (command.getArgs().size() > 1) return new Action(Actions.ERROR_TOO_MANY_ARGS, command.isRoot());
                if (!command.getArgs().get(0).equals("-l")) return new Action(Actions.INVALID_FLAG, command.getArgs(), command.isRoot());
                return new Action(Actions.LIST_CRONS, command.isRoot());
            }
            case SUDO -> {
                if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
                return handle(Analyser.analyse(String.join(" ", command.getArgs()), true, command.getIssuer()));
            }
        }
        return new Action(Actions.NONE, command.isRoot());
    }
}
