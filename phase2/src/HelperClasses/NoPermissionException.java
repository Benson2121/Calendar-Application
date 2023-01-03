package HelperClasses;

/**
 * NoPermissionException, thrown when some user does not have permission to perform command.
 */
public class NoPermissionException extends Exception {
    String username;
    String invalidCommand;

    /** Constructor for NoPermissionException.
     * @param username the User's username
     * @param invalidCommand the name of the command the User doesn't have access to
     */
    public NoPermissionException(String username, String invalidCommand) {
        this.username = username;
        this.invalidCommand = invalidCommand;
    }

    /** String representation of NoPermissionException.
     * @return NoPermissionException as a String
     */
    @Override
    public String toString() {
        return "User " + username + " does not have permission to use the command " + invalidCommand;
    }
}
