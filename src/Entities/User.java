package Entities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** An abstract entity class representing a user of the program.
 * Can be divided into RegularUser and AdminUser, whose differences have to do with banning.
 */
public abstract class User implements Serializable {
    private String username;
    private String password;
    private List<String> ownedCalendars; // CalendarIDs that a user owns
    private List<String> viewableCalendars; // CalendarIDs that a user can view
    private List<String> editableCalendars; // CalendarIDs that a user can edit

    /** Constructor for User.
     * @param username the User's username
     * @param password the User's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.ownedCalendars = new ArrayList<>();
        this.viewableCalendars = new ArrayList<>();
        this.editableCalendars = new ArrayList<>();
    }

    /** Getter for username.
     * @return The username.
     */
    public String getUsername() {
        return this.username;
    }

    /** Getter for password.
     * @return The password.
     */
    public String getPassword() {
        return this.password;
    }

    /** Setter for username.
     * @param username The username to set to.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Setter for password.
     * @param password The password to set to.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Verify whether a given username-password combination matches this user's username-password info/
     * @param username The input username
     * @param password The input password
     * @return A boolean indicating whether the two arguments match
     */
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /** Return a string representation of the user.
     */
    @Override
    public String toString() {
        return username + " | " + password + " | " + (isAdmin() ? "Admin" : "User");
    }

    /** Return whether the user is an admin.
     */
    public abstract boolean isAdmin();

    /** Check whether a calendarID is in the ownedCalendars.
     * @return a Boolean indicating the presence of the calendarID in ownedCalendars
     */
    public boolean ownsCalendar(String calendarID) {
        return ownedCalendars.contains(calendarID);
    }

    /** Getter for a List of owned calendarIDs.
     * @return a List of owned calendarIDs
     */
    public List<String> getOwnedCalendarsID(){
        return new ArrayList<>(ownedCalendars);
    }

    /** Getter for a List of viewable calendarIDs.
     * @return a List of viewable calendarIDs
     */
    public List<String> getViewableCalendarsID() {
        return new ArrayList<>(viewableCalendars);
    }

    /** Check whether the calendarID is in the viewableCalendars.
     * @param calendarID the calendarID to check
     * @return a Boolean indicating the presence of calendarID in viewable Calendars
     */
    public boolean canViewCalendar(String calendarID){
        return viewableCalendars.contains(calendarID);
    }

    /** Getter for a List of editable calendarIDs.
     * @return a List of editable calendarIDs */
    public List<String> getEditableCalendarsID() {
        return new ArrayList<>(editableCalendars);
    }

    /** Check whether the calendarID is in the editableCalendars.
     * @param calendarID the calendarID to check
     * @return a Boolean indicating the presence of the calendarID in the editableCalendars
     */
    public boolean canEditCalendar(String calendarID){
        return editableCalendars.contains(calendarID);
    }

    /** Clear both editableCalendars and viewableCalendars.
     */
    public void clearSharedCalendars() {
        viewableCalendars.clear();
        editableCalendars.clear();
    }

    /** Add a calendarID to viewableCalendars or editCalendars based on the editAccess
     * @param calendarID the calendarID to add
     * @param editAccess a Boolean indicating the whether the calendar is editable or not
     */
    public void editSharingPermission(String calendarID, boolean editAccess) {
        if (!editAccess)
            viewableCalendars.add(calendarID);
        else
            editableCalendars.add(calendarID);
    }

    /** Add a calendarID to ownedCalendars
     * @param calendarID the calendarID to add
     */
    public void addOwnedCalendar(String calendarID){
        ownedCalendars.add(calendarID);
    }

    /** remove a calendarID from ownedCalendars or viewableCalendars or editableCalendars
     * @param calendarID the calendarID to remove
     */
    public void removeCalendar(String calendarID) {
        ownedCalendars.remove(calendarID);
        editableCalendars.remove(calendarID);
        viewableCalendars.remove(calendarID);
    }

    // Unused
    private static int idCounter = 0;
    public User() {
        this.username = User.giveId();
        this.password = "";
    }
    private static String giveId() {
        idCounter ++;
        return String.valueOf(idCounter);
    }
}
