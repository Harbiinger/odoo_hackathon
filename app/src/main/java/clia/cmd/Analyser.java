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
        ArrayList<String> lexems = new ArrayList<>(Arrays.asList(input.split(" ")));
        String cmd = lexems.get(0);
        lexems.remove(0);
        switch (cmd) {
            case "ls":
                return new Command(Commands.LS, lexems, issuer);
            case "cd":
                return new Command(Commands.CD, lexems, issuer);
            case "browser":
                return new Command(Commands.BROWSER, issuer);
            case "cat":
                return new Command(Commands.CAT, lexems, issuer);
            case "mail":
                return new Command(Commands.MAIL, lexems, issuer);
            case "su":
                return new Command(Commands.SU, lexems, issuer);
            case "whoami":
                return new Command(Commands.WHOAMI, issuer);
            case "clear":
                return new Command(Commands.CLEAR, issuer);
            case "shutdown":
                return new Command(Commands.SHUTDOWN, issuer);
            case "reboot":
                return new Command(Commands.REBOOT, issuer);
            case "crontab":
                return new Command(Commands.CRONTAB, lexems, issuer);
            case "sudo":
                return new Command(Commands.SUDO, lexems, issuer);
            case "kill":
                return new Command(Commands.KILL, lexems, issuer);
            case "getpid":
                return new Command(Commands.GETPID, lexems, issuer);
            default:
                return new Command(Commands.NOT_FOUND, issuer);
        }
    }
}
