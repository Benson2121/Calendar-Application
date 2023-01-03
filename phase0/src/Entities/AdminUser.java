package Entities;

public class AdminUser extends User {
    public AdminUser(String username, String password){
        super(username, password);
    }
    public AdminUser() {
        super();
    }
    public boolean isAdmin() {
        return true;
    }
}
