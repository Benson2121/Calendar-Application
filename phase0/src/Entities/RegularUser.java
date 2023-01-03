package Entities;

public class RegularUser extends User {
    private boolean isBanned;

    public RegularUser(String username, String password){
        super(username, password);
        this.isBanned = false;
    }

    public RegularUser() {
        super();
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
}

