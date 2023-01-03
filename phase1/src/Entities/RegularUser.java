package Entities;

/** An entity class representing a regular user of the program.
 * Has extra methods regarding banning.
 */
public class RegularUser extends User {
    private boolean isBanned;

    public RegularUser(String username, String password) {
        super(username, password);
        this.isBanned = false;
    }
    public void setIsBanned(boolean bool){
        this.isBanned = bool;
    }
    public boolean getIsBanned() {
        return isBanned;
    }
    public boolean isAdmin(){
        return false;
    }
    // Unused
    public RegularUser() {
        super();
    }
}

