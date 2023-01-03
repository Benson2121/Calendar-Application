package HelperClasses;

/**
 * NotFoundException, thrown when some user input cannot be found in any of the Manager classes.
 */
public class NotFoundException extends Exception {
    private final String invalidInput;
    public NotFoundException(String invalidInput) {
        this.invalidInput = invalidInput;
    }
    @Override
    public String toString() {
        return "Could not find invalid input " + invalidInput + ". PLease try again. \n";
    }
}
