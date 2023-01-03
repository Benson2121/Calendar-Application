package Entities;
import java.io.*;

/** An abstract entity class representing a user of the program.
 * Can be divided into RegularUser and AdminUser, whose differences have to do with banning.
 */
public abstract class User implements Serializable {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    @Override
    public String toString() {
        return username + " | " + password + " | " + (isAdmin() ? "Admin" : "User");
    }
    public abstract boolean isAdmin();

    // Unused
    private static int idCounter = 0;
    public User() {
        this.username = User.giveId();
        this.password = "";
    }
    private static String giveId() {
        idCounter ++;
        return String.valueOf(idCounter);
    }
//    public static void setIdCounter(int idCounter) {
//        User.idCounter = idCounter;
//    }

}
