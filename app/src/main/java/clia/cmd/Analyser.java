package clia.cmd;

import java.util.ArrayList;
import java.util.Arrays;

public class Analyser {

    public static Command analyse(String input) {
        ArrayList<String> lexems = new ArrayList<String>(Arrays.asList(input.split(" ")));
        String cmd = lexems.get(0);
        lexems.remove(0);
        switch(cmd) {
            case "ls":
                return new Command(Commands.LS, lexems);
            case "open":
                return new Command(Commands.OPEN, lexems);
            case "browser":
                return new Command(Commands.BROWSER);
            case "cat":
                return new Command(Commands.CAT, lexems);
            case "mail":
                return new Command(Commands.MAIL, lexems);
            case "su":
                return new Command(Commands.SU, lexems);
            case "whoami":
                return new Command(Commands.WHOAMI);
            case "clear":
                return new Command(Commands.CLEAR);
            case "shutdown":
                return new Command(Commands.SHUTDOWN);
            case "reboot":
                return new Command(Commands.REBOOT);
            case "crontab":
                return new Command(Commands.CRONTAB, lexems);
            default:
                return new Command(Commands.NOT_FOUND);
        }
    }
}
