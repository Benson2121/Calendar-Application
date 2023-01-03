package GUI.Controller;
import GUI.ViewModel.*;
import HelperClasses.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

/**
 * An interface for the "menu logged in" controller class. Created for the purposes of dependency inversion.
 * @author Richard Yin
 */
public interface InterfaceControllerMenuLoggedIn extends InterfaceController {
    /**
     * Execute the button "log out".
     * @throws IOException If the logged out menu fxml file cannot load
     */
    void logOut() throws IOException;

    /**
     * Execute the button "save".
     * @throws IOException If the system cannot serialize data and save it to a .ser file
     */
    void save() throws IOException;

    /** Execute the button "change username".
     * @param newUsername The text field where a new username is entered
     * @param messageChangeUsername The text label next to the "new username" text field
     * @param messageLoggedIn The text label in the logged-in menu that displays the user's name
     * @throws IOException If the system cannot save after setting the new username
     */
    void changeUsername(String newUsername, Label messageChangeUsername, Label messageLoggedIn) throws IOException;

    /** Execute the button "change password".
     * @param newPassword The password field where a new password is entered
     * @param messageChangePassword The text label next to the "new password" field
     * @throws IOException If the system cannot save after setting the new password
     */
    void changePassword(String newPassword, Label messageChangePassword) throws IOException;

    /** Execute the button "delete my account".
     * @param confirmPassword The password field where the user confirms their current password
     * @param messageConfirmPassword The text label next to the "confirm password" field
     * @throws IOException If the system cannot save after deleting the new account
     */
    void deleteMyAccount(String confirmPassword, Label messageConfirmPassword) throws IOException;

    /**
     * Populate the ListView of users in the tab "Admin Settings".
     * @param usersList The ListView of users to build
     */
    void buildAdminSettingsList(ListView<AnchorPane> usersList);

    /**
     * Execute the button "ban".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    void ban(ListView<AnchorPane> usersList) throws InvalidCommandException, NotFoundException, IOException;

    /**
     * Execute the button "unban".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    void unban(ListView<AnchorPane> usersList) throws InvalidCommandException, NotFoundException, IOException;

    /**
     * Execute the button "delete account".
     * @throws InvalidCommandException If the selected user is an admin (should be impossible, only regularUsers are shown)
     * @throws NotFoundException If the user can't be found (should be impossible, only existing users are shown)
     * @throws IOException If the change cannot be saved for some reason (should not happen)
     */
    void deleteAccount(ListView<AnchorPane> usersList) throws NotFoundException, InvalidCommandException, IOException, NoPermissionException;

    /**
     * Populate the TableView of histories in the tab "History".
     * @param historyTable The TableView of histories to build
     */
    void buildHistoryTable(TableView<EntryHistory> historyTable);

    /**
     * Populate the TableView of calendars in the tab "My Calendars".
     * @param myCalendarsTable The TableView of calendars to build
     */
    void buildMyCalendarsTable(TableView<EntryCalendar> myCalendarsTable);

    /**
     * Populate the TableView of calendars in the tab "Shared With Me".
     * @param sharedWithMeTable The TableView of calendars to build
     */
    void buildSharedWithMeTable(TableView<EntryCalendar> sharedWithMeTable);

    /**
     * Execute the button "share calendar".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     */
    void shareCalendar(TableView<EntryCalendar> myCalendarsTable);

    /**
     * Execute the button "delete calendar".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @throws NotFoundException If a calendar is not found (impossible, as only existing calendars are displayed)
     * @throws NoPermissionException If a calendar cannot be accessed (impossible, as only accessible calendars are displayed)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    void deleteCalendar(TableView<EntryCalendar> myCalendarsTable) throws NotFoundException, NoPermissionException, IOException;

    /**
     * Execute the button "remove myself".
     * @param sharedWithMeTable The TableView of calendars, from which the user selects a calendar
     * @throws NotFoundException If a calendar is not found (impossible, as only existing calendars are displayed)
     */
    void removeMyself(TableView<EntryCalendar> sharedWithMeTable) throws NotFoundException;

    /**
     * Execute the button "new calendar".
     * @param name The name of the new calendar
     * @param description The description of the new calendar
     * @throws NotFoundException If a calendar's generated ID can't be found (impossible given logic)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    void newCalendar(String name, String description) throws NotFoundException, IOException;

    /**
     * Execute the button "edit properties".
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @param name The updated name of the selected calendar
     * @param description The updated description of the selected calendar
     * @throws NotFoundException If the calendar cannot be found (impossiblem as only existing calendars and displayed)
     * @throws IOException If the system cannot save History, which auto-updates and saves here
     */
    void editProperties(TableView<EntryCalendar> myCalendarsTable, String name, String description) throws NotFoundException, IOException;

    /**
     * Set the popup's Name and Description fields to the selected calendar's Name and Description.
     * @param myCalendarsTable The TableView of calendars, from which the user selects a calendar
     * @param calendarName The text field containing the calendar's name
     * @param calendarDescription The text area containing the calendar's description
     */
    void setCalendarNameDescriptionText(TableView<EntryCalendar> myCalendarsTable, TextField calendarName, TextArea calendarDescription);
}
