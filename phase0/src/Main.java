import Controller.ShellController;

public class Main {
    public static void main(String[] args) {
        ShellController shell = new ShellController();
        System.out.println("Create an admin user before starting the program.");
        shell.handleNewAdminUser();
        shell.loggedOutMenu();
    }
}
