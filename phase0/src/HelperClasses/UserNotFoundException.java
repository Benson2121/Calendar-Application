package HelperClasses;

public class UserNotFoundException extends Exception {
    String invalidUsername;
    public UserNotFoundException(String invalidUsername) {
        this.invalidUsername = invalidUsername;
    }

    @Override
    public String toString() {
        return "Username " + invalidUsername + " is not found. \n";
    }
}
