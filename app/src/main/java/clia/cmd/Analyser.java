package clia.cmd;

import clia.back.fs.Users;

import java.util.ArrayList;
import java.util.Arrays;

public class Analyser {
    public static Command analyse(String input, boolean isRoot, Users issuer) {
        Command command = analyse(input, issuer);
        command.setRoot(isRoot);
        return command;
    }

    public static Command analyse(String input, Users issuer) {
        ArrayList<String> lexemes = new ArrayList<>(Arrays.asList(input.split(" ")));
        String cmd = lexemes.get(0);
        lexemes.remove(0);
        return switch (cmd) {
            case "ls" -> new Command(Commands.LS, lexemes, issuer);
            case "cd" -> new Command(Commands.CD, lexemes, issuer);
            case "browser" -> new Command(Commands.BROWSER, issuer);
            case "cat" -> new Command(Commands.CAT, lexemes, issuer);
            case "mail" -> new Command(Commands.MAIL, lexemes, issuer);
            case "su" -> new Command(Commands.SU, lexemes, issuer);
            case "whoami" -> new Command(Commands.WHOAMI, issuer);
            case "clear" -> new Command(Commands.CLEAR, issuer);
            case "shutdown" -> new Command(Commands.SHUTDOWN, issuer);
            case "reboot" -> new Command(Commands.REBOOT, issuer);
            case "crontab" -> new Command(Commands.CRONTAB, lexemes, issuer);
            case "sudo" -> new Command(Commands.SUDO, lexemes, issuer);
            case "kill" -> new Command(Commands.KILL, lexemes, issuer);
            case "getpid" -> new Command(Commands.GETPID, lexemes, issuer);
            default -> new Command(Commands.NOT_FOUND, issuer);
        };
    }
}
