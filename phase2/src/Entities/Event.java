package Entities;
import java.time.*;
import java.util.*;

/** An entity class corresponding to an event, i.e. a plan.
 */
public class Event extends Plan {
    private LocalDateTime startTime;

    /** Constructor for Event.
     * @param name the Event's name
     * @param description the Event's description
     */
    public Event(String name, String description) {
        super(name, description);
    }

    /** Set the event's start time.
     * @param startTime the Calendar's start time
     */
    public void setStartTime(LocalDateTime startTime) throws DateTimeException {
        this.startTime = startTime;
    }

    /** Get the event's start time.
     * @return the Calendar's start time
     */
    public LocalDateTime getStartTime(){
        return startTime;
    }

    /** String representation of an Event.
     * @return Event as a String
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("Event | " + getName() + " (" + getPlanID() + ")");
        if (!getDescription().equals(""))
            str.append(" | \"").append(getDescription()).append("\"");

        if (getEndTime() == null)
            str.append("\n└ No Time Assigned");
        else
            str.append("\n└ From ").append(getStartTime()).append(" to ").append(getEndTime());

        if (getAllSubPlans().size() > 0) {
            str.append("\n└ Subevents/subtasks: \n  ");
            for (Plan p : getAllSubPlans()) {
                str.append(p.toStringSubplan().replace("\n", "\n    "));
            }
        }
        return str.toString();
    }

    /** Whether the plan is a task.
     * @return false
     */
    @Override
    public boolean isTask(){
        return false;
    }
}
