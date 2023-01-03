package GUI;
import GUI.Menu.*;
import Gateway.*;
import UseCase.*;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.*;
import java.io.IOException;
import java.util.Objects;

/**
 * The starting controller class of the GUI, responsible for starting initialize data and launching the GUI.
 * @author Richard Yin
 */
public class Main extends Application {
    private final CalendarManager cals = new CalendarManager();
    private final PlanManager plans = new PlanManager();
    private final LabelManager labs = new LabelManager();
    private final HistoryManager hists = new HistoryManager();
    private final UserManager users = new UserManager();
    private final LoggingManager logs = new LoggingManager();
    private final CommentManager comms = new CommentManager();
    private final InterfaceSaver s = new Saver();

    public Main() {
        cals.setSaver(s);
        plans.setSaver(s);
        labs.setSaver(s);
        hists.setSaver(s);
        users.setSaver(s);
        comms.setSaver(s);
    }

    /**
     * Run the GUI of the Calendar application.
     * @param args The same arguments passed from public static void main.
     */
    public void run(String[] args) {
        Application.launch(args);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Handle the beginning of the GUI Calendar application.
     * @param stage The stage to display the GUI
     * @throws Exception If anything goes wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        configureStage(stage);
        if (load()) {
            System.out.println("Loaded previous database");
            startMenu(stage);
        } else {
            System.out.println("No previous database");
            // Prompt the user to create a new admin user first
            startMenu(stage);
        }
    }

    /** Load pre-existing data if possible.
     * @return A boolean indicating whether old data was loaded. */
    private boolean load() {
        try {
            cals.load();
            plans.load();
            labs.load();
            hists.load();
            users.load();
            comms.load();
            return true;
        } catch (IOException | ClassNotFoundException e1) {
            return false;
        }
    }

    /**
     * Display the logged out menu as the first page in the GUI.
     * @param stage The stage to display the page on
     * @throws IOException if there is an issue loading the fxml file
     */
    private void startMenu(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/GUI/Assets/loggedOut.fxml")));
        stage.setScene(new Scene(loader.load()));
        stage.show();
        MenuLoggedOut m = loader.getController();
        m.getContr().setData(stage, cals, plans, labs, logs, users, hists, comms, s);
        m.getContr().setMenu(m);
    }

    /** Configure the dimensions and properties of the GUI screen.
     * @param stage The stage to be displayed by the GUI. */
    private void configureStage(Stage stage) {
        int screenWidth = 1200;
        int screenHeight = 800;
        String title = "CSC207 Project, Group 0253";

        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        stage.setTitle(title);
        stage.setResizable(false);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        Main m = new Main();
        m.run(args);
    }
}
