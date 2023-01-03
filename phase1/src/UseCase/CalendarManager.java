package UseCase;
import Entities.*;
import Entities.Calendar;

import javax.swing.*;
import java.io.*;
import java.util.*;

/** Use Case class CalendarManager, which stores all instances of Calendar and contains methods to manipulate them
 */
public class CalendarManager implements Serializable {
    private Map<String, Calendar> calendars ; // Map of CalendarID to Calendar
    private String currCalendarID = null;

    /** addHistory() adds an instance of calendar to calendars.
     * @param c the Calendar
     */
    private void addCalendar(Calendar c) {
        calendars.put(c.getCalendarID(), c);
    }
    public CalendarManager(){
        calendars = new HashMap<>();
    }
    /** getCalendar() get an instance of Calendar
     *
     * @param calendarID The idea of the calendar
     * @return a Calendar
     */
    public Calendar getCalendar(String calendarID) {
        Calendar c = calendars.get(calendarID);
        return c;
    }

    /** createCalendar() create a Calendar Instance with given name and description
     *
     * @param name name of the Calendar instance
     * @param description Description of the Calendar instance
     */
    public String createCalendar(String name, String description) {
        Calendar c = new Calendar(name, description);
        // ID = name for now. Only assigning special ID to calendar for phase 2, when 1+ calendars are allowed
        c.setCalendarID(name);
        addCalendar(c);
        return "Successfully created calendar " + name + " with ID " + c.getCalendarID() + ". \n";
    }

//    private String createCalendarHelper(String name){
//        if (!calendars.containsKey(name)){
//            return name;
//        } else{
//            Integer count = 0;
//            for (Map.Entry<String, Calendar> entry : calendars.entrySet()) {
//                count++;
//                return createCalendarHelper(name.concat(count.toString()));
//            }
//        }
//    }

    /** deleteCalendar() Delete a Calendar stored in the calendars
     *
     * @param calendarID the CalendarID of the Calendar instance
     */

    public String deleteCalendar(String calendarID) {
        calendars.remove(calendarID);
        return "Successfully deleted calendar. \n";
    }

//    public String addEditor(String calendarID, User u) throws CalendarNotFoundException {
//        Calendar c = getCalendar(calendarID);
//        c.getEditor().add(u);
//        return "Successfully added editor " + u.getUsername();
//    }
//
//    public String deleteEditor(String calendarID, User u) throws CalendarNotFoundException {
//        Calendar c = getCalendar(calendarID);
//        c.getEditor().remove(u);
//        return "Successfully deleted editor " + u.getUsername();
//    }
//
//    public String addPlanner(String calendarID, User u) throws CalendarNotFoundException {
//        Calendar c = getCalendar(calendarID);
//        c.getPlanner().add(u);
//        return "Successfully added planner " + u.getUsername();
//    }
//
//    public String deletePlanner(String calendarID, User u) throws CalendarNotFoundException {
//        Calendar c = getCalendar(calendarID);
//        return "Successfully deleted planner " + u.getUsername();
//    }

//    //check if someone is an editor
//    public boolean isEditor(String calendarID, User u) {
//        Calendar c = getCalendar(calendarID);
//        boolean flag = c.getEditor().contains(u);
//        return flag;
//    }
//
//    //check if someone is a planner
//    public boolean isPlanner(String calendarID, User u) {
//        Calendar c = getCalendar(calendarID);
//        boolean flag = c.getPlanner().contains(u);
//        return flag;
//    }
//
//    //check if someone is the owner, one calendar only has one owner
//    public boolean isOwner(String calendarID, User u) {
//        Calendar c = getCalendar(calendarID);
//        boolean flag = c.getOwner().equals(u);
//        return flag;
//    }

//    public void searchCalendar(String CalendarID) throws NotFoundException {
//        Calendar c = getCalendar(CalendarID);
//        int searchedTimes = c.getSearched();
//        c.setSearched(searchedTimes + 1);
//    }

//    public void addPlan(String CalendarID, Plan p) throws CalendarNotFoundException {
//        Calendar c = getCalendar(CalendarID);
//        c.getPlans().add(p);
//    }
//
//    public void deletePlan(String CalendarID, Plan p) throws CalendarNotFoundException {
//        Calendar c = getCalendar(CalendarID);
//        c.getPlans().remove(p);
//    }

    /** addOwner() add owner to the attribute of Calendar owner
     *
     * @param CalendarID the CalendarID that you want add owner to
     * @param u User which is the owner of the calendar
     */
    public void addOwner(String CalendarID, User u) {
        Calendar c = getCalendar(CalendarID);
        c.setOwner(u);
    }

    /** setCurrCalendarID() set the currCalendarID to the input
     *
     * @param currCalendarID the current Calendar ID
     */
    public boolean setCurrCalendarID(String currCalendarID) {
        if (calendars.containsKey(currCalendarID)) {
            this.currCalendarID = currCalendarID;
            return true;
        }
        return false;
    }

    /** getCurrCalendarID() get the currCalendarID
     *
     * @return a String of currCaledarID
     */
    public String getCurrCalendarID(){return this.currCalendarID;}


}
