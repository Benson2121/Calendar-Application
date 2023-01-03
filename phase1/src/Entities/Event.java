package Entities;
import java.time.*;
import java.util.*;

/** An entity class corresponding to an event/task, ie. a plan.
 */
public class Event extends Plan {
    private LocalDateTime startTime;
    private List<DayOfWeek> Repeat = new ArrayList<>(); // For phase 2, not implemented yet

    public Event(String name, String description) {
        super(name, description);
    }
    public void setStartTime(LocalDateTime startTime) throws DateTimeException {
        this.startTime = startTime;
    }
    public LocalDateTime getStartTime(){
        return startTime;
    }
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
    @Override
    public boolean isTask(){
        return false;
    }

    public List<DayOfWeek> getRepeat(){
        return Repeat;
    }
    public void addRepeat(DayOfWeek day){
        Repeat.add(day);
    }
}
