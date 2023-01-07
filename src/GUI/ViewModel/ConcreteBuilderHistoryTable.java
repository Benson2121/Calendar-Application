package GUI.ViewModel;
import UseCase.*;
import javafx.collections.*;
import java.util.*;

/**
 * Implemented builder for a TableView of Histories.
 * @author Richard Yin
 */
public class ConcreteBuilderHistoryTable implements BuilderHistoryTable {
    private List<String> dates, times, actions;
    private final List<EntryHistory> entries = new ArrayList<>();
    private final HistoryManager hists;
    private final String username;

    /**
     * Constructor for ConcreteBuilderHistoryTable that acts as a setter.
     * @param hists The HistoryManager to use
     * @param username A given username to get histories from
     */
    public ConcreteBuilderHistoryTable(HistoryManager hists, String username) {
        this.hists = hists;
        this.username = username;
    }

    /** Generate all of a user's histories' dates. */
    @Override
    public void buildDates() {
        dates = hists.getHistoryPropertyAsList(username, "date");
    }

    /** Generate all of a user's histories' times. */
    @Override
    public void buildTimes() {
        times = hists.getHistoryPropertyAsList(username, "time");
    }

    /** Generate all of a user's histories' actions. */
    @Override
    public void buildActions() {
        actions = hists.getHistoryPropertyAsList(username, "action");
    }

    /** Combine the above three lists into a list of history entries. */
    @Override
    public void buildHistoryEntries() {
        for (int i = 0; i < dates.size(); i++) {
            entries.add(new EntryHistory(dates.get(i), times.get(i), username, actions.get(i)));
        }
    }

    /** Return the builder's final product.
     * @return The History entries, fit for display on a TableView. */
    @Override
    public ObservableList<EntryHistory> getResult() {
        return FXCollections.observableArrayList(entries);
    }
}
