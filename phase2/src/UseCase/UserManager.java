package UseCase;
import Entities.*;
import HelperClasses.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

/** Use Case Class UserManager, which stores all instances of User and contains method to manipulate them
 *
 */
public class UserManager extends Saveable implements Serializable {
    private Map<String, User> users = new HashMap<>();  // A map of username to User

    /** getUser() Get a User.
     * @param username User's username
     * @return User
     * @throws NotFoundException when User with username doesn't exist
     */
    public User getUser(String username) throws NotFoundException {
        User u = users.get(username);
        if (u == null)
            throw new NotFoundException(username);
        else
            return u;
    }

    /** setPassword() sets a new password.
     * @param username User's username
     * @param newPassword User's new password
     */
    public void setPassword(String username, String newPassword) throws NotFoundException, IOException {
        users.get(username).setPassword(newPassword);
        save();
    }

    /** setUsername() sets a new username.
     * @param username User's username
     * @param newUsername User's new username
     */
    public void setUsername(String username, String newUsername) throws NotFoundException, DuplicateUsernameException, IOException {
        if (users.containsKey(newUsername))
            throw new DuplicateUsernameException(username);
        users.get(username).setUsername(newUsername);
        users.put(newUsername, users.get(username));
        users.remove(username);
        save();
    }

