package GUI.Controller;
import GUI.Menu.Menu;
import GUI.Menu.MenuLoggedOut;
import HelperClasses.*;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.io.IOException;

/**
 * Controller class that handles user requests in the logged out menu.
 * @author Richard Yin
 */
public class ControllerMenuLoggedOut extends Controller implements InterfaceControllerMenuLoggedOut {
    private MenuLoggedOut menu;

    /** Getter for menu.
     * @return The menu */
    public MenuLoggedOut getMenu() {
        return menu;
    }

    /** Setter for menu.
     * @param menu The menu to set to */
    @Override
    public void setMenu(Menu menu) {
        this.menu = (MenuLoggedOut) menu;
    }

    /** Called when this menu is switched to. */
    @Override
    public void update() {
        // Calls nothing - it was decided leaving this empty was better than having a special if branch for this menu that didn't call update()
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Execute the button "log in".
     * @param username The username text field
     * @param password The password text field
     * @param menuMessage The menu message label
     * @throws IOException If the user's histories cannot properly update and save, or the menu cannot be loaded
     */
    @Override
    public void logIn(TextField username, PasswordField password, Label menuMessage) throws IOException {
        try {
            getLogs().logIn(getUsers().authenticateUser(username.getText(), password.getText()));
            getHists().addHistory(username.getText(), "Logged in");
            switchMenu(getStage(), "/GUI/Assets/loggedIn.fxml");
        } catch (NotFoundException e) {
            getMenu().errorMessage(menuMessage, "Invalid username or password. Please try again.");
        } catch (NoPermissionException e) {
            getMenu().errorMessage(menuMessage, "Cannot log in because username " + username.getText() + " is banned.");
        }
    }

    /**
     * Execute the button "new user".
     * @param username The username text field
     * @param password The password text field
     * @param menuMessage The menu message label
     */
    @Override
    public void newUser(TextField username, PasswordField password, Label menuMessage) {
        try {
            getUsers().createUser(username.getText(), password.getText());
            getHists().addHistory(username.getText(), "Created user " + username.getText());
            getMenu().successMessage(menuMessage, "Created user \"" + username.getText() + "\" with password \"" + password.getText() + "\"");
            getUsers().save();
            getHists().save();
        } catch (DuplicateUsernameException | IOException e) {
            getMenu().errorMessage(menuMessage, e.toString());
        }
    }

    /**
     * Execute the button "exit", which closes the program.
     */
    @Override
    public void exit() {
        Platform.exit();
        System.exit(0);
    }
}
