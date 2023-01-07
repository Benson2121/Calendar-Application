package GUI.Menu;
import GUI.Controller.*;
import GUI.ViewModel.*;
import HelperClasses.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import java.io.IOException;

/**
 * The presenter class for the logged in GUI menu.
 * @author Richard Yin
 */
public class MenuLoggedIn extends Menu {
    private final InterfaceControllerMenuLoggedIn contr = new ControllerMenuLoggedIn();

    /** Getter for the controller.
     * @return The controller */
    @Override
    public InterfaceController getContr() {
        return contr;
    }

    /** This method is called in a menu upon switching to it. Initializes and resets some visual settings. */
    @Override
    public void update() {
        messageLoggedIn.setText("Logged in as " + getContr().getLogs().getCurrUsername());
        selectCalendarsTab();
        refreshSettingsTab();
        refreshAdminSettingsTab();
        refreshSaveButton();
        configureTables();
        showTabsBasedOnUser();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tabs
    @FXML private Label messageLoggedIn;
    @FXML private Button myCalendarsTab, sharedWithMeTab, historyTab, adminSettingsTab, settingsTab, saveButton, logOutButton;
    @FXML private AnchorPane myCalendarsMenu, sharedWithMeMenu, historyMenu, adminSettingsMenu, settingsMenu;

    private void configureTables() {
        usersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        historyDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        historyTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        historyUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        historyAction.setCellValueFactory(new PropertyValueFactory<>("Action"));
        historyTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        myCalendarsName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        myCalendarsDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        myCalendarsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        sharedWithMeName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        sharedWithMeDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        sharedWithMeAccess.setCellValueFactory(new PropertyValueFactory<>("Access"));
        sharedWithMeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /** Makes the Save Button glow yellow if there are unsaved changes, and grey otherwise. */
    private void refreshSaveButton() {
        String id = getContr().getS().changesMade() ? "save" : "saved";
        saveButton.setId(id);
    }

    /** Hide or show the admin settings tab. */
    private void showTabsBasedOnUser() {
        if (contr.getLogs().userLoggedIn()) {
            adminSettingsTab.setVisible(false);
            settingsTab.setLayoutY(416);
            saveButton.setLayoutY(491);
            logOutButton.setLayoutY(491);
        } else {
            adminSettingsTab.setVisible(true);
            settingsTab.setLayoutY(456);
            saveButton.setLayoutY(531);
            logOutButton.setLayoutY(531);
        }
    }

    /** Open the tab "My Calendars" and hide the others. */
    public void selectCalendarsTab() {
        toggleTab(myCalendarsTab, myCalendarsMenu);
        refreshMyCalendarsTab();
    }

    /** Open the tab "Shared With Me" and hide the others. */
    public void selectSharedTab() {
        toggleTab(sharedWithMeTab, sharedWithMeMenu);
        refreshSharedWithMeTab();
    }

    /** Open the tab "History" and hide the others. */
    public void selectHistoryTab() {
        toggleTab(historyTab, historyMenu);
        refreshHistoryTab();
    }

    /** Open the tab "Admin Settings" and hide the others. */
    public void selectAdminTab() {
        toggleTab(adminSettingsTab, adminSettingsMenu);
    }

    /** Open the tab "Settings" and hide the others. */
    public void selectSettingsTab() {
        toggleTab(settingsTab, settingsMenu);
    }

    /** Shows a menu (in the form of an AnchorPane) and highlight its corresponding tab (in the form of a button)
     * @param selected The selected button to highlight
     * @param selectedMenu The selected menu to show */
    private void toggleTab(Button selected, AnchorPane selectedMenu) {
        for (Button b : new Button[] {myCalendarsTab, sharedWithMeTab, historyTab, adminSettingsTab, settingsTab}) {
            b.setId("tab");
        }
        for (AnchorPane a : new AnchorPane[] {myCalendarsMenu, sharedWithMeMenu, historyMenu, adminSettingsMenu, settingsMenu}) {
            hide(a);
        }
        show(selectedMenu);
        selected.setId("tabSelected");
    }

    /**
     * Execute the button "log out".
     * @throws IOException If the logged out menu fxml file cannot load
     */
    public void logOut() throws IOException {
        contr.logOut();
    }

    /**
     * Execute the button "save".
     * @throws IOException If the system cannot serialize data and save it to a .ser file
     */
    public void save() throws IOException {
        contr.save();
        refreshSaveButton();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Settings Menu
    @FXML private TextField newUsername;
    @FXML private PasswordField newPassword, confirmPassword;
    @FXML private Label messageChangeUsername, messageChangePassword, messageConfirmPassword;

    /** Reset all the text labels in the settings tab. */
    public void refreshSettingsTab() {
        newUsername.setText(contr.getLogs().getCurrUsername());
        newPassword.setText(contr.getLogs().getCurrPassword());
        confirmPassword.setText("");
        messageChangeUsername.setText("");
        messageChangePassword.setText("");
        messageConfirmPassword.setText("");
    }

    /** Execute the button "change username". */
    public void changeUsername() throws IOException {
        contr.changeUsername(newUsername.getText(), messageChangeUsername, messageLoggedIn);
    }

    /** Execute the button "change password". */
    public void changePassword() throws IOException {
        contr.changePassword(newPassword.getText(), messageChangePassword);
    }

    /** Execute the button "delete my account". */
    public void deleteMyAccount() throws IOException {
        contr.deleteMyAccount(confirmPassword.getText(), messageConfirmPassword);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Admin Settings Menu
    @FXML private ListView<AnchorPane> usersList;

    /** Load/reload the list of users inside admin settings */
    private void refreshAdminSettingsTab() {
        contr.buildAdminSettingsList(usersList);
    }

    /**
     * Execute the button "ban".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    public void ban() throws InvalidCommandException, NotFoundException, IOException {
        contr.ban(usersList);
    }

    /**
     * Execute the button "unban".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    public void unban() throws InvalidCommandException, NotFoundException, IOException {
        contr.unban(usersList);
    }

    /**
     * Execute the button "delete account".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    public void deleteAccount() throws NotFoundException, InvalidCommandException, IOException, NoPermissionException {
        contr.deleteAccount(usersList);
        refreshAdminSettingsTab();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // History Menu
    @FXML private TableView<EntryHistory> historyTable;
    @FXML private TableColumn<EntryHistory, String> historyDate, historyTime, historyUser, historyAction;

    /** Load/reload the table of all history entries. */
    public void refreshHistoryTab() {
        contr.buildHistoryTable(historyTable);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // My Calendars Menu
    @FXML private TableView<EntryCalendar> myCalendarsTable;
    @FXML private TableColumn<EntryCalendar, String> myCalendarsName, myCalendarsDescription;

    /** Load/reload the table of all owned calendar entries. */
    public void refreshMyCalendarsTab() {
        contr.buildMyCalendarsTable(myCalendarsTable);
    }

    /** Execute the button "open calendar". */
    public void openCalendar() {
        // Not enough time to implement, requires a completely new menu
        System.out.println("Sorry, not implemented!");
    }

    /** Execute the button "edit properties". */
    public void editCalendarProperties() {
        contr.setCalendarNameDescriptionText(myCalendarsTable, calendarName, calendarDescription);
        showPopup(false);
    }

    /** Execute the button "share". */
    public void shareCalendar() {
        contr.shareCalendar(myCalendarsTable);
    }

    /** Execute the button "delete". */
    public void deleteCalendar() throws NotFoundException, NoPermissionException, IOException {
        contr.deleteCalendar(myCalendarsTable);
        refreshMyCalendarsTab();
        refreshSaveButton();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Shared With Me Menu
    @FXML private TableView<EntryCalendar> sharedWithMeTable;
    @FXML private TableColumn<EntryCalendar, String> sharedWithMeName, sharedWithMeDescription, sharedWithMeAccess;

    /** Load/reload the table of all shared calendar entries. */
    public void refreshSharedWithMeTab() {
        contr.buildSharedWithMeTable(sharedWithMeTable);
    }

    /** Execute the button "remove myself" */
    public void removeMyself() throws NotFoundException {
        contr.removeMyself(sharedWithMeTable);
        selectSharedTab();
        refreshSaveButton();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Create Calendar, Edit Calendar Property, and popups
    @FXML private AnchorPane popupShadowLeft, popupShadowRight, popup;
    @FXML private TextField calendarName;
    @FXML private TextArea calendarDescription;
    private boolean newCalendar;

    /** Execute the button "new calendar". */
    public void newCalendar() {
        showPopup(true);
        calendarName.setText("");
        calendarDescription.setText("");
    }

    /** Execute the button "confirm" in the popup.
     * @throws NotFoundException If the selected calendar can't be found (impossible)
     * @throws IOException If the system could not save History, which auto-updates and saves here */
    public void confirmCalendarNameDescription() throws NotFoundException, IOException {
        if (newCalendar)
            contr.newCalendar(calendarName.getText(), calendarDescription.getText());
        else
            contr.editProperties(myCalendarsTable, calendarName.getText(), calendarDescription.getText());
        refreshSaveButton();
        exitPopup();
        selectCalendarsTab();
    }

    /** Open the popup.
     * @param newCalendar Whether the user is creating a new calendar (true) or changing calendar properties (false) */
    public void showPopup(boolean newCalendar) {
        this.newCalendar = newCalendar;
        show(popupShadowLeft);
        show(popupShadowRight);
        show(popup);
    }
    /** Close the popup. */
    public void exitPopup() {
        hide(popupShadowLeft);
        hide(popupShadowRight);
        hide(popup);
    }
}
