package GUI.Menu;
import GUI.Controller.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.*;

/**
 * The controller class for the logged out GUI menu.
 * @author Richard Yin
 */
public class MenuLoggedOut extends Menu {
    private final InterfaceControllerMenuLoggedOut contr = new ControllerMenuLoggedOut();

    /** Getter for the controller.
     * @return The controller */
    @Override
    public InterfaceController getContr() {
        return contr;
    }

    /** This method is called in a menu upon switching to it. */
    @Override
    public void update() {
        // Calls nothing - it was decided leaving this empty was better than having a special if branch for this menu that didn't call update()
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private AnchorPane subMenu1;
    @FXML private AnchorPane subMenu2;
    @FXML private Button confirm;
    @FXML private Label menuMessage;
    @FXML private TextField username;
    @FXML private PasswordField password;

    /** Display subMenu1, which has the sign in and sign out buttons. */
    public void getSubMenu1() {
        show(subMenu1);
        hide(subMenu2);
        menuMessage.setText("");
        username.setText("");
        password.setText("");
    }

    /**
     * Display subMenu2, either the sign in menu or the sign up menu.
     * @param menuType The string that the confirm button is set to (either "Register" or "Log in")
     */
    private void getSubMenu2(String menuType) {
        show(subMenu2);
        hide(subMenu1);
        username.setFocusTraversable(false);
        password.setFocusTraversable(false);
        confirm.setText(menuType);
    }

    /** Display the sign in menu. */
    public void getSubMenuSignIn() {
        getSubMenu2("Log in");
    }

    /** Display the sign up menu. */
    public void getSubMenuSignUp() {
        getSubMenu2("Register");
    }

    /** Handle when the user clicks the confirm button - either create a new user or log in.
     * @throws IOException If a new user cannot be saved.
     */
    public void confirm() throws IOException {
        if (confirm.getText().equals("Log in"))
            contr.logIn(username, password, menuMessage);
        else
            contr.newUser(username, password, menuMessage);
    }

    /** Exit the program. */
    public void exit() {
        contr.exit();
    }
}
