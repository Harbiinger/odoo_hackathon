package clia.front.actions;

import java.util.ArrayList;

public class Action {
    private Actions action;
    private ArrayList<Object> args;

    public Action(Actions action, ArrayList<Object> args) {
        this.action = action;
        this.args = args;
    }

    public Action(Actions action) {
        this.action = action;
    }

    public Actions getAction() {
        return action;
    }

    public ArrayList<Object> getArgs() {
        return args;
    }
}
