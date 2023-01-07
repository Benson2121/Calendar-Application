package GUI.ViewModel;
import javafx.collections.*;

/**
 * Builder for a TableView of Calendars.
 * @author Richard Yin
 */
public interface BuilderCalendarTable {
    /** Generate all calendarIDs owned by a user. */
    void buildOwnedCalendarIDs();

    /** Generate all calendarIDs accessible but not owned by a user. */
    void buildSharedCalendarIDs();

    /** Generate all calendar entries based on the generated calendarIDs user. */
    void buildCalendarEntries();

    /** Return the builder's final product.
     * @return The Calendar entries, fit for display on a TableView. */
    ObservableList<EntryCalendar> getResult();
}
