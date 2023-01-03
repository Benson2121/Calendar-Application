package Entities;

/** An entity class representing a single instance of a recorded action. (just Logins in Phase 0)
 */
public class History {
    private final String action;          // For phase 0, its just "Log in" and "Log out".
    private final String username;

    /** Constructor
     * @param action a description of the action performed
     * @param username the username of the user who performed the action.
     */
    public History(String username, String action) {
        this.username = username;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username + " | " + action;
    }
}
