package GUI.Controller;
import GUI.ViewModel.EntryCalendar;
import HelperClasses.*;
import javafx.scene.control.TableView;
import UseCase.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;

/**
 * A controller class that handles user requests in the myCalendars and sharedWithMe tabs of the main menu.
 * @author Richard Yin
 */
public class HandlerCalendars {
    private final CalendarManager cals;
    private final PlanManager plans;
    private final LabelManager labs;
    private final UserManager users;
    private final LoggingManager logs;
    private final HistoryManager hists;
    private final CommentManager comms;

    /**
     * Constructor for HandlerCalendars that acts as a setter.
     * @param cals The CalendarManager to use
     * @param plans The PlanManager to use
     * @param labs The LabelManager to use
     * @param users The UserManager to use
     * @param logs The LoggingManager to use
     * @param hists The HistoryManager to use
     * @param comms The CommentManager to use
     */
    public HandlerCalendars(CalendarManager cals, PlanManager plans, LabelManager labs, UserManager users,
                            LoggingManager logs, HistoryManager hists, CommentManager comms) {
        this.cals = cals;
        this.plans = plans;
        this.labs = labs;
        this.users = users;
        this.logs = logs;
        this.hists = hists;
        this.comms = comms;
    }

    /**
     * Execute the button "share calendar".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     */
    public void shareCalendar(TableView<EntryCalendar> myCalendarsTable) {
        // Not enough time to implement, requires a new popup
        System.out.println("Sorry, not implemented!");
    }

    /**
     * Execute the button "delete calendar".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @throws NotFoundException If a calendar is not found (impossible, as only existing calendars are displayed)
     * @throws NoPermissionException If a calendar cannot be accessed (impossible, as only accessible calendars are displayed)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    public void deleteCalendar(TableView<EntryCalendar> myCalendarsTable) throws NotFoundException, NoPermissionException, IOException {
        EntryCalendar cal = getSelectedCalendarEntry(myCalendarsTable);
        if (cal != null) {
            cals.deleteCalendar(cal.getId());
            hists.addHistory(logs.getCurrUsername(), "Deleted calendar " + cal.getName());
            users.deleteCalendar(cal.getId(), logs.getCurrUsername());
            for (String planID : plans.getAllPlanIDs(cal.getId())) {
                labs.handleDeletePlan(planID);
                comms.handleDeletePlan(planID);
            }
            plans.clearAllPlans(cal.getId());
            plans.makeChange();
        }
    }

    /**
     * Execute the button "remove myself".
     * @param sharedWithMeTable The TableView of calendars, from which the user selects a calendar
     * @throws NotFoundException If a calendar is not found (impossible, as only existing calendars are displayed)
     */
    public void removeMyself(TableView<EntryCalendar> sharedWithMeTable) throws NotFoundException {
        EntryCalendar cal = getSelectedCalendarEntry(sharedWithMeTable);
        if (cal != null) {
            users.deleteOrRemoveAccess(cal.getId(), logs.getCurrUsername());
            users.makeChange();
        }
    }

    /**
     * Execute the button "new calendar".
     * @param name The name of the new calendar
     * @param description The description of the new calendar
     * @throws NotFoundException If a calendar's generated ID can't be found (impossible given logic)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    public void newCalendar(String name, String description) throws NotFoundException, IOException {
        String id = cals.createCalendar(name, description, logs.getCurrUsername());
        users.addCalendar(id, logs.getCurrUsername());
        plans.handleCreateCalendar(id);
        hists.addHistory(logs.getCurrUsername(), "Created calendar " + name);
        hists.makeChange();
    }

    /**
     * Execute the button "edit properties".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @param name The updated name of the selected calendar
     * @param description The updated description of the selected calendar
     * @throws NotFoundException If the calendar cannot be found (impossiblem as only existing calendars and displayed)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    public void editProperties(TableView<EntryCalendar> myCalendarsTable, String name, String description) throws NotFoundException, IOException {
        EntryCalendar cal = getSelectedCalendarEntry(myCalendarsTable);
        if (cal != null) {
            if (!cal.getName().equals(name)) {
                hists.addHistory(logs.getCurrUsername(), "Changed name of calendar " + cal.getName() + " to " + name);
                cals.changeCalendarName(name, cal.getId());
                cals.makeChange();
            }
            if (!cal.getDescription().equals(description)) {
                hists.addHistory(logs.getCurrUsername(), "Changed description of calendar " + cal.getName());
                cals.changeCalendarDescription(description, cal.getId());
                cals.makeChange();
            }
        }
    }

    /**
     * Set the popup's Name and Description fields to the selected calendar's Name and Description.
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @param calendarName The text field containing the calendar's name
     * @param calendarDescription The text area containing the calendar's description
     */
    public void setCalendarNameDescriptionText(TableView<EntryCalendar> myCalendarsTable, TextField calendarName, TextArea calendarDescription) {
        EntryCalendar cal = getSelectedCalendarEntry(myCalendarsTable);
        if (cal != null) {
            calendarName.setText(cal.getName());
            calendarDescription.setText(cal.getDescription());
        }
    }

    /**
     * Retrieve what calendar entry is currently being selected by the user, given a TableView of calendar entries.
     * @param calTable The TableView of calendars, from which the user selects a calendar
     * @return A calendar entry if the user has selected something, otherwise null
     */
    private EntryCalendar getSelectedCalendarEntry(TableView<EntryCalendar> calTable) {
        int i = calTable.getSelectionModel().getSelectedIndex();
        return i < 0 ? null : calTable.getItems().get(i);
    }
}
