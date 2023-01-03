package GUI.Controller;
import GUI.Menu.Menu;
import HelperClasses.*;
import java.io.IOException;
import UseCase.*;
import javafx.scene.control.Label;

/**
 * A controller class that handles user requests in the settings tab of the main menu.
 * @author Richard Yin
 */
public class HandlerSettings {
    private final UserManager users;
    private final LoggingManager logs;
    private final HistoryManager hists;
    private final Menu menu;
    private final CalendarManager cals;
    private final PlanManager plans;
    private final LabelManager labs;

    /**
     * Constructor for HandlerSettings that acts as a setter.
     * @param users The UserManager to use
     * @param logs The LoggingManager to use
     * @param hists The HistoryManager to use
     * @param menu The Menu to use
     * @param cals The CalendarManager to use
     * @param plans The PlanManager to use
     * @param labs The LabelManager to use
     */
    public HandlerSettings(UserManager users, LoggingManager logs, HistoryManager hists, Menu menu, CalendarManager cals, PlanManager plans, LabelManager labs) {
        this.users = users;
        this.logs = logs;
        this.hists = hists;
        this.menu = menu;
        this.cals = cals;
        this.plans = plans;
        this.labs = labs;
    }

    /**
     * Execute the button "change username".
     * @param newUsername The text field for entering the new username
     * @param messageChangeUsername The text label next to the new username text field
     * @param messageLoggedIn The text label in the logged-in menu that displays the user's name
     * @return A boolean indicating whether the operation was successful.
     */
    public boolean changeUsername(String newUsername, Label messageChangeUsername, Label messageLoggedIn) {
        if (logs.getCurrUsername().equals(newUsername)) {
            menu.errorMessage(messageChangeUsername, "New & old usernames match");
            return false;
        } else {
            try {
                hists.addHistory(logs.getCurrUsername(), "Changed username from " + logs.getCurrUsername() + " to " + newUsername);
                hists.handleChangeUsername(logs.getCurrUsername(), newUsername);
                users.setUsername(logs.getCurrUsername(), newUsername);
                messageLoggedIn.setText("Logged in as " + logs.getCurrUsername());
                menu.successMessage(messageChangeUsername, "Username changed!");
                return true;
            } catch (NotFoundException | DuplicateUsernameException | IOException e) {
                menu.errorMessage(messageChangeUsername, e.toString());
                return false;
            }
        }
    }

    /**
     * Execute the button "change password".
     * @param newPassword The password field for entering the new password.
     * @param messageChangePassword The text label next to the new password field
     * @return A boolean indicating whether the operation was successful.
     */
    public boolean changePassword(String newPassword, Label messageChangePassword) {
        if (logs.getCurrPassword().equals(newPassword)) {
            menu.errorMessage(messageChangePassword, "New & old passwords match");
            return false;
        } else {
            try {
                users.setPassword(logs.getCurrUsername(), newPassword);
                hists.addHistory(logs.getCurrUsername(), "Changed password");
                menu.successMessage(messageChangePassword, "Password changed!");
                return true;
            } catch (NotFoundException | IOException e) {
                menu.errorMessage(messageChangePassword, e.toString());
                return false;
            }
        }
    }

    /**
     * Execute the button "delete my account".
     * @param confirmPassword The password field for confirming the current password
     * @param messageConfirmPassword The text label next to the new password field
     * @return A boolean indicating whether the operation was successful.
     */
    public boolean deleteMyAccount(String confirmPassword, Label messageConfirmPassword) {
        try {
            if (confirmPassword.equals(logs.getCurrPassword())) {
                String u = logs.getCurrUsername();
                hists.clearHistoriesByUser(u);

                String c = cals.getCurrCalendarID();
                if (c != null) {
                    cals.deleteCalendar(c);
                    plans.handleClearCalendar(c);
                    labs.handleClearUserPlans(plans.getAllPlanIDs(c));
                    cals.setCurrCalendarID(null);
                }

                users.deleteUser(u, true);
                return true;
            } else {
                menu.errorMessage(messageConfirmPassword, "Incorrect password");
                return false;
            }
        } catch (InvalidCommandException | NotFoundException | NoPermissionException | IOException e) {
            menu.errorMessage(messageConfirmPassword, e.toString());
            return false;
        }
    }
}
