package GUI.ViewModel;
import javafx.collections.ObservableList;

/**
 * Builder for a TableView of Histories.
 * @author Richard Yin
 */
public interface BuilderHistoryTable {
    /** Generate all of a user's histories' dates. */
    void buildDates();

    /** Generate all of a user's histories' times. */
    void buildTimes();

    /** Generate all of a user's histories' actions. */
    void buildActions();

    /** Combine the above three lists into a list of history entries. */
    void buildHistoryEntries();

    /** Return the builder's final product.
     * @return The History entries, fit for display on a TableView. */
    ObservableList<EntryHistory> getResult();
}
