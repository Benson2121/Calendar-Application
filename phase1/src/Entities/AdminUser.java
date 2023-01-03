package Entities;

/** An entity class representing an admin user of the program.
 * Has extra methods regarding banning.
 */
public class AdminUser extends User {
    public AdminUser(String username, String password){
        super(username, password);
    }
    public boolean isAdmin() {
        return true;
    }

    // Unused
    public AdminUser() {
        super();
    }
}
