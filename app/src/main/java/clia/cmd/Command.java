package clia.cmd;

import java.util.ArrayList;

public class Command {

    private Commands cmd;
    private ArrayList<String> args;

    public Command(Commands cmd, ArrayList<String> args) {
        this.cmd = cmd;
        this.args = args;
    }

    public Commands getCmd() {
        return cmd;
    }

    public ArrayList<String> getArgs() {
        return args;
    }
}
