package GUI.Controller;
import javafx.scene.control.*;
import java.io.IOException;

/**
 * An interface for the "menu logged out" controller class. Created for the purposes of dependency inversion.
 * @author Richard Yin
 */
public interface InterfaceControllerMenuLoggedOut extends InterfaceController {
    /**
     * Execute the button "log in".
     * @param username The username text field
     * @param password The password text field
     * @param menuMessage The menu message label
     * @throws IOException If the user's histories cannot properly update and save, or the menu cannot be loaded
     */
    void logIn(TextField username, PasswordField password, Label menuMessage) throws IOException;

    /**
     * Execute the button "new user".
     * @param username The username text field
     * @param password The password text field
     * @param menuMessage The menu message label
     */
    void newUser(TextField username, PasswordField password, Label menuMessage);

    /**
     * Execute the button "exit", which closes the program.
     */
    void exit();
}
