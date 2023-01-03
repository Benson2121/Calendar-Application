package Entities;

/** An entity class corresponding to a Calendar.
 */
public class Calendar extends NameDuplicatable {
    private String calendarID;
    private String name;
    private String description;

    /** Constructor for Calendar.
     * @param name the Calendar's name
     * @param description the Calendar's description
     */
    public Calendar(String name, String description){
        this.name = name;
        this.description = description;
    }

    /** Set the calendar's current name.
     * @param name the Calendar's name
     */
    public void setName(String name){
        this.name = name;
    }

    /** Set the calendar's current description.
     * @param description the Calendar's description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /** Get the calendar's current name.
     * @return the Calendar's name
     */
    public String getName(){
        return name;
    }

    /** Get the calendar's current description.
     * @return the Calendar's description
     */
    public String getDescription(){
        return description;
    }

    /** Get the calendar's CalendarID.
     * @return the Calendar's CalendarID
     */
    public String getCalendarID(){
        return calendarID;
    }

    /** Set the calendar's CalendarID.
     * @param CalendarID the Calendar's new CalendarID
     */
    public void setCalendarID(String CalendarID){
        calendarID = CalendarID;
    }

    /** String representation of a Calendar.
     * @return Calendar as a String
     */
    @Override
    public String toString(){
        return "CalendarID: " + calendarID +
                "\nname: " + name +
                "\ndescription: " + description;
    }
}
