package clia.cmd;

import java.util.ArrayList;

public class Analyser {

    public static analyse(string cmd) {
        ArrayList<String> lexems = new ArrayList<String>(Arrays.asList(cmd.split(" ")));
        switch(lexems.get(0)) {
            case "ls":
                return new Command(Commands.LS, lexems.subList(1, lexems.size()-1));
                break;
            case "open":
                return new Command(Commands.OPEN, lexems.subList(1, lexems.size()-1));
                break;
            case "browser":
                return new Command(Commands.BROWSER, null);
                break;
            case "cat":
                return new Command(Commands.CAT, lexems.subList(1, lexems.size()-1));
                break;
            case "mail":
                return new Command(Commands.MAIL, lexems.subList(1, lexems.size()-1));
                break;
            case "logout":
                return new Command(Commands.LOGOUT, null);
                break;
            case "login":
                return new Command(Commands.LOGIN, lexems.subList(1, lexems.size()-1));
            case "clear":
                return new Command(Commands.CLEAR, null);
                break;
            default:
                return new Command(Commands.NOT_FOUND, null);
                break;
        }
    }
}