    /** getAllRegularUsernames() returns all Usernames of Regular Users.
     * @return List of all Regular Users' Usernames
     */
    public List<String> getAllRegularUsernames() {
        List<String> s = new ArrayList<>();
        for (User u : users.values()) {
            if (!u.isAdmin())
                s.add(u.getUsername());
        }
        return s.stream().sorted(Comparator.comparing((String x) -> x, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
    }

    /** getAllUsernames() returns all Usernames of all Users.
     * @return List of all Usernames
     */
    public List<String> getAllUsernames() {
        List<String> s = new ArrayList<>();
        for (User u : users.values()) {
            s.add(u.getUsername());
        }
        return s.stream().sorted(Comparator.comparing((String x) -> x, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
    }

    /** authenticateUser() returns a specified user, given correct credentials.
     * @param username User's username
     * @param password User's password
     * @return A user
     * @throws NoPermissionException if the user is banned
     * @throws NotFoundException if the credentials don't match
     */
    public User authenticateUser(String username, String password) throws NoPermissionException, NotFoundException {
        for (User u : users.values()) {
            if (u.authenticate(username, password)) {
                if (isBanned(username))
                    throw new NoPermissionException(username, "logIn");
                return u;
            }
        }
        throw new NotFoundException(username);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** isBanned() check if the user with given username is banned
     * @param username the username of the user you want to check
     * @return a boolean show whether the user is banned
     * @throws NotFoundException if the username doesn't exist
     */
    public boolean isBanned(String username) throws NotFoundException {
        return !getUser(username).isAdmin() && ((RegularUser) getUser(username)).getIsBanned();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void addUser(User newUser){
        users.put(newUser.getUsername(), newUser);
    }
    /** createUser() create a RegularUser and add to the users
     * @param username the username of the user
     * @param password the password for the username
     * @throws DuplicateUsernameException if the given username already exists in the database
     */
    public void createUser(String username, String password) throws DuplicateUsernameException {
        if (users.containsKey(username))
            throw new DuplicateUsernameException(username);
        else {
            RegularUser newUser = new RegularUser(username, password);
            addUser(newUser);
        }
    }

    /** createAdminUser() create an AdminUser and add to the users
     * @param username the username of the AdminUSer
     * @param password the password of the AdminUser
     * @throws DuplicateUsernameException if the username already exists in the database
     */
    public void createAdminUser(String username, String password) throws DuplicateUsernameException {
        if (users.containsKey(username))
            throw new DuplicateUsernameException(username);
        else {
            AdminUser newAdminUser = new AdminUser(username, password);
            addUser(newAdminUser);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** deleteUser() Delete the User from the users
     * @param username the username of the user you want to delete
     * @param deleteSelf a boolean to show whether you want to delete yourself
     * @throws NotFoundException if the username doesn't exist
     * @throws InvalidCommandException if the user cannot be deleted (they are an admin and user isn't deleting their own account)
     */
    public void deleteUser(String username, boolean deleteSelf) throws NotFoundException, InvalidCommandException, IOException, NoPermissionException {
        if (deleteSelf || !getUser(username).isAdmin()) {
            clearCalendars(username);
            users.remove(username);
            save();
        } else
            throw new InvalidCommandException("deleteUser");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** banUser() ban the User
     * @param username the user with the username you want to ban
     * @throws NotFoundException if the username doesn't exist
     * @throws InvalidCommandException if the user cannot be banned (they are an admin)
     */
    public void banUser(String username) throws NotFoundException, InvalidCommandException, IOException {
        if (getUser(username).isAdmin()) {
            throw new InvalidCommandException("banUser");
        }
        ((RegularUser) getUser(username)).setIsBanned(true);
        save();
    }

    /** unbanUser() unban the user with the username
     * @param username the user's username you want to unban
     * @throws NotFoundException if the username doesn't exist
     * @throws InvalidCommandException if the user cannot be unbanned (they are an admin)
     */
    public void unbanUser(String username) throws NotFoundException, InvalidCommandException, IOException {
        if (getUser(username).isAdmin()) {
            throw new InvalidCommandException("banUser");
        }
        ((RegularUser) getUser(username)).setIsBanned(false);
        save();
    }

    /** Add an owned Calendar to the User
     * @param calendarID the calendar's ID being add to the user's owned Calendars
     * @param username the user's username you want to add Calendar to
     * @throws NotFoundException - cannot find what user is expecting
     */
    public void addCalendar(String calendarID, String username) throws NotFoundException {
        User u = getUser(username);
        u.addOwnedCalendar(calendarID);
        makeChange();
    }

    /** Delete Calendar with calendarID from the User
     * @param calendarID the calendar's ID being removed from the user's owned and shared Calendars
     * @throws NotFoundException - cannot find what user is expecting
     */
    public void deleteCalendar(String calendarID, String username) throws NotFoundException, NoPermissionException {
        User u = getUser(username);
        if (!u.ownsCalendar(calendarID)){
            throw new NoPermissionException(username, "delete not owned Calendar");
        }
        for (User allUser:users.values()){
            allUser.removeCalendar(calendarID);
        }
        makeChange();
    }

    /** Clear all the Calendars in the User with given username, delete owned calendars and clear shared calendars
     * @param username the user's username that you want to clear the calendars for
     * @throws NotFoundException - cannot find what user is expecting
     */
    public void clearCalendars(String username) throws NotFoundException, NoPermissionException {
        User u = getUser(username);
        for (String calendarID:u.getOwnedCalendarsID()){
            deleteCalendar(calendarID, username);
        }
        u.clearSharedCalendars();
        makeChange();
    }

    /** Share a calendar with another user
     * @param calendarID The calendarID to share
     * @param targetUser The target user's username
     * @param access What access the target user should have. Either "edit", "view", or "none"
     * @throws NotFoundException If the calendar, target user, or calendar owner cannot be found
     * @throws InvalidCommandException If the user doesn't own a calendar or the user is sharing with themself
     */
    public void shareCalendar(String calendarID, String targetUser, String calOwner, String access) throws NotFoundException, InvalidCommandException {
        User u = getUser(targetUser);
        if (targetUser.equals(calOwner) || !getUser(calOwner).ownsCalendar(calendarID))
            throw new InvalidCommandException("share calendar");
        switch (access) {
            case "edit":
                u.editSharingPermission(calendarID, true);
                break;
            case "view":
                u.editSharingPermission(calendarID, false);
                break;
            case "none":
                u.removeCalendar(calendarID);
                break;
        }
        makeChange();
    }

    /** Withdraw the sharing from the User with username for the Calendar with calendarID
     * @param calendarID the calendar's ID you want to unshare
     * @param username username of the person you do not want to share to
     */
    public void deleteOrRemoveAccess(String calendarID, String username) throws NotFoundException {
        User u = getUser(username);
        u.removeCalendar(calendarID);
        makeChange();
    }

    /** getOwnedCalendars() returns all owned Calendars' IDs.
     * @param username User's username
     * @return List of all Owned Calendars' IDs
     * @throws NotFoundException can't find username
     */
    public List<String> getOwnedCalendars(String username) throws NotFoundException {
        User u = getUser(username);
        return u.getOwnedCalendarsID();
    }

    /** getEditableCalendars() returns all shared editable Calendars' IDs.
     * @param username User's username
     * @return List of all shared editable Calendars' IDs
     * @throws NotFoundException can't find username
     */
    public List<String> getEditableCalendars(String username) throws NotFoundException {
        User u = getUser(username);
        return u.getEditableCalendarsID();
    }

    /** getViewableCalendars() returns all shared viewable Calendars' IDs.
     * @param username User's username
     * @return List of all shared viewable Calendars' IDs
     * @throws NotFoundException can't find username
     */
    public List<String> getViewableCalendars(String username) throws NotFoundException {
        User u = getUser(username);
        return u.getViewableCalendarsID();
    }

    /** validateCalendarForEditAccess() returns a specified user has edit access to Calendar with CalenderID.
     * @param username User's username
     * @param calendarID A CalenderID
     * @return true/false if User has edit access
     * @throws NoPermissionException if the user does not have edit permission
     * @throws NotFoundException if cannot find calendar
     */
    public boolean validateCalendarForEditAccess(String username, String calendarID) throws NotFoundException, NoPermissionException {
        User u = getUser(username);
        if (!u.ownsCalendar(calendarID) && !u.canEditCalendar(calendarID))
            throw new NoPermissionException(username, "Edit");
        else return true;
    }

    /** Save the current state.
     * @throws IOException error regarding files
     */
    public void save() throws IOException {
        if (getSaver() != null) {
            getSaver().save(this, "users");
        }
    }

    /** Load the current state.
     * @throws IOException error regarding files
     * @throws ClassNotFoundException cannot find class in classpath
     */
    public void load() throws IOException, ClassNotFoundException {
        users = getSaver().load(this, "users").users;
    }

}
