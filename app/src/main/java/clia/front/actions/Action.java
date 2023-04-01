package clia.front.actions;

import java.util.ArrayList;

public class Action {
    private Actions action;
    private ArrayList<String> args;
    private boolean asRoot = false;

    public Action(Actions action, ArrayList<String> args) {
        this.action = action;
        this.args = args;
    }

    public Action(Actions action, boolean asRoot) {
        this.action = action;
        this.asRoot = asRoot;
    }

    public Action(Actions action, ArrayList<String> args, boolean asRoot) {
        this.action = action;
        this.args = args;
        this.asRoot = asRoot;
    }

    public Action(Actions action) {
        this.action = action;
    }

    public Actions getAction() {
        return action;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public void setAsRoot(boolean asRoot) {
        this.asRoot = asRoot;
    }

    public boolean isAsRoot() {
        return asRoot;
    }
}
