package Controller;
import UseCase.*;
import HelperClasses.*;

/** The Controller class, responsible for retrieving user inputs and handling them.
 * @author Richard Yin
 */
public class ShellController {
    UserManager userManager = new UserManager();
    HistoryManager historyManager = new HistoryManager();
    ShellPresenter shellPresenter = new ShellPresenter();

    /** Shell prompter loop when not logged in/logged out.
     */
    public void loggedOutMenu() {
        String input = shellPresenter.loggedOutPrompter();
        do {
            if (input.equals("1")) {
                handleLogIn();
                return;
            } else if (input.equals("2")) {
                handleNewUser();
                loggedOutMenu();
                return;
            } else if (input.equals("3")) {
                System.exit(0);
                return;
            } else {
                shellPresenter.displayResult("Unrecognized input. Please try again.");
                input = shellPresenter.getInput();
            }
        } while (true);
    }

    public void handleLogIn() {
        String[] args = {"username", "password"};
        String[] argInputs = shellPresenter.getArguments(args);
        try {
            shellPresenter.displayResult(userManager.logIn(argInputs[0], argInputs[1]));
            if (userManager.userLoggedIn()) {
                historyManager.addHistory(argInputs[0], "Log in");
                userMenu();
            } else if (userManager.adminUserLoggedIn()) {
                historyManager.addHistory(argInputs[0], "Log in");
                adminUserMenu();
            } else
                loggedOutMenu();
        } catch (UserNotFoundException e) {
            shellPresenter.displayResult(e.toString());
            loggedOutMenu();
        } catch (NoPermissionException e) {
            shellPresenter.displayResult(e + ": the user is banned \n");
            loggedOutMenu();
        }
    }

    public void handleNewUser() {
        String[] args = {"username for the new user", "password for the new user"};
        String[] argInputs = shellPresenter.getArguments(args);
        try {
            shellPresenter.displayResult(userManager.createUser(argInputs[0], argInputs[1]));
        } catch (DuplicateUsernameException e) {
            shellPresenter.displayResult(e.toString());
        }
    }

    /** Shell prompter loop when logged in as a regular user.
     */
    public void userMenu() {
        String input = shellPresenter.userPrompter();
        do {
            if (input.equals("1")) {
                handleViewHistory();
                userMenu();
                return;
            } else if (input.equals("2")) {
                handleLogOut();
                return;
            } else if (input.equals("3")) {
                handleDeleteOwnAccount();
                return;
            } else {
                shellPresenter.displayResult("Unrecognized input. Please try again.");
                input = shellPresenter.getInput();
            }
        } while (true);
    }

    public void handleViewHistory() {
        shellPresenter.displayResult(historyManager.getHistoriesByUser(userManager.getCurrUser().getUsername()));
    }

    public void handleLogOut() {
        historyManager.addHistory(userManager.getCurrUser().getUsername(), "Log out");
        shellPresenter.displayResult(userManager.logOut());
        loggedOutMenu();
    }

    public void handleDeleteOwnAccount() {
        historyManager.clearHistoriesByUser(userManager.getCurrUser().getUsername());
        shellPresenter.displayResult(userManager.deleteUser());
        loggedOutMenu();
    }

    /** Shell prompter loop when logged in as an admin user.
     */
    public void adminUserMenu() {
        String input = shellPresenter.adminUserPrompter();
        do {
            if (input.equals("1")) {
                handleViewHistory();
                adminUserMenu();
                return;
            } else if (input.equals("2")) {
                handleLogOut();
                return;
            } else if (input.equals("3")) {
                handleDeleteOwnAccount();
                return;
            } else if (input.equals("4")) {
                handleNewAdminUser();
                adminUserMenu();
                return;
            } else if (input.equals("5")) {
                handleBanUser();
                adminUserMenu();
                return;
            } else if (input.equals("6")) {
                handleUnbanUser();
                adminUserMenu();
                return;
            } else if (input.equals("7")) {
                handleDeleteNonAdminAccount();
                adminUserMenu();
                return;
            } else {
                shellPresenter.displayResult("Unrecognized input. Please try again.");
                input = shellPresenter.getInput();
            }
        } while (true);
    }

    public void handleNewAdminUser() {
        String[] args = {"username for the new admin user", "password for the new admin user"};
        String[] argInputs = shellPresenter.getArguments(args);
        try {
            shellPresenter.displayResult(userManager.createAdminUser(argInputs[0], argInputs[1]));
        } catch (DuplicateUsernameException e) {
            shellPresenter.displayResult(e.toString());
        }
    }
    public void handleBanUser() {
        String[] args = {"username of the user to ban"};
        String[] argInputs = shellPresenter.getArguments(args);
        try {
            shellPresenter.displayResult(userManager.banUser(argInputs[0]));
        } catch (UserNotFoundException e) {
            shellPresenter.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            shellPresenter.displayResult(e + ": the target username is an admin \n");
        }
    }
    public void handleUnbanUser() {
        String[] args = {"username of the user to unban"};
        String[] argInputs = shellPresenter.getArguments(args);
        try {
            shellPresenter.displayResult(userManager.unbanUser(argInputs[0]));
        } catch (UserNotFoundException e) {
            shellPresenter.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            shellPresenter.displayResult(e + ": the target username is an admin \n");
        }
    }
    public void handleDeleteNonAdminAccount() {
        String[] args = {"username of the account to delete"};
        String[] argInputs = shellPresenter.getArguments(args);
        try {
            shellPresenter.displayResult(userManager.deleteUser(argInputs[0]));
            historyManager.clearHistoriesByUser(argInputs[0]);
        } catch (UserNotFoundException e) {
            shellPresenter.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            shellPresenter.displayResult(e + ": the target username is an admin \n");
        }
    }
}
