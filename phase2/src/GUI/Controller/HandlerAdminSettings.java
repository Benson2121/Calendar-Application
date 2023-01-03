package GUI.Controller;
import GUI.ViewModel.*;
import HelperClasses.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.io.IOException;
import java.util.*;
import UseCase.*;

/**
 * A controller class that handles user requests in the admin settings tab of the main menu.
 * @author Richard Yin
 */
public class HandlerAdminSettings {
    private final UserManager users;
    private final HistoryManager hists;
    private final LoggingManager logs;

    /**
     * Constructor for HandlerAdminSettings that acts as a setter.
     * @param users The UserManager to use
     * @param hists The HistoryManager to use
     * @param logs The LoggingManager to use
     */
    public HandlerAdminSettings(UserManager users, HistoryManager hists, LoggingManager logs) {
        this.users = users;
        this.hists = hists;
        this.logs = logs;
    }

    /**
     * Create/refresh a list of users in a displayable format (where users are represented as AnchorPanes) to be
     * displayed in the ListView of users in the tab "Admin Settings".
     * @param usersList The ListView of AnchorPanes to build the users/AnchorPanes into
     */
    public void buildAdminSettingsList(ListView<AnchorPane> usersList) {
        BuilderUsersList b = new ConcreteBuilderUsersList(users);
        b.buildUsernames();
        b.buildBanStatuses();
        b.buildLabels();
        b.buildImageViews();
        b.buildAnchorPanes();
        usersList.getItems().clear();
        usersList.getItems().addAll(b.getResult());
    }

    /**
     * Visually modify the user's current selection of usernames depending on whether they are being banned.
     * @param usersList The ListView of AnchorPanes that the user can select.
     * @param ban Whether the selected users are being banned.
     * @return The list of usernames that the user has currently selected.
     */
    private List<String> getUserSelection(ListView<AnchorPane> usersList, boolean ban) {
        List<String> selected = new ArrayList<>();
        for (AnchorPane a : usersList.getSelectionModel().getSelectedItems()) {
            // Hide the image of the ban-hammer
            ImageView i = (ImageView) a.getChildren().get(1);
            i.setVisible(ban);
            // Make the username font bold
            Label l = (Label) a.getChildren().get(0);
            l.setFont(Font.font(Font.getDefault().toString(), ban ? FontWeight.BOLD : FontWeight.NORMAL, 18));
            // Collect all selected usernames
            String username = l.getText();
            selected.add(username);
        }
        return selected;
    }

    /**
     * Similar to getUserSelection() with two arguments, but does not visually modify any of the user's selection.
     * @param usersList The ListView of AnchorPanes that the user can select.
     * @return The list of usernames that the user has currently selected.
     */
    private List<String> getUserSelection(ListView<AnchorPane> usersList) {
        List<String> selected = new ArrayList<>();
        for (AnchorPane a : usersList.getSelectionModel().getSelectedItems()) {
            Label l = (Label) a.getChildren().get(0);
            String username = l.getText();
            selected.add(username);
        }
        return selected;
    }

    /**
     * Ban or unban a selection of users.
     * @param usersList The ListView of AnchorPanes that the user can select.
     * @param ban Whether to ban or unban a selection of users
     * @throws InvalidCommandException If an admin user is selected (shouldn't happen, as only regular users are displayed)
     * @throws NotFoundException If a username cannot be found (shouldn't happen, as only existing users are displayed)
     * @throws IOException If the system cannot save after banning/unbanning
     */
    public void toggleBan(ListView<AnchorPane> usersList, boolean ban) throws InvalidCommandException, NotFoundException, IOException {
        List<String> selected = getUserSelection(usersList, ban);
        // Unban all usernames
        for (String username : selected) {
            if (ban) {
                users.banUser(username);
                hists.addHistory(logs.getCurrUsername(), "Banned user " + username);
            } else {
                users.unbanUser(username);
                hists.addHistory(logs.getCurrUsername(), "Unbanned user " + username);
            }
        }
    }

    /**
     * Delete a selection of users.
     * @param usersList The ListView of AnchorPanes that the user can select.
     * @throws NotFoundException If a username cannot be found (shouldn't happen, as only existing users are displayed)
     * @throws InvalidCommandException If the user is deleting themself (shouldn't happen, as regular users cannot access this menu and admin users are not displayed)
     * @throws IOException If the system cannot save after deleting a user
     * @throws NoPermissionException If an admin user is selected (shouldn't happen, as only regular users are displayed)
     */
    public void deleteAccount(ListView<AnchorPane> usersList) throws NotFoundException, InvalidCommandException, IOException, NoPermissionException {
        List<String> selected = getUserSelection(usersList);
        for (String username : selected) {
            users.deleteUser(username, false);
            hists.addHistory(logs.getCurrUsername(), "Deleted account " + username);
            hists.clearHistoriesByUser(username);
            // Will not delete user calendars...
        }
    }
}
