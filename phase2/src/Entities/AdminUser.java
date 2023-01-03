package Entities;

/** An entity class representing an admin user of the program.
 * Has extra methods regarding banning.
 */
public class AdminUser extends User {

    /** Constructor for AdminUser.
     * @param username the AdminUser's username
     * @param password the AdminUser's password
     */
    public AdminUser(String username, String password){
        super(username, password);
    }

    /** Whether the user is an admin.
     * @return true
     */
    @Override
    public boolean isAdmin() {
        return true;
    }

    /** Constructor for AdminUser.
     */
    public AdminUser() {
        super();
    }
}
