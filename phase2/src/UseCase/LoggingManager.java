package UseCase;
import Entities.*;
import java.io.*;

/** Use Case class LoggingManager, which handle logIn and logOut

 */
public class LoggingManager implements Serializable {
    private User currUser;

    /** Get current User's username.
     * @return current User's username
     */
    public String getCurrUsername() {
        return currUser.getUsername();
    }

    /** Get current User's password.
     * @return current User's password
     */
    public String getCurrPassword() {
        return currUser.getPassword();
    }

    /** Return if User has logged out.
     * @return true/false if user has logged out.
     */
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

    /** Log in a User.
     * @param user the User logging in
     */
    public void logIn(User user) {
        currUser = user;
    }

    /** Log out a User.
     */
    public void logOut() {
        currUser = null;
    }
}
