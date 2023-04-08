package clia.cmd;

import clia.back.fs.Users;

import java.util.ArrayList;

public class Command {
    private final Commands cmd;
    private ArrayList<String> args;
    private boolean isRoot = false;
    private final Users issuer;

    public Command(Commands cmd, ArrayList<String> args, boolean isRoot, Users issuer) {
        this.cmd = cmd;
        this.issuer = issuer;
        this.args = args;
        this.isRoot = isRoot;
    }

    public Command(Commands cmd, boolean isRoot, Users issuer) {
        this.cmd = cmd;
        this.issuer = issuer;
        this.isRoot = isRoot;
    }

    public Command(Commands cmd, ArrayList<String> args, Users issuer) {
        this.cmd = cmd;
        this.issuer = issuer;
        this.args = args;
    }

    public Command(Commands cmd, Users issuer) {
        this.cmd = cmd;
        this.issuer = issuer;
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

    public Users getIssuer() {
        return issuer;
    }
}
