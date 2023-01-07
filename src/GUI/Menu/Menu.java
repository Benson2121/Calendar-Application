package GUI.Menu;
import GUI.Controller.InterfaceController;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * An abstract presenter class representing any part of the GUI menu.
 *
 * Its subclasses delegate many methods to their controllers, which they access via dependency inversion, because:
 *  (a) javafx requires fxml files be linked to only 1 class (which are the Menu classes)
 *  (b) User inputs come in the form of clicking buttons
 *  (c) Buttons can only be linked to a method in the class that the fxml file is linked to (ie. a method in a Menu class)
 *
 * @author Richard Yin
 */
abstract public class Menu {
    /** Getter for the controller.
     * @return The controller */
    public abstract InterfaceController getContr();

    /** Show a fxml anchorpane.
     * @param anchorPane An anchorpane in fxml. */
    public void show(AnchorPane anchorPane) {
        anchorPane.setDisable(false);
        anchorPane.setOpacity(1);
    }

    /** Hide a fxml anchorpane.
     * @param anchorPane An anchorpane in fxml. */
    public void hide(AnchorPane anchorPane) {
        anchorPane.setDisable(true);
        anchorPane.setOpacity(0);
    }

    /**
     * Make a Label display an error message.
     * @param label The label to change
     * @param text The text of the error message
     */
    public void errorMessage(Label label, String text) {
        label.setText(text);
        label.setTextFill(Color.valueOf("#eb5c5c"));
    }

    /**
     * Make a Label display a success message.
     * @param label The label to change
     * @param text The text of the success message
     */
    public void successMessage(Label label, String text) {
        label.setText(text);
        label.setTextFill(Color.valueOf("#000000"));
    }

    /** This method is called in a menu upon switching to it. */
    abstract public void update();
}
