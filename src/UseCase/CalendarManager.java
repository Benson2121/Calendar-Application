package UseCase;
import Entities.Calendar;
import HelperClasses.NotFoundException;

import java.io.*;
import java.util.*;

/** Use Case class CalendarManager, which stores all instances of Calendar and contains methods to manipulate them
 */
public class CalendarManager extends Saveable implements Serializable {
    private Map<String, Calendar> calendars ; // Map of CalendarID to Calendar
    private transient String currCalendarID = null;

    /** Adds an instance of calendar to calendars.
     * @param c the Calendar
     */
    private void addCalendar(Calendar c) {
        calendars.put(c.getCalendarID(), c);
    }

    /** Constructor for CalendarManager.
     */
    public CalendarManager(){
        calendars = new HashMap<>();
    }

    /** Get an instance of Calendar.
     * @param calendarID The ID of the calendar
     * @return a Calendar
     */
    public Calendar getCalendar(String calendarID) throws NotFoundException {
        if (!calendars.containsKey(calendarID))
            throw new NotFoundException(calendarID);
        else
            return calendars.get(calendarID);
    }

    /** Create a Calendar Instance with given name and description.
     * @param name name of the Calendar instance
     * @param description Description of the Calendar instance
     */
    public String createCalendar(String name, String description, String username) {
        Calendar c = new Calendar(name, description);
        List<String> ids = new ArrayList<>(calendars.keySet());
        c.setCalendarID(c.assignID(name, ids, username));
        addCalendar(c);
        makeChange();
        return c.getCalendarID();
    }

    /** Delete a Calendar stored in the calendars.
     * @param calendarID the CalendarID of the Calendar instance
     */
    public void deleteCalendar(String calendarID) {
        calendars.remove(calendarID);
        makeChange();
    }

    /** Change name of calendar.
     * @param newName the name to change to
     * @param calendarID The ID of the calendar
     */
    public void changeCalendarName(String newName, String calendarID) throws NotFoundException {
        Calendar c = getCalendar(calendarID);
        c.setName(newName);
        makeChange();
    }

    /** Change name of calendar.
     * @param description the new description to change to
     * @param calendarID The ID of the calendar
     */
    public void changeCalendarDescription(String description, String calendarID) throws NotFoundException {
        Calendar c = getCalendar(calendarID);
        c.setDescription(description);
        makeChange();
    }

    /** Set the currCalendarID to the input.
     * @param currCalendarID the current Calendar ID
     */
    public void setCurrCalendarID(String currCalendarID) {
        if (calendars.containsKey(currCalendarID)) {
            this.currCalendarID = currCalendarID;
        }
    }

    /** View all the owned Calendars.
     * @param ownedCalendarIDs list of all owned Calendar's ID's
     * @return A String with all the Owned Calendars ID
     * @throws NotFoundException - cannot find what user is expecting
     */
    public String viewAllOwnedCalendars(List<String> ownedCalendarIDs) throws NotFoundException {
        StringBuilder s = new StringBuilder("Owned Calendars:\n");
        s.append("----------------------------------\n");
        for (String calendarID:ownedCalendarIDs){
            s.append(getCalendar(calendarID).toString());
            s.append("\n\n");
        }
        return s.toString();
    }

    /** View all the Editable Shared Calendars.
     * @param editableCalendarIDs list of all editable shared Calendar's ID's
     * @return A String with all the editable Shared Calendar's ID
     * @throws NotFoundException  - cannot find what user is expecting
     */
    private String viewEditableCalendars(List<String> editableCalendarIDs) throws NotFoundException {
        StringBuilder s = new StringBuilder("Editable Calendars:\n");
        s.append("----------------------------------\n");
        for (String calendarID: editableCalendarIDs){
            s.append(getCalendar(calendarID).toString());
            s.append("\n\n");
        }
        return s.toString();
    }

    /** View all the Viewable Shared Calendars.
     * @param viewableCalendarIDs list of all viewable shared Calendar's ID's
     * @return A String with all the viewable Shared Calendar's ID
     * @throws NotFoundException  - cannot find what user is expecting
     */
    private String viewViewableCalendars(List<String> viewableCalendarIDs) throws NotFoundException {
        StringBuilder s = new StringBuilder("Viewable Calendars:\n");
        s.append("----------------------------------\n");
        for (String calendarID: viewableCalendarIDs){
            s.append(getCalendar(calendarID).toString());
            s.append("\n\n");
        }
        return s.toString();
    }

    /** View all the Shared Calendars.
     * @param editableCalendarIDs list of all editable shared Calendar's ID's
     * @param viewableCalendarIDs list of all viewable shared Calendar's ID's
     * @return A String with all the Shared Calendars' ID
     */
    public String viewSharedCalendars(List<String> editableCalendarIDs, List<String> viewableCalendarIDs) throws NotFoundException {
        return viewEditableCalendars(editableCalendarIDs) + viewViewableCalendars(viewableCalendarIDs);
    }

    /** Check if Calendar exists.
     * @param calendarID Calendar ID
     * @return a Boolean if the Calendar exists
     */
    public boolean containCalendar(String calendarID) {
        return calendars.containsKey(calendarID);

    }

    /** Deletes Calendars.
     * @param calendarIDs List of Calendar ID to delete
     */
    public void deleteCalendars(List<String> calendarIDs){
        for (String calendarID:calendarIDs){
            calendars.remove(calendarID);
        }
        makeChange();
    }

    /** Get the currCalendarID.
     * @return a String of currCalendarID
     */
    public String getCurrCalendarID(){return this.currCalendarID;}

    /** Save the current state.
     * @throws IOException error regarding files
     */
    @Override
    public void save() throws IOException {
        getSaver().save(this, "calendars");
    }

    /** Load the current state.
     * @throws IOException error regarding files
     * @throws ClassNotFoundException cannot find class in classpath
     */
    @Override
    public void load() throws IOException, ClassNotFoundException {
        calendars = getSaver().load(this, "calendars").calendars;
    }

    /** Get name of Calendar.
     * @param calendarID ID of Calendar in question
     * @return a String of corresponding Calendar's name
     * @throws NotFoundException - the input is unrecognized
     */
    public String getCalendarName(String calendarID) throws NotFoundException {
        try {
            return calendars.get(calendarID).getName();
        } catch (NullPointerException e) {
            throw new NotFoundException(calendarID);
        }
    }

    /** Get description of Calendar.
     * @param calendarID ID of Calendar in question
     * @return a String of corresponding Calendar's description
     * @throws NotFoundException - the input is unrecognized
     */
    public String getCalendarDescription(String calendarID) throws NotFoundException {
        try {
            return calendars.get(calendarID).getDescription();
        } catch (NullPointerException e) {
            throw new NotFoundException(calendarID);
        }
    }
}
