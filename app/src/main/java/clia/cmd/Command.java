package clia.cmd;

import java.util.ArrayList;

public class Command {
    private Commands cmd;
    private ArrayList<String> args;
    private boolean isRoot = false;

    public Command(Commands cmd, ArrayList<String> args, boolean isRoot) {
        this.cmd = cmd;
        this.args = args;
        this.isRoot = isRoot;
    }

    public Command(Commands cmd, boolean isRoot) {
        this.cmd = cmd;
        this.isRoot = isRoot;
    }

    public Command(Commands cmd, ArrayList<String> args) {
        this.cmd = cmd;
        this.args = args;
    }

    public Command(Commands cmd) {
        this.cmd = cmd;
    }

    public Commands getCmd() {
        return cmd;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean isRoot() {
        return isRoot;
    }
}
