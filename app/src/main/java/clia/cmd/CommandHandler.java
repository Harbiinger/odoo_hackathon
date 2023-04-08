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
     *
     * @param command The command to execute
     */
    public static Action handle(Command command) {
        return switch (command.getCmd()) {
            case BROWSER -> null;
            case LS -> handleLS(command);
            case CD -> handleCD(command);
            case SU -> handleSU(command);
            case WHOAMI -> handleWHOAMI(command);
            case CAT -> handleCAT(command);
            case MAIL -> handleMAIL(command);
            case NOT_FOUND -> handleNOTFOUND(command);
            case SHUTDOWN -> handleSHUTDOWN(command);
            case REBOOT -> handleREBOOT(command);
            case CLEAR -> handleCLEAR(command);
            case CRONTAB -> handleCRONTAB(command);
            case SUDO -> handleSUDO(command);
            case KILL -> handleKILL(command);
            case GETPID -> handleGETPID(command);
            default -> new Action(Actions.NONE, command.isRoot());
        };
    }

    private static Action handleGETPID(Command command) {
        if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
        if (command.getArgs().size() > 1) return new Action(Actions.ERROR_TOO_MANY_ARGS, command.isRoot());
        if (command.getIssuer() != Users.CEO)
            return new Action(Actions.INSUFFICIENT_PERMISSION, command.isRoot());
        return new Action(Actions.GETPID, command.getArgs(), command.isRoot());
    }

    private static Action handleKILL(Command command) {
        if (command.getIssuer() != Users.CEO) return new Action(Actions.INSUFFICIENT_PERMISSION);
        if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS_KILL);
        return new Action(Actions.KILL, command.getArgs(), command.isRoot());
    }

    private static Action handleSUDO(Command command) {
        if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
        return handle(Analyser.analyse(String.join(" ", command.getArgs()), true, command.getIssuer()));
    }

    private static Action handleCRONTAB(Command command) {
        if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
        if (command.getArgs().size() > 1) return new Action(Actions.ERROR_TOO_MANY_ARGS, command.isRoot());
        if (!command.getArgs().get(0).equals("-l"))
            return new Action(Actions.INVALID_FLAG, command.getArgs(), command.isRoot());
        return new Action(Actions.LIST_CRONS, command.isRoot());
    }

    private static Action handleCLEAR(Command command) {
        return new Action(Actions.CLEAR, command.isRoot());
    }

    private static Action handleREBOOT(Command command) {
        return new Action(Actions.REBOOT, command.isRoot());
    }

    private static Action handleSHUTDOWN(Command command) {
        return new Action(Actions.SHUTDOWN, command.isRoot());
    }

    private static Action handleNOTFOUND(Command command) {
        return new Action(Actions.ERROR_COMMAND_NOT_FOUND, command.isRoot());
    }

    private static Action handleMAIL(Command command) {
        if (command.getArgs().size() < 1) return new Action(Actions.DISPLAY_MAIL_INFO, command.isRoot());
        for (String s : command.getArgs()) {
            if (command.getIssuer() == Users.CEO) return new Action(Actions.INSUFFICIENT_PERMISSION);
            if ((command.isRoot() || command.getIssuer() == Users.Manager) && (s.equals("3") || s.equals("4")))
                return new Action(Actions.DISPLAY_MAIL_CONTENT, command.getArgs(), true);
            if (!command.isRoot() && command.getIssuer() == Users.Employee && (s.equals("1") || s.equals("2")))
                return new Action(Actions.DISPLAY_MAIL_CONTENT, command.getArgs());
            if (!command.isRoot() && command.getIssuer() == Users.Employee && (s.equals("3") || s.equals("4")))
                return new Action(Actions.INSUFFICIENT_PERMISSION);
            return new Action(Actions.INVALID_MAIL_ID, command.getArgs());
        }
        return new Action(Actions.DISPLAY_MAIL_CONTENT, command.getArgs(), command.isRoot());
    }

    private static Action handleCAT(Command command) {
        if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
        return new Action(Actions.DISPLAY_CONTENT_OF_FILE, command.getArgs(), command.isRoot());
    }

    private static Action handleWHOAMI(Command command) {
        return new Action(Actions.DISPLAY_CURRENT_USER, command.isRoot());
    }

    private static Action handleSU(Command command) {
        if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
        if (command.getArgs().size() > 1) return new Action(Actions.ERROR_TOO_MANY_ARGS, command.isRoot());
        if (Users.getUser(command.getArgs().get(0)) == null)
            return new Action(Actions.INVALID_USERNAME, command.getArgs(), command.isRoot());
        return new Action(Actions.CHANGE_USER, command.getArgs(), command.isRoot());
    }

    private static Action handleCD(Command command) {
        if (command.getArgs().size() < 1) return new Action(Actions.ERROR_NOT_ENOUGH_ARGS, command.isRoot());
        if (command.getArgs().size() > 1) return new Action(Actions.ERROR_TOO_MANY_ARGS, command.isRoot());
        return new Action(Actions.CHANGE_DIRECTORY, command.getArgs(), command.isRoot());
    }

    private static Action handleLS(Command command) {
        return new Action(Actions.DISPLAY_DIRECTORY, command.isRoot());
    }
}
