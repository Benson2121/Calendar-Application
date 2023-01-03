package UseCase;
import Entities.*;
import HelperClasses.*;
import java.util.*;
import java.io.*;

/** Use Case Class UserManager, which stores all instances of User and contains method to manipulate them
 *
 */
public class UserManager implements Serializable {
    private Map<String, User> users = new HashMap<>();  // A map of username to User
//    public enum UserType {
//        REGULARUSER,
//        ADMINUSER,
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public User getUser(String username) throws NotFoundException {
        User u = users.get(username);
        if (u == null)
            throw new NotFoundException(username);
        else
            return u;
    }
//    public HashSet<User> getAllUsers() {
//        return new HashSet<>(users.values());
//    }

    /** authenticateUser() returns a specified user, given correct credentials.
     * @param username User's username
     * @param password User's password
     * @return A user
     * @throws NoPermissionException if the user is banned
     * @throws NotFoundException if the credentials don't match
     */
    public User authenticateUser(String username, String password) throws NoPermissionException, NotFoundException {
        for (User u : users.values()) {
            if (u.authenticate(username, password)) {
                if (isBanned(username))
                    throw new NoPermissionException(username, "logIn");
                return u;
            }
        }
        throw new NotFoundException(username);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** isBanned() check if the user with given username is banned
     * @param username the username of the user you want to check
     * @return a boolean show whether the user is banned
     * @throws NotFoundException if the username doesn't exist
     */
    private boolean isBanned(String username) throws NotFoundException {
        return !getUser(username).isAdmin() && ((RegularUser) getUser(username)).getIsBanned();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void addUser(User newUser){
        users.put(newUser.getUsername(), newUser);
    }

    /** createUser() create a RegularUser and add to the users
     * @param username the username of the user
     * @param password the password for the username
     * @return a print String to show you created a new user
     * @throws DuplicateUsernameException if the given username already exists in the database
     */
    public String createUser(String username, String password) throws DuplicateUsernameException{
        if (users.containsKey(username))
            throw new DuplicateUsernameException(username);
        else {
            RegularUser newUser = new RegularUser(username, password);
            addUser(newUser);
            return "Successfully created new user " + username + "\n";
        }
    }

    /** createAdminUser() create an AdminUser and add to the users
     * @param username the username of the AdminUSer
     * @param password the password of the AdminUser
     * @return a print String to show you create an AdminUser
     * @throws DuplicateUsernameException if the username already exists in the database
     */
    public String createAdminUser(String username, String password) throws DuplicateUsernameException {
        if (users.containsKey(username))
            throw new DuplicateUsernameException(username);
        else {
            AdminUser newAdminUser = new AdminUser(username, password);
            addUser(newAdminUser);
            return "Successfully created new admin user " + username + "\n";
        }
    }
//    public String createUser() {
//        RegularUser newUser = new RegularUser();
//        String username = newUser.getUsername();
//        if (users.containsKey(username))
//            return createUser();
//        else {
//            addUser(newUser);
//            return "Successfully created new default user " + newUser.getUsername();
//        }
//    }
//    public String createAdminUser() {
//        AdminUser newUser = new AdminUser();
//        String username = newUser.getUsername();
//        if (users.containsKey(username))
//            return createAdminUser();
//        else {
//            addUser(newUser);
//            return "Successfully created new default admin user " + newUser.getUsername();
//        }
//    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** deleteUser() Delete the User from the users
     * @param username the username of the user you want to delete
     * @param deleteSelf a boolean to show whether you want to delete yourself
     * @return a String to show you delete a User
     * @throws NotFoundException if the username doesn't exist
     * @throws InvalidCommandException if the user cannot be deleted (they are an admin and user isn't deleting themself)
     */
    public String deleteUser(String username, boolean deleteSelf) throws NotFoundException, InvalidCommandException {
        if (deleteSelf || !getUser(username).isAdmin()) {
            users.remove(username);
            return "Successfully deleted user " + username + "\n";
        } else
            throw new InvalidCommandException("deleteUser");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** banUser() ban the User
     * @param username the user with the username you want to ban
     * @return a String to show you banned a User
     * @throws NotFoundException if the username doesn't exist
     * @throws InvalidCommandException if the user cannot be banned (they are an admin)
     */
    public String banUser(String username) throws NotFoundException, InvalidCommandException {
        if (getUser(username).isAdmin()) {
            throw new InvalidCommandException("banUser");
        }
        ((RegularUser) getUser(username)).setIsBanned(true);
        return "Successfully banned user " + username + "\n";
    }

    /** unbanUser() unban the user with the username
     * @param username the user's username you want to unban
     * @return a String to show you unbanned a User
     * @throws NotFoundException if the username doesn't exist
     * @throws InvalidCommandException if the user cannot be unbanned (they are an admin)
     */
    public String unbanUser(String username) throws NotFoundException, InvalidCommandException {
        if (getUser(username).isAdmin()) {
            throw new InvalidCommandException("banUser");
        }
        ((RegularUser) getUser(username)).setIsBanned(false);
        return "Successfully unbanned user " + username + "\n";
    }
}
