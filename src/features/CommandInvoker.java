package features;

public class CommandInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void execute() {
        if (command == null) {
            throw new IllegalStateException("Command has not been set.");
        }
        command.execute();
    }
}
