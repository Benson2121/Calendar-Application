package GUI.Controller;
import GUI.Menu.*;
import GUI.Menu.Menu;
import GUI.ViewModel.*;
import HelperClasses.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.*;

/**
 * Facade controller class that handles user requests in the logged in menu.
 * @author Richard Yin
 */
public class ControllerMenuLoggedIn extends Controller implements InterfaceControllerMenuLoggedIn {
    private MenuLoggedIn menu;

    /** Getter for the associated menu.
     * @return The menu. */
    public MenuLoggedIn getMenu() {
        return menu;
    }

    /** Setter for the associated menu.
     * @param menu The menu to set it to. Must be a MenuLoggedIn. */
    @Override
    public void setMenu(Menu menu) {
        this.menu = (MenuLoggedIn) menu;
    }

    /** Called when a menu is switched to. Injects the correct data into the inner classes. */
    @Override
    public void update() {
        mainTab = new HandlerMainTab(getCals(), getHists(), getLabs(), getUsers(), getPlans(), getLogs());
        settings = new HandlerSettings(getUsers(), getLogs(), getHists(), getMenu(), getCals(), getPlans(), getLabs());
        adminSettings = new HandlerAdminSettings(getUsers(), getHists(), getLogs());
        tables = new HandlerTables(getUsers(), getLogs(), getCals(), getHists());
        calendars = new HandlerCalendars(getCals(), getPlans(), getLabs(), getUsers(), getLogs(), getHists(), getComms());
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Main Menu
    private HandlerMainTab mainTab;

    /**
     * Execute the button "log out".
     * @throws IOException If the logged out menu fxml file cannot load
     */
    @Override
    public void logOut() throws IOException {
        mainTab.logOut();
        switchMenu(getStage(), "/GUI/Assets/loggedOut.fxml");
    }

    /**
     * Execute the button "save".
     * @throws IOException If the system cannot serialize data and save it to a .ser file
     */
    @Override
    public void save() throws IOException {
        mainTab.save();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Settings
    private HandlerSettings settings;

    /** Execute the button "change username".
     * @param newUsername The text field where a new username is entered
     * @param messageChangeUsername The text label next to the "new username" text field
     * @param messageLoggedIn The text label in the logged-in menu that displays the user's name
     * @throws IOException If the system cannot save after setting the new username
     */
    @Override
    public void changeUsername(String newUsername, Label messageChangeUsername, Label messageLoggedIn) throws IOException {
        if (settings.changeUsername(newUsername, messageChangeUsername, messageLoggedIn))
            save();
    }

    /** Execute the button "change password".
     * @param newPassword The password field where a new password is entered
     * @param messageChangePassword The text label next to the "new password" field
     * @throws IOException If the system cannot save after setting the new password
     */
    @Override
    public void changePassword(String newPassword, Label messageChangePassword) throws IOException {
        if (settings.changePassword(newPassword, messageChangePassword))
            save();
    }

    /** Execute the button "delete my account".
     * @param confirmPassword The password field where the user confirms their current password
     * @param messageConfirmPassword The text label next to the "confirm password" field
     * @throws IOException If the system cannot save after deleting the new account
     */
    @Override
    public void deleteMyAccount(String confirmPassword, Label messageConfirmPassword) throws IOException {
        if (settings.deleteMyAccount(confirmPassword, messageConfirmPassword)) {
            save();
            switchMenu(getStage(), "loggedOut.fxml");
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Admin Settings
    private HandlerAdminSettings adminSettings;

    /**
     * Populate the ListView of users in the tab "Admin Settings".
     * @param usersList The ListView of users to build
     */
    @Override
    public void buildAdminSettingsList(ListView<AnchorPane> usersList) {
        adminSettings.buildAdminSettingsList(usersList);
    }

    /**
     * Execute the button "ban".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    @Override
    public void ban(ListView<AnchorPane> usersList) throws InvalidCommandException, NotFoundException, IOException {
        adminSettings.toggleBan(usersList, true);
    }

    /**
     * Execute the button "unban".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    @Override
    public void unban(ListView<AnchorPane> usersList) throws InvalidCommandException, NotFoundException, IOException {
        adminSettings.toggleBan(usersList, false);
    }

    /**
     * Execute the button "delete account".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    @Override
    public void deleteAccount(ListView<AnchorPane> usersList) throws NotFoundException, InvalidCommandException, IOException, NoPermissionException {
        adminSettings.deleteAccount(usersList);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tables
    private HandlerTables tables;

    /**
     * Populate the TableView of calendars in the tab "My Calendars".
     * @param myCalendarsTable The TableView of calendars to build
     */
    @Override
    public void buildMyCalendarsTable(TableView<EntryCalendar> myCalendarsTable) {
        tables.buildMyCalendarsTable(myCalendarsTable);
    }

    /**
     * Populate the TableView of calendars in the tab "Shared With Me".
     * @param sharedWithMeTable The TableView of calendars to build
     */
    @Override
    public void buildSharedWithMeTable(TableView<EntryCalendar> sharedWithMeTable) {
        tables.buildSharedWithMeTable(sharedWithMeTable);
    }

    /**
     * Populate the TableView of histories in the tab "History".
     * @param historyTable The TableView of histories to build
     */
    @Override
    public void buildHistoryTable(TableView<EntryHistory> historyTable) {
        tables.buildHistoryTable(historyTable);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Calendars
    private HandlerCalendars calendars;

    /**
     * Execute the button "share calendar".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     */
    @Override
    public void shareCalendar(TableView<EntryCalendar> myCalendarsTable) {
        calendars.shareCalendar(myCalendarsTable);
    }

    /**
     * Execute the button "delete calendar".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @throws NotFoundException If a calendar is not found (impossible, as only existing calendars are displayed)
     * @throws NoPermissionException If a calendar cannot be accessed (impossible, as only accessible calendars are displayed)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    @Override
    public void deleteCalendar(TableView<EntryCalendar> myCalendarsTable) throws NotFoundException, NoPermissionException, IOException {
        calendars.deleteCalendar(myCalendarsTable);
    }

    /**
     * Execute the button "remove myself".
     * @param sharedWithMeTable The TableView of calendars, from which the user selects a calendar
     * @throws NotFoundException If a calendar is not found (impossible, as only existing calendars are displayed)
     */
    @Override
    public void removeMyself(TableView<EntryCalendar> sharedWithMeTable) throws NotFoundException {
        calendars.removeMyself(sharedWithMeTable);
    }

    /**
     * Execute the button "new calendar".
     * @param name The name of the new calendar
     * @param description The description of the new calendar
     * @throws NotFoundException If a calendar's generated ID can't be found (impossible given logic)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    @Override
    public void newCalendar(String name, String description) throws NotFoundException, IOException {
        calendars.newCalendar(name, description);
    }

    /**
     * Execute the button "edit properties".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @param name The updated name of the selected calendar
     * @param description The updated description of the selected calendar
     * @throws NotFoundException If the calendar cannot be found (impossiblem as only existing calendars and displayed)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    @Override
    public void editProperties(TableView<EntryCalendar> myCalendarsTable, String name, String description) throws NotFoundException, IOException {
        calendars.editProperties(myCalendarsTable, name, description);
    }

    /**
     * Set the popup's Name and Description fields to the selected calendar's Name and Description.
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @param calendarName The text field containing the calendar's name
     * @param calendarDescription The text area containing the calendar's description
     */
    @Override
    public void setCalendarNameDescriptionText(TableView<EntryCalendar> myCalendarsTable, TextField calendarName, TextArea calendarDescription) {
        calendars.setCalendarNameDescriptionText(myCalendarsTable, calendarName, calendarDescription);
    }
}
