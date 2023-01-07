package HelperClasses;

/**
 * InvalidCommandException, thrown when command does not have proper, required input or cannot be executed.
 */
public class InvalidCommandException extends Exception {
    String invalidCommand;

    /** Constructor for InvalidCommandException.
     * @param invalidCommand the name of the invalid command
     */
    public InvalidCommandException(String invalidCommand) {
        this.invalidCommand = invalidCommand;
    }

    /** String representation of InvalidCommandException.
     * @return InvalidCommandException as a String
     */
    @Override
    public String toString() {
        return "Invalid command " + invalidCommand;
    }
}
