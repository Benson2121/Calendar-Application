package Entities;

/** An entity class representing a regular user of the program.
 * Has extra methods regarding banning.
 */
public class RegularUser extends User {
    private boolean isBanned;

    /** Constructor for RegularUser.
     * @param username the RegularUser's username
     * @param password the RegularUser's password
     */
    public RegularUser(String username, String password) {
        super(username, password);
        this.isBanned = false;
    }

    /** Setter for is banned.
     * @param banned Whether to ban the user. */
    public void setIsBanned(boolean banned){
        this.isBanned = banned;
    }

    /** Getter for is banned.
     * @return If the user is banned. */
    public boolean getIsBanned() {
        return isBanned;
    }

    /** Return whether the user is an admin.
     * @return False */
    public boolean isAdmin(){
        return false;
    }

    /** Constructor for RegularUser
     */
    public RegularUser() {
        super();
    }
}

