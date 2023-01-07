package Shell.Controller;
import Gateway.*;
import HelperClasses.*;
import Shell.Presenter.ShellPresenter;
import UseCase.*;

import java.io.IOException;

/** The part of the Shell responsible for what to do when logged in as an admin user.
 * @author Richard Yin
 */
public class ShellMenuUserAdmin extends ShellMenuUser {

    /** Constructor for ShellMenuUserAdmin.
     * @param p the presenter
     * @param s the saver
     * @param plans the plan manager
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     * @param labs the label manager
     * @param hists the history manager
     * @param comms the comment manager
     */
    public ShellMenuUserAdmin(ShellPresenter p, InterfaceSaver s, PlanManager plans, CalendarManager cals, LoggingManager logs, LabelManager labs, UserManager users, HistoryManager hists, CommentManager comms) {
        super(p, s, plans, cals, logs, labs, hists, users, comms);
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> viewHistory(p, logs, hists));
        commands.put("2", () -> logOut(p, s, logs, hists));
        commands.put("3", () -> deleteOwnAccount(p, logs, hists, cals, plans, labs, users,comms));
        commands.put("4", () -> newAdminUser(p, users, plans, cals, logs));
        commands.put("5", () -> banUser(p, users, logs, hists));
        commands.put("6", () -> unbanUser(p, users, logs, hists));
        commands.put("7", () -> deleteNonAdminAccount(p, users, plans, cals, labs, hists, logs,comms));
        commands.put("8", this::editCalendars);
        commands.put("9", () -> changeUsername(p, users, logs, hists));
        commands.put("10", () -> changePassword(p, users, logs, hists));
        commands.put("11", () -> save(p, cals, plans, labs, users, hists, comms));
    }

    /** Run the Admin User Logged In edit mode Presenter.
     */
    @Override
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("Logged in as Admin. Choose the commands below:",
                "View User History", "Log Out", "Delete Own Account", "Create Admin User", "Ban User",
                "Unban User", "Delete Non-Admin Account", "Edit Calendars", "Change Username", "Change Password", "Save"));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Create a new admin user.
     * @param p The presenter
     * @param users The user manager
     * @param plans The plan manager
     * @param cals The calendar manager
     * @param logs The logging manager
     */
    private void newAdminUser(ShellPresenter p, UserManager users, PlanManager plans, CalendarManager cals, LoggingManager logs) {
        String[] args = p.getArguments("01", "username for the new admin user",
                "password for the new admin user");
        try {
            users.createAdminUser(args[0], args[1]);
            p.displayResult("Admin User created.");
            cals.createCalendar(args[0], "", logs.getCurrUsername());
            plans.handleCreateCalendar(args[0]);
            users.save();
        } catch (DuplicateUsernameException | IOException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Ban a non-admin user.
     * @param p The presenter
     * @param users The user manager
     * @param logs The plan manager
     * @param hists The history manager
     */
    private void banUser(ShellPresenter p, UserManager users, LoggingManager logs, HistoryManager hists) {
        String[] args = p.getArguments(false, "username of the user to ban");
        try {
            users.banUser(args[0]);
            hists.addHistory(logs.getCurrUsername(), "Banned user " + args[0]);
            p.displayResult("User banned.");
        } catch (NotFoundException | IOException e) {
            p.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            p.displayResult(e + ": the target username is an admin \n");
        }
    }

    /**
     * Unban a non-admin user.
     * @param p The presenter
     * @param users The user manager
     * @param logs The plan manager
     * @param hists The history manager
     */
    private void unbanUser(ShellPresenter p, UserManager users, LoggingManager logs, HistoryManager hists) {
        String[] args = p.getArguments(false, "username of the user to unban");
        try {
            users.unbanUser(args[0]);
            hists.addHistory(logs.getCurrUsername(), "Unbanned user " + args[0]);
            p.displayResult("User unbanned.");
        } catch (NotFoundException | IOException e) {
            p.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            p.displayResult(e + ": the target username is an admin \n");
        }
    }

    /**
     * Delete a non-admin account.
     * @param p The presenter
     * @param users The user manager
     * @param plans The plan manager
     * @param cals The calendar manager
     * @param labs The label manager
     * @param hists The history manager
     * @param logs The logging manager
     * @param comms The comment manager
     */
    private void deleteNonAdminAccount(ShellPresenter p, UserManager users, PlanManager plans, CalendarManager cals, LabelManager labs, HistoryManager hists, LoggingManager logs, CommentManager comms) {
        String[] args = p.getArguments(false, "username of the account to delete");
        try {
            users.deleteUser(args[0], false);
            p.displayResult("User deleted.");
            hists.addHistory(logs.getCurrUsername(), "Deleted account " + args[0]);
            hists.clearHistoriesByUser(args[0]);
            // CalendarID is currently set to the name of a user so far, it will be expanded upon properly in phase 2
            String currCalendarID = args[0];
            // Delete all user data
            if (currCalendarID != null) {
                cals.deleteCalendar(currCalendarID);
                plans.handleClearCalendar(currCalendarID);
                labs.handleClearUserPlans(plans.getAllPlanIDs(currCalendarID));
                cals.setCurrCalendarID(null);
                comms.handleClearUserPlans(plans.getAllPlanIDs(currCalendarID));
            }
        } catch (NotFoundException | IOException | NoPermissionException e) {
            p.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            p.displayResult(e + ": the target username is an admin \n");
        }
    }
}
