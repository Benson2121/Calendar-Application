package GUI.Controller;
import GUI.Menu.*;
import Gateway.InterfaceSaver;
import UseCase.*;
import javafx.stage.*;

/**
 * An interface for a general controller class. Created for the purposes of dependency inversion.
 * @author Richard Yin
 */
public interface InterfaceController {
    /** Getter for calendar manager.
     * @return The calendar manager. */
    CalendarManager getCals();

    /** Getter for plan manager.
     * @return The plan manager. */
    PlanManager getPlans();

    /** Getter for label manager.
     * @return The label manager. */
    LabelManager getLabs();

    /** Getter for logging manager.
     * @return The logging manager. */
    LoggingManager getLogs();

    /** Getter for user manager.
     * @return The user  manager. */
    UserManager getUsers();

    /** Getter for history manager.
     * @return The history manager. */
    HistoryManager getHists();

    /** Getter for comment manager.
     * @return The comment manager. */
    CommentManager getComms();

    /** Getter for saver.
     * @return The saver. */
    InterfaceSaver getS();

    /** Called when this menu is switched to.*/
    void update();

    /** Setter for the menu class.
     * @param menu The menu to set to. */
    void setMenu(Menu menu);

    /** Setter for all manager classes, stage, and saver. */
    void setData(Stage stage, CalendarManager cals, PlanManager plans, LabelManager labs, LoggingManager logs, UserManager users, HistoryManager hists, CommentManager comms, InterfaceSaver s);
}
