package Shell.Controller;
import HelperClasses.*;
import Shell.Presenter.ShellPresenter;
import UseCase.*;

import java.io.IOException;

/** The part of the Shell responsible for what to do when logged out.
 * @author Richard Yin
 */
public class ShellMenuLoggedOut extends ShellMenu {

    /** Constructor for ShellMenuLoggedOut.
     * @param p the presenter
     * @param users the user manager
     * @param logs the logging manager
     * @param hists the history manager
     */
    public ShellMenuLoggedOut(ShellPresenter p, UserManager users, LoggingManager logs, HistoryManager hists) {
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> logIn(p, users, logs, hists));
        commands.put("2", () -> newUser(p, users, hists));
        commands.put("3", () -> System.exit(0));
    }

    /** Run the Logged Out edit mode Presenter.
     */
    @Override
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("CSC207: Phase 2, Group 0253, Main Menu \nPlease enter the numbers corresponding to the following commands:",
                "Log In", "New User", "Exit"));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Log in and change menus to the user menu
     * @param p The presenter
     * @param users The user manager
     * @param logs The logging manager
     * @param hists The history manager
     */
    private void logIn(ShellPresenter p, UserManager users, LoggingManager logs, HistoryManager hists) {
        String[] args = p.getArguments("01", "username", "password");
        try {
            logs.logIn(users.authenticateUser(args[0], args[1]));
            p.displayResult("Successfully logged in.\n");
            hists.save();
            if (logs.userLoggedIn()) {
                hists.addHistory(args[0], "Logged in");
                setMenuState(MenuState.USER_MENU);
            } else if (logs.adminUserLoggedIn()) {
                hists.addHistory(args[0], "Logged in");
                setMenuState(MenuState.ADMIN_USER_MENU);
            }
        } catch (NotFoundException e) {
            p.displayResult("Invalid username or password. Please try again. \n");
        } catch (NoPermissionException e) {
            p.displayResult("Cannot log in because username " + args[0] + " is banned. \n");
        } catch (IOException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Create a new regular user
     * @param p The presenter
     * @param users The user manager
     * @param hists The history manager
     */
    private void newUser(ShellPresenter p, UserManager users, HistoryManager hists) {
        String[] args = p.getArguments("01", "username for the new user", "password for the new user");
        try {
            users.createUser(args[0], args[1]);
            p.displayResult("Created new user.\n");
            hists.addHistory(args[0], "Created user " + args[0]);
            users.save();
            hists.save();
        } catch (DuplicateUsernameException | IOException e) {
            p.displayResult(e.toString());
        }
    }
}
