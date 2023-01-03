package Entities;
import java.util.*;

/** An entity class corresponding to a Calendar.
 */
public class Calendar extends NameDuplicatable {
    private String calendarID;
    private String name;
    private String description; // Not yet implemented a way to see it, will do in phase 2
    private User owner; // For phase 2, right now we have one calendar per user only
    private List<User> editor; // For phase 2
    private List<User> planner; // For phase 2

    public Calendar(String name, String description){
        calendarID = name;
        this.name = name;
        this.description = description;
        this.editor = new ArrayList<>();
        this.planner = new ArrayList<>();
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getCalendarID(){
        return calendarID;
    }
    public void setCalendarID(String CalendarID){
        calendarID = CalendarID;
    }
    public User getOwner(){
        return owner;
    }
    public void setOwner(User u){
        owner = u;
    }

    // Unused
    public List<User> getEditor(){
        return editor;
    }
    public List<User> getPlanner(){
        return planner;
    }
    @Override
    public String toString(){
        return "Calendar: " + calendarID +
                "\nname: " + name +
                "\ndescription: " + description;
    }
}
