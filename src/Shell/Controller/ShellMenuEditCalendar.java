package Shell.Controller;
import HelperClasses.*;
import Shell.Presenter.ShellPresenter;
import UseCase.*;
import java.io.IOException;
import java.util.*;

/** The part of the Shell responsible for what to do when in calendar edit mode.
 * @author Richard Yin
 */
public class ShellMenuEditCalendar extends ShellMenu {

    /** Constructor for ShellMenuEditCalendar.
     * @param p the presenter
     * @param plans the plan manager
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     * @param labs the label manager
     * @param hists the history manager
     * @param comms the comment manager
     */
    public ShellMenuEditCalendar(ShellPresenter p, PlanManager plans, CalendarManager cals, UserManager users, LoggingManager logs, LabelManager labs, HistoryManager hists, CommentManager comms) {
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> back(logs));
        commands.put("2", () -> viewSchedule(p, plans));
        commands.put("3", () -> viewScheduleByLabel(p, plans, labs));
        commands.put("4", () -> viewUpcoming(p, plans));
        commands.put("5", () -> createCalendar(p, cals, users, logs, plans, hists));
        commands.put("6", () -> deleteCalendar(p, cals, users, plans, labs, logs, hists, comms));
        commands.put("7", () -> clearCalendars(p, cals, users, plans, logs, labs, hists, comms));
        commands.put("8", () -> editName(p, users, cals, hists, logs));
        commands.put("9", () -> editDescription(p, users, cals, hists, logs));
        commands.put("10", () -> editSharePermission(p, users, logs));
        commands.put("11", () -> viewAllCalendars(p, cals, users, logs));
        commands.put("12", () -> viewSharedCalendars(p, cals, users, logs));
        commands.put("13", () -> editPlans(p, cals, users, logs));
    }

    /** Run the Calendar edit mode Presenter.
     */
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("Calendar Editor. Choose the commands below:",
                "Back", "View Schedule", "View Schedule by Label", "View Upcoming", "Create Calendar",
                "Delete Calendar", "Clear Calendars", "Edit Calendar Name", "Edit Calendar Description",
                "Edit Sharing Permissions", "View My Calendars", "View Calendars Shared With Me", "Edit Events/Tasks"
        ));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Go back a stage in MenuStates
     */
    private void back(LoggingManager logs) {
        if (logs.adminUserLoggedIn())
            setMenuState(MenuState.ADMIN_USER_MENU);
        else
            setMenuState(MenuState.USER_MENU);
    }

    /** View Schedule of Calendar
     * @param p the presenter
     * @param plans the plan manager
     */
    private void viewSchedule(ShellPresenter p, PlanManager plans) {
        String[] args = p.getArguments(false, "calendar ID to view");
        try {
            if (plans.getAllPlans(args[0]).isEmpty()) {
                p.displayResult("Nothing in schedule yet. \n");
            } else {
                p.displayResult(plans.viewSchedule(args[0]));
            }
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** View Schedule of Calendar based on label
     * @param p the presenter
     * @param plans the plan manager
     * @param labs the label manager
     */
    private void viewScheduleByLabel(ShellPresenter p, PlanManager plans, LabelManager labs) {
        String[] argsID = p.getArguments(false, "calendar ID to view");
        try {
            if (plans.getAllPlans(argsID[0]).isEmpty()) {
                p.displayResult("Nothing in schedule yet. \n");
                return;
            }
            Set<String> args = new HashSet<>();
            String label = p.getArguments(false, "label to filter for")[0];
            while (!label.equals("")) {
                args.add(label);
                label = p.getArguments(true,  "another label to filter for (just enter nothing to stop adding labels)")[0];
            }
            Set<String> planIDs = labs.getPlanIDsWithLabels(args);
            String result = plans.viewScheduleByLabel(planIDs, argsID[0]);
            if (result.length() == 0)
                p.displayResult("No events/tasks found with specified labels.\n");
            else
                p.displayResult(result);
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** View Upcoming Schedule of Calendar
     * @param p the presenter
     * @param plans the plan manager
     */
    private void viewUpcoming(ShellPresenter p, PlanManager plans) {
        String[]  args = p.getArguments(false, "calendar ID to view");
        try {
            if (plans.getAllPlans(args[0]).isEmpty()) {
                p.displayResult("Nothing in schedule yet. \n");
                return;
            }
            String result = plans.viewAllUpcoming(args[0]);
            if (result.length() == 0)
                p.displayResult("No upcoming events/tasks.\n");
            else
                p.displayResult(result);
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** Create a Calendar
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     * @param plans the plan manager
     * @param hists the history manager
     */
    private void createCalendar(ShellPresenter p, CalendarManager cals, UserManager users, LoggingManager logs, PlanManager plans, HistoryManager hists) {
        String[] args = p.getArguments("01", "name for the calendar", "description for the Calendar");
        try {
            String result = cals.createCalendar(args[0], args[1], logs.getCurrUsername());
            users.addCalendar(result, logs.getCurrUsername());
            plans.handleCreateCalendar(result);
            hists.addHistory(logs.getCurrUsername(), "Created calendar " + args[0]);
            p.displayResult("Successfully created calendar with ID: " + result + "\n");
        } catch (IOException | NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** Delete a Calendar
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     * @param plans the plan manager
     * @param hists the history manager
     * @param labs the label manager
     * @param comms the comment manager
     */
    private void deleteCalendar(ShellPresenter p, CalendarManager cals, UserManager users, PlanManager plans, LabelManager labs, LoggingManager logs, HistoryManager hists, CommentManager comms) {
        String[] args = p.getArguments(false, "calendar ID to delete");
        try {
            hists.addHistory(logs.getCurrUsername(), "Deleted calendar " + cals.getCalendar(args[0]));
            cals.deleteCalendar(args[0]);
            users.deleteCalendar(args[0], logs.getCurrUsername());
            for (String planID : plans.getAllPlanIDs(args[0])) {
                labs.handleDeletePlan(planID);
                comms.handleDeletePlan(planID);
            }
            plans.clearAllPlans(args[0]);
            p.displayResult("Successfully deleted calendar with ID " + args[0] + "\n");
        } catch (NotFoundException | IOException | NoPermissionException e) {
            p.displayResult(e.toString());
        }
    }

    /** Clear all Calendars
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     * @param plans the plan manager
     * @param hists the history manager
     * @param labs the label manager
     * @param comms the comment manager
     */
    private void clearCalendars(ShellPresenter p, CalendarManager cals, UserManager users, PlanManager plans, LoggingManager logs, LabelManager labs, HistoryManager hists, CommentManager comms) {
        p.displayResult("Are you sure? Type \"clear\" to continue.");
        try {
            String args = p.getInput();
            if (args.equals("clear")) {
                for (String calendarID : users.getOwnedCalendars(logs.getCurrUsername())) {
                    cals.deleteCalendar(calendarID);
                    for (String planID:plans.getAllPlanIDs(calendarID)) {
                        labs.handleDeletePlan(planID);
                        comms.handleDeletePlan(planID);
                    }
                    plans.clearAllPlans(calendarID);
                }
                hists.addHistory(logs.getCurrUsername(), "Cleared calendars");
                users.clearCalendars(logs.getCurrUsername());
                p.displayResult("Successfully clear all the calendars\n");
            }
        } catch (NotFoundException | IOException | NoPermissionException e) {
            p.displayResult(e.toString());
        }
    }

    /** Edit Calendar Description
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     * @param hists the history manager
     */
    private void editDescription(ShellPresenter p, UserManager users, CalendarManager cals, HistoryManager hists, LoggingManager logs) {
        String[] args = p.getArguments(false, "calendar ID", "description to change to");
        try {
            if (users.validateCalendarForEditAccess(logs.getCurrUsername(), args[0])) {
                cals.changeCalendarDescription(args[1], args[0]);
                hists.addHistory(logs.getCurrUsername(), "Changed description of calendar " + cals.getCalendar(args[0]));
                p.displayResult("Successfully changed description of calendar " + args[0] + " to " + args[1] + "\n");
            }
        } catch (NotFoundException | IOException | NoPermissionException e) {
            p.displayResult(e.toString());
        }
    }

    /** Edit Calendar Name
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     * @param hists the history manager
     */
    private void editName(ShellPresenter p, UserManager users, CalendarManager cals, HistoryManager hists, LoggingManager logs) {
        String[] args = p.getArguments(false, "calendar ID", "name to change to");
        try {
            if (users.validateCalendarForEditAccess(logs.getCurrUsername(), args[0])) {
                hists.addHistory(logs.getCurrUsername(), "Changed name of calendar " + cals.getCalendar(args[0]) + " to " + args[1]);
                cals.changeCalendarName(args[1], args[0]);
                p.displayResult("Successfully changed name of calendar " + args[0] + " to " + args[1] + "\n");
            }
        } catch (NotFoundException | IOException | NoPermissionException e) {
            p.displayResult(e.toString());
        }
    }

    /** Edit Calendar share permission
     * @param p the presenter
     * @param users the user manager
     * @param logs the logging manager
     */
    private void editSharePermission(ShellPresenter p, UserManager users, LoggingManager logs) {
        String[] args = p.getArguments(false, "calendar ID to share/unshare", "username to share to/unshare from");
        List<String> shareStatus = new ArrayList<>(List.of("editor", "viewer", "none"));
        String[] argsStatus = p.getArguments(shareStatus, "sharing permission");
        try {
            switch (argsStatus[0]) {
                case "editor":
                    users.shareCalendar(args[0], args[1], logs.getCurrUsername(), "edit");
                    p.displayResult("Successfully gave edit access of " + args[0] + " to user " + args[1] + "\n");
                    break;
                case "viewer":
                    users.shareCalendar(args[0], args[1], logs.getCurrUsername(), "view");
                    p.displayResult("Successfully gave view access of " + args[0] + " to user " + args[1] + "\n");
                    break;
                case "none":
                    users.shareCalendar(args[0], args[1], logs.getCurrUsername(), "none");
                    p.displayResult("Successfully unshared calendar " + args[0] + " with user " + args[1] + "\n");
                    break;
            }
        } catch (NotFoundException | InvalidCommandException e) {
            p.displayResult(e.toString());
        }
    }

    /** View all Owned Calendars
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     */
    private void viewAllCalendars(ShellPresenter p, CalendarManager cals, UserManager users, LoggingManager logs) {
        try {
            p.displayResult(cals.viewAllOwnedCalendars(users.getOwnedCalendars(logs.getCurrUsername())));
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** View all Shared Calendars
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     */
    private void viewSharedCalendars(ShellPresenter p, CalendarManager cals, UserManager users, LoggingManager logs) {
        try {
            p.displayResult(cals.viewSharedCalendars(users.getEditableCalendars(logs.getCurrUsername()),
                    users.getViewableCalendars(logs.getCurrUsername())));
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** Edit plans
     * @param p the presenter
     * @param cals the calendar manager
     * @param users the user manager
     * @param logs the logging manager
     */
    private void editPlans(ShellPresenter p, CalendarManager cals, UserManager users, LoggingManager logs) {
        String[] args = p.getArguments(false, "Calendar ID");
        try {
            if (users.validateCalendarForEditAccess(logs.getCurrUsername(), args[0])) {
                cals.setCurrCalendarID(args[0]);
                setMenuState(MenuState.EDIT_PLAN);
            }
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        } catch (NoPermissionException e2) {
            p.displayResult(e2.toString());
        }
    }
}
