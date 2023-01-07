package GUI.Controller;
import GUI.Menu.*;
import Gateway.InterfaceSaver;
import UseCase.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import java.io.IOException;
import java.util.Objects;

/**
 * An abstract controller class that handles a given menu's complex user requests (ie. they require the retrieval or
 * editing of data from the Use Case classes)
 * @author Richard Yin
 */
abstract public class Controller implements InterfaceController {
    private Stage stage;
    private CalendarManager cals;
    private PlanManager plans;
    private LabelManager labs;
    private LoggingManager logs;
    private UserManager users;
    private HistoryManager hists;
    private CommentManager comms;
    private InterfaceSaver s;

    /** Getter for stage.
     * @return The stage. */
    public Stage getStage() {
        return stage;
    }

    /** Getter for calendar manager.
     * @return The calendar manager. */
    public CalendarManager getCals() {
        return cals;
    }

    /** Getter for plan manager.
     * @return The plan manager. */
    public PlanManager getPlans() {
        return plans;
    }

    /** Getter for label manager.
     * @return The label manager. */
    public LabelManager getLabs() {
        return labs;
    }

    /** Getter for logging manager.
     * @return The logging manager. */
    public LoggingManager getLogs() {
        return logs;
    }

    /** Getter for user manager.
     * @return The user  manager. */
    public UserManager getUsers() {
        return users;
    }

    /** Getter for history manager.
     * @return The history manager. */
    public HistoryManager getHists() {
        return hists;
    }

    /** Getter for comment  manager.
     * @return The comment manager. */
    public CommentManager getComms() {
        return comms;
    }

    /** Getter for saver.
     * @return The saver. */
    public InterfaceSaver getS() {
        return s;
    }

    /** Setter for all manager classes, stage, and saver. */
    public void setData(Stage stage, CalendarManager cals, PlanManager plans, LabelManager labs, LoggingManager logs,
                        UserManager users, HistoryManager hists, CommentManager comms, InterfaceSaver s) {
        this.stage = stage;
        this.cals = cals;
        this.plans = plans;
        this.labs = labs;
        this.logs = logs;
        this.users = users;
        this.hists = hists;
        this.comms = comms;
        this.s = s;
    }

    /** Setter for the menu class.
     * @param menu The menu to set to. */
    abstract public void setMenu(Menu menu);

    /** Called when this menu is switched to.*/
    abstract public void update();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Switch into a new menu.
     * @param stage The stage that needs to display the new menu
     * @param menuFileName The file name of the new menu to switch to
     * @throws IOException If the menu fxml file can be found, but not loaded
     */
    public void switchMenu(Stage stage, String menuFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(menuFileName)));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        Menu m = loader.getController();
        m.getContr().setData(stage, cals, plans, labs, logs, users, hists, comms, s);
        m.getContr().setMenu(m);
        m.getContr().update();
        m.update();
        stage.show();
    }
}
