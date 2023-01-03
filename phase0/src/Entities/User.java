package Entities;

/** An entity representing a user.
 */
public abstract class User {
    private String username;
    private String password;
    private static int idCounter = 0;

    // Constructor for User with input
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Constructor for User without any input - intended as shortcuts to use in tests
    public User() {
        this.username = User.giveId();
        this.password = "";
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
//    public void changeUsername(String username){
//        setUsername(username);
//    }
//    public void changePassword(String password){
//        setPassword(password);
//    }

//    public static void setIdCounter(int idCounter) {
//        User.idCounter = idCounter;
//    }

    private static String giveId()
    {
        idCounter ++;
        return String.valueOf(idCounter);
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + " | " + password + " | " + (isAdmin() ? "Admin" : "User");
    }

//    public int compareTo(User otherUser){
//        return this.username.compareTo(otherUser.username);
//    }

    public abstract boolean isAdmin();
}
