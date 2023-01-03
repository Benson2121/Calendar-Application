package HelperClasses;

public class InvalidCommandException extends Exception {
    String invalidCommand;
    public InvalidCommandException(String invalidCommand) {
        this.invalidCommand = invalidCommand;
    }

    @Override
    public String toString() {
        return "Invalid command " + invalidCommand;
    }
}
