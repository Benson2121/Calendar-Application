package HelperClasses;

/**
 * NotFoundException, thrown when some user input cannot be found in any of the Manager classes.
 */
public class NotFoundException extends Exception {
    private final String invalidInput;

    /** Constructor for NotFoundException.
     * @param invalidInput name of input causing error/not found
     */
    public NotFoundException(String invalidInput) {
        this.invalidInput = invalidInput;
    }

    /** String representation of NotFoundException.
     * @return NotFoundException as a String
     */
    @Override
    public String toString() {
        return "Could not find invalid input " + invalidInput + ". PLease try again. \n";
    }
}
