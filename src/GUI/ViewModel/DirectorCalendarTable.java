package GUI.ViewModel;
import javafx.collections.ObservableList;

/**
 * The Director for BuilderHistoryTable.
 * @author Richard Yin
 */
public class DirectorCalendarTable {
    private final BuilderCalendarTable builder;

    /**
     * Constructor for DirectoryCalendarTable, which acts as a setter.
     * @param builder The Builder to use.
     */
    public DirectorCalendarTable(BuilderCalendarTable builder) {
        this.builder = builder;
    }

    /**
     * Generate the data for a calendar table.
     * @param type The calendar table to generate data for: either "myCalendars" or "sharedWithMe"
     * @return A list of EntryCalendars displayable in a TableView.
     */
    public ObservableList<EntryCalendar> make(String type) {
        if (type.equals("myCalendars"))
            builder.buildOwnedCalendarIDs();
        else
            builder.buildSharedCalendarIDs();
        builder.buildCalendarEntries();
        return builder.getResult();
    }
}
