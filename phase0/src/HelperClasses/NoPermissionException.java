package HelperClasses;

public class NoPermissionException extends Exception {
    String username;
    String invalidCommand;
    public NoPermissionException(String username, String invalidCommand) {
        this.username = username;
        this.invalidCommand = invalidCommand;
    }

    @Override
    public String toString() {
        return "User " + username + " does not have permission to use the command " + invalidCommand;
    }
}
