package GUI.ViewModel;
import HelperClasses.NotFoundException;
import UseCase.*;
import javafx.collections.*;
import java.util.*;

/**
 * Implemented builder for a TableView of Calendars.
 * @author Richard Yin
 */
public class ConcreteBuilderCalendarTable implements BuilderCalendarTable {
    private final Map<String, List<String>> calendarIDs = new HashMap<>();
    private final List<EntryCalendar> calendarEntries = new ArrayList<>();
    private final UserManager users;
    private final LoggingManager logs;
    private final CalendarManager cals;

    /**
     * Constructor for ConcreteBuilderCalendarTable that acts as a setter.
     * @param users The UserManager to use
     * @param logs The LoggingManager to use
     * @param cals The CalendarManager to use
     */
    public ConcreteBuilderCalendarTable(UserManager users, LoggingManager logs, CalendarManager cals) {
        this.users = users;
        this.logs = logs;
        this.cals = cals;
    }

    /** Generate all calendarIDs owned by a user. */
    @Override
    public void buildOwnedCalendarIDs() {
        calendarIDs.put("Editor", new ArrayList<>());
        calendarIDs.put("Viewer", new ArrayList<>());
        try {
            calendarIDs.put("Owner", users.getOwnedCalendars(logs.getCurrUsername()));
        } catch (NotFoundException e) {
            calendarIDs.put("Owner", new ArrayList<>());
        }
    }

    /** Generate all calendarIDs accessible but not owned by a user. */
    @Override
    public void buildSharedCalendarIDs() {
        calendarIDs.put("Owner", new ArrayList<>());
        try {
            calendarIDs.put("Editor", users.getEditableCalendars(logs.getCurrUsername()));
            calendarIDs.put("Viewer", users.getViewableCalendars(logs.getCurrUsername()));
        } catch (NotFoundException e) {
            calendarIDs.put("Editor", new ArrayList<>());
            calendarIDs.put("Viewer", new ArrayList<>());
        }
    }

    /** Generate all calendar entries based on the generated calendarIDs user. */
    @Override
    public void buildCalendarEntries() {
        for (String access : new String[] {"Owner", "Editor", "Viewer"}) {
            calendarEntries.addAll(getCalendarEntries(calendarIDs.get(access), access));
        }
    }

    /**
     * Helper function that turns a list of CalendarIDs into a list of EntryCalendars.
     * @param calendarIDs The list of calendarIDs
     * @param access The sharing permission level in all calendars of calendarIDs
     * @return A displayable list of EntryCalendars
     */
    private List<EntryCalendar> getCalendarEntries(List<String> calendarIDs, String access) {
        List<EntryCalendar> l = new ArrayList<>();
        for (String calendarID : calendarIDs) {
            String name, description;
            try {
                name = cals.getCalendarName(calendarID);
                description = cals.getCalendarDescription(calendarID);
            } catch (NotFoundException e) {
                name = description = "Error: couldn't find calendar";
            }
            l.add(new EntryCalendar(name, description, access, calendarID));
        }
        return l;
    }

    /** Return the builder's final product.
     * @return The Calendar entries, fit for display on a TableView. */
    @Override
    public ObservableList<EntryCalendar> getResult() {
        return FXCollections.observableArrayList(calendarEntries);
    }
}
