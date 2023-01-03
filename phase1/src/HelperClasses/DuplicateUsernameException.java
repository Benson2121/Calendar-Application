package HelperClasses;

public class DuplicateUsernameException extends Exception{
    String duplicateUsername;
    public DuplicateUsernameException(String duplicateUsername) {
        this.duplicateUsername = duplicateUsername;
    }

    @Override
    public String toString() {
            return "Username " + duplicateUsername + " already exists; please choose another. \n";
        }

}
