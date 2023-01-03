package UseCase;
import Entities.*;
import java.io.*;

/** Use Case class LoggingManager, which handle logIn and logOut

 */
public class LoggingManager implements Serializable {
    private User currUser;

    public String getCurrUsername() {
        return currUser.getUsername();
    }
    public boolean loggedOut() {
        return currUser == null;
    }
    /** userLoggedIn() show whether the currUser is a Regular User
     * @return a boolean show the currUser is a RegularUser
     */
    public boolean userLoggedIn() {
        return currUser instanceof RegularUser;
    }
    /** adminUserLoggedIn() show whether the currUser is a AdminUser
     * @return a boolean show the currUser is a AdminUser
     */
    public boolean adminUserLoggedIn() {
        return currUser instanceof AdminUser;
    }
    public String logIn(User user) {
        currUser = user;
        return "Successfully logged into " + user.getUsername() + "\n";
    }
    public String logOut() {
        currUser = null;
        return "Successfully logged out. \n";
    }
}
