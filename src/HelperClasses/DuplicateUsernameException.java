package HelperClasses;

/**
 * DuplicateUsernameException, thrown when username already exists.
 */
public class DuplicateUsernameException extends Exception{
    String duplicateUsername;

    /** Constructor for DuplicateUsernameException.
     * @param duplicateUsername the name of the duplicate username
     */
    public DuplicateUsernameException(String duplicateUsername) {
        this.duplicateUsername = duplicateUsername;
    }

    /** String representation of DuplicateUsernameException.
     * @return DuplicateUsernameException as a String
     */
    @Override
    public String toString() {
            return "Username " + duplicateUsername + " already exists. \n";
        }

}
