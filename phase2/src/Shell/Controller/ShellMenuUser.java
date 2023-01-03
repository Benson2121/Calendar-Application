package Shell.Controller;
import Gateway.*;
import HelperClasses.*;
import Shell.Presenter.ShellPresenter;
import UseCase.*;

import java.io.IOException;
import java.util.List;

/** The part of the Shell responsible for what to do when logged in as a regular user.
 * @author Richard Yin
 */
public class ShellMenuUser extends ShellMenu {

    /** Constructor for ShellMenuUser.
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
    public ShellMenuUser(ShellPresenter p, InterfaceSaver s, PlanManager plans, CalendarManager cals, LoggingManager logs, LabelManager labs, HistoryManager hists, UserManager users, CommentManager comms) {
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> viewHistory(p, logs, hists));
        commands.put("2", () -> logOut(p, s, logs, hists));
        commands.put("3", () -> deleteOwnAccount(p, logs, hists, cals, plans, labs, users, comms));
        commands.put("4", this::editCalendars);
        commands.put("5", () -> changeUsername(p, users, logs, hists));
        commands.put("6", () -> changePassword(p, users, logs, hists));
        commands.put("7", () -> save(p, cals, plans, labs, users, hists, comms));
    }

    /** Run the Regular User Logged In edit mode Presenter.
     */
    @Override
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("Logged in as User. Choose the commands below:",
                "View User History", "Log Out", "Delete Own Account", "Edit Calendars", "Change Username",
                "Change Password", "Save"));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * View a list of all history actions.
     * @param p The presenter
     * @param logs The logging manager
     * @param hists The history manager
     */
    protected void viewHistory(ShellPresenter p, LoggingManager logs, HistoryManager hists) {
        p.displayResult(hists.getHistoriesByUser(logs.getCurrUsername()));
    }

    /**
     * Log out and switch to the logged out menu
     * @param p The presenter
     * @param s The saver
     * @param logs The logging manager
     * @param hists The history manager
     */
    protected void logOut(ShellPresenter p, InterfaceSaver s, LoggingManager logs, HistoryManager hists) {
        if (s.changesMade()) {
            p.displayResult("Changes have been made. Type \"yes\" to log out without saving.");
            if (!p.getInput().equals("yes"))
                return;
        }
        try {
            hists.addHistory(logs.getCurrUsername(), "Logged out");
            logs.logOut();
            p.displayResult("Successfully logged out.\n");
            setMenuState(MenuState.LOG_OUT);
        } catch (IOException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Delete one's own account and return to the logged out menu
     * @param p The presenter
     * @param logs The logging manager
     * @param hists The history manager
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param users The user manager
     * @param comms The comment manager
     */
    protected void deleteOwnAccount(ShellPresenter p, LoggingManager logs, HistoryManager hists, CalendarManager cals, PlanManager plans, LabelManager labs, UserManager users, CommentManager comms) {
        try {
            hists.clearHistoriesByUser(logs.getCurrUsername());
            List<String> ownedCalendars = users.getOwnedCalendars(logs.getCurrUsername());
            for (String calendarID:ownedCalendars){
                plans.handleClearCalendar(calendarID);
                labs.handleClearUserPlans(plans.getAllPlanIDs(calendarID));
                cals.deleteCalendars(ownedCalendars);
                comms.handleClearUserPlans(plans.getAllPlanIDs(calendarID));
            }
            users.deleteUser(logs.getCurrUsername(), true);
            p.displayResult("Account deleted.\n");
            logs.logOut();
            save(p, cals, plans, labs, users, hists, comms);
            setMenuState(MenuState.LOG_OUT);
        } catch (NotFoundException | InvalidCommandException | IOException | NoPermissionException e) {
            p.displayResult(e.toString());
        }
    }
    /**
     * Go to the edit calendar menu
     */
    protected void editCalendars() {
        setMenuState(MenuState.EDIT_CALENDAR);
    }

    /**
     * Change the current user's username
     * @param p The presenter
     * @param users The user manager
     * @param logs The logging manager
     * @param hists The history manager
     */
    protected void changeUsername(ShellPresenter p, UserManager users, LoggingManager logs, HistoryManager hists) {
        String arg = p.getArguments(false, "username to change to")[0];
        try {
            hists.addHistory(logs.getCurrUsername(), "Changed username from " + logs.getCurrUsername() + " to " + arg);
            hists.handleChangeUsername(logs.getCurrUsername(), arg);
            users.setUsername(logs.getCurrUsername(), arg);
            p.displayResult("Successfully changed username to " + arg + ".\n");
        } catch (NotFoundException | DuplicateUsernameException | IOException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Change the current user's password
     * @param p The presenter
     * @param users The user manager
     * @param logs The logging manager
     * @param hists The history manager
     */
    protected void changePassword(ShellPresenter p, UserManager users, LoggingManager logs, HistoryManager hists) {
        String arg = p.getArguments(true, "password to change to")[0];
        try {
            users.setPassword(logs.getCurrUsername(), arg);
            hists.addHistory(logs.getCurrUsername(), "Changed password");
            p.displayResult("Successfully changed password to " + arg + ".\n");
        } catch (NotFoundException | IOException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Save al data.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param users The user manager
     * @param hists The history manager
     * @param comms The comment manager
     */
    protected void save(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, UserManager users, HistoryManager hists, CommentManager comms) {
        try {
            cals.save();
            plans.save();
            labs.save();
            users.save();
            hists.save();
            cals.saveAll();
            comms.saveAll();
            comms.save();
            p.displayResult("Successfully saved. \n");
        } catch (IOException e) {
            p.displayResult(e.toString());
        }
    }
}
