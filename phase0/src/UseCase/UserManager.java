package UseCase;
import Entities.*;
import HelperClasses.*;
import java.util.*;

public class UserManager {
    private User currUser;
    private Map<String, User> users = new HashMap<>();  // A map of username to User
//    public enum UserType {
//        REGULARUSER,
//        ADMINUSER,
//    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public User getUser(String username) throws UserNotFoundException {
        User u = users.get(username);
        if (u == null)
            throw new UserNotFoundException(username);
        else
            return u;
    }
//    public HashSet<User> getAllUsers() {
//        return new HashSet<>(users.values());
//    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public User getCurrUser() {
        return currUser;
    }
//    public boolean loggedOut() {
//        return currUser == null;
//    }
    public boolean userLoggedIn() {
        return currUser instanceof RegularUser;
    }
    public boolean adminUserLoggedIn() {
        return currUser instanceof AdminUser;
    }
    private boolean isBanned(String username) throws UserNotFoundException {
        return !getUser(username).isAdmin() && ((RegularUser) getUser(username)).getIsBanned();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addUser(User newUser){
        users.put(newUser.getUsername(), newUser);
    }
    public String createUser(String username, String password) throws DuplicateUsernameException{
        if (users.containsKey(username))
            throw new DuplicateUsernameException(username);
        else {
            RegularUser newUser = new RegularUser(username, password);
            addUser(newUser);
            return "Successfully created new user " + username + "\n";
        }
    }
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
    // Deletes own account. Any user can do this.
    public String deleteUser() {
        String temp = currUser.getUsername();
        users.remove(temp);
        logOut();
        return "Successfully deleted user " + temp + "\n";
    }
    // Deletes someone else's account. Only admins can do this, and only to non-admin users.
    public String deleteUser(String username) throws UserNotFoundException, InvalidCommandException {
        if (getUser(username).isAdmin()) {
            throw new InvalidCommandException("banUser");
        }
        users.remove(username);
        return "Successfully deleted user " + username + "\n";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String logIn(String username, String password) throws UserNotFoundException, NoPermissionException {
        if (!users.containsKey(username))
            throw new UserNotFoundException(username);
        for (User u : users.values()) {
            if (u.authenticate(username, password)) {
                if (isBanned(username))
                    throw new NoPermissionException(username, "logIn");
                currUser = u;
                return "Successfully logged into " + username + "\n";
            }
        }
        return "Wrong username or password. Please try again. \n";
    }
    public String logOut() {
        currUser = null;
        return "Successfully logged out. \n";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String banUser(String username) throws UserNotFoundException, InvalidCommandException {
        if (getUser(username).isAdmin()) {
            throw new InvalidCommandException("banUser");
        }
        ((RegularUser) getUser(username)).setIsBanned(true);
        return "Successfully banned user " + username + "\n";
    }

    public String unbanUser(String username) throws UserNotFoundException, InvalidCommandException {
        if (getUser(username).isAdmin()) {
            throw new InvalidCommandException("banUser");
        }
        ((RegularUser) getUser(username)).setIsBanned(false);
        return "Successfully unbanned user " + username + "\n";
    }
}
