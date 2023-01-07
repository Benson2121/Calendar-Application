package GUI.ViewModel;

/**
 * A class representing an entry into a TableView of calendars (either myCalendars or SharedWithMe).
 * While this is a data class, the Oracle documentation says this is best practice for javafx, as shown here:
 * @see <a href="https://docs.oracle.com/javafx/2/ui_controls/table-view.htm">https://docs.oracle.com/javafx/2/ui_controls/table-view.htm</a>
 * @author Richard Yin
 */
public class EntryCalendar {
    private final String name;
    private final String description;
    private final String access;
    private final String id;

    /** Constructor for EntryCalendar, which acts as a setter.
     * @param name The calendar name.
     * @param description The calendar description.
     * @param access The calendar access.
     * @param id The calendarID.
     */
    public EntryCalendar(String name, String description, String access, String id) {
        this.name = name;
        this.description = description;
        this.access = access;
        this.id = id;
    }
    /** Getter for calendar name. */
    public String getName() {
        return name;
    }

    /** Getter for calendar description. */
    public String getDescription() {
        return description;
    }

    /** Getter for calendar access permission level. */
    public String getAccess() {
        return access;
    }

    /** Getter for calendarID. */
    public String getId() {
        return id;
    }
}
