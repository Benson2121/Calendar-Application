package GUI.ViewModel;

/**
 * A class representing an entry into a TableView of histories.
 * While this is a data class, the Oracle documentation says this is best practice for javafx, as shown here:
 * @see <a href="https://docs.oracle.com/javafx/2/ui_controls/table-view.htm">https://docs.oracle.com/javafx/2/ui_controls/table-view.htm</a>
 * @author Richard Yin
 */
public class EntryHistory {
    private final String date;
    private final String time;
    private final String user;
    private final String action;

    /** Constructor for EntryHistory, which acts as a setter.
     * @param date The History's date
     * @param time The History's time
     * @param user The History's user
     * @param action The History's action
     */
    public EntryHistory(String date, String time, String user, String action) {
        this.date = date;
        this.time = time;
        this.user = user;
        this.action = action;
    }

    /** Getter for history date. */
    public String getDate() {
        return date;
    }

    /** Getter for history time. */
    public String getTime() {
        return time;
    }

    /** Getter for username. */
    public String getUser() {
        return user;
    }

    /** Getter for action performed. */
    public String getAction() {
        return action;
    }
}
