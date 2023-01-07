package GUI.Controller;
import UseCase.*;
import java.io.IOException;

/**
 * A controller class that handles user requests in the left tab of the main menu.
 * @author Richard Yin
 */
public class HandlerMainTab {
    private final CalendarManager cals;
    private final HistoryManager hists;
    private final LabelManager labs;
    private final UserManager users;
    private final PlanManager plans;
    private final LoggingManager logs;

    /**
     * Constructor for HandlerMainTab that acts as a setter.
     * @param cals The CalendarManager to use
     * @param hists The HistoryManager to use
     * @param labs The LabelManager to use
     * @param users The UserManager to use
     * @param plans The PlanManager to use
     * @param logs The LoggingManager to use
     */
    public HandlerMainTab(CalendarManager cals, HistoryManager hists, LabelManager labs, UserManager users,
                          PlanManager plans, LoggingManager logs) {
        this.cals = cals;
        this.hists = hists;
        this.labs = labs;
        this.users = users;
        this.plans = plans;
        this.logs = logs;
    }

    /**
     * Execute the button "log out".
     * @throws IOException If the logged out menu fxml file cannot load
     */
    public void logOut() throws IOException {
        hists.addHistory(logs.getCurrUsername(), "Logged out");
        logs.logOut();
        // Haven't implemented confirm logout option when there are unsaved changes
    }

    /**
     * Execute the button "save".
     * @throws IOException If the system cannot serialize data and save it to a .ser file
     */
    public void save() throws IOException {
        cals.save();
        hists.save();
        labs.save();
        users.save();
        plans.save();
        cals.saveAll();
    }
}
