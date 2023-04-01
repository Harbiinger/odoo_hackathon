package clia.cmd;

public class Command {

    private Commands cmd;
    private ArrayList<String> args;

    public Command(commands cmd, ArrayList<String> args) {
        this.cmd = cmd;
        this.args = args;
    }

    public getCmd() {
        return cmd;
    }

    public getArgs() {
        return args;
    }
}
