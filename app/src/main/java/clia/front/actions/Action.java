package clia.front.actions;

import java.util.ArrayList;

public class Action {
    private Actions action;
    private ArrayList<String> args;

    public Action(Actions action, ArrayList<String> args) {
        this.action = action;
        this.args = args;
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
}
