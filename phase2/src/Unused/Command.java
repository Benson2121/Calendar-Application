package Unused;

import java.util.List;

/** The parent class for all commands.
 *  The function eval() should evaluate a command.
 */
abstract public class Command {
    // These functions are copied from Java shell
    int maxArguments;
    int minArguments;

    public Command(int minArguments, int maxArguments) {
        this.maxArguments = maxArguments;
        this.minArguments = minArguments;
    }

    // Should return a string output corresponding to what is printed
    abstract public String evaluate();

    protected void checkArgumentsNum(List<String> arguments) throws Exception {
        if (arguments.size() > maxArguments) {
            throw new Exception("Too many arguments provided");
        }
        if (arguments.size() < minArguments) {
            throw new Exception("Not enough arguments provided");
        }
    }
}
