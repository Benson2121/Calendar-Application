package GUI.Controller;
import GUI.ViewModel.*;
import javafx.scene.control.*;
import UseCase.*;

/**
 * A controller class that handles user requests in main menu tabs that contain TableViews
 * (ie. My Calendars, Shared With Me, History).
 * @author Richard Yin
 */
public class HandlerTables {
    private final UserManager users;
    private final LoggingManager logs;
    private final CalendarManager cals;
    private final HistoryManager hists;

    /**
     * Constructor for HandlerTables that acts as a setter.
     * @param users The UserManager to use
     * @param logs The LoggingManager to use
     * @param cals The CalendarManager to use
     * @param hists The HistoryManager to use
     */
    public HandlerTables(UserManager users, LoggingManager logs, CalendarManager cals, HistoryManager hists) {
        this.users = users;
        this.logs = logs;
        this.cals = cals;
        this.hists = hists;
    }

    /**
     * Create/refresh a TableView of users in the tab "My Calendars".
     * @param myCalendarsTable The TableView of users to populate
     */
    public void buildMyCalendarsTable(TableView<EntryCalendar> myCalendarsTable) {
        BuilderCalendarTable b = new ConcreteBuilderCalendarTable(users, logs, cals);
        DirectorCalendarTable d = new DirectorCalendarTable(b);
        myCalendarsTable.setItems(d.make("myCalendars"));
    }

    /**
     * Create/refresh a TableView of users in the tab "Shared With Me".
     * @param sharedWithMeTable The TableView of users to populate
     */
    public void buildSharedWithMeTable(TableView<EntryCalendar> sharedWithMeTable) {
        BuilderCalendarTable b = new ConcreteBuilderCalendarTable(users, logs, cals);
        DirectorCalendarTable d = new DirectorCalendarTable(b);
        sharedWithMeTable.setItems(d.make("sharedWithMe"));
    }

    /**
     * Create/refresh a TableView of histories in the tab "History".
     * @param historyTable The TableView of histories to populate
     */
    public void buildHistoryTable(TableView<EntryHistory> historyTable) {
        BuilderHistoryTable b = new ConcreteBuilderHistoryTable(hists, logs.getCurrUsername());
        b.buildActions();
        b.buildDates();
        b.buildTimes();
        b.buildHistoryEntries();
        historyTable.setItems(b.getResult());
    }
}
