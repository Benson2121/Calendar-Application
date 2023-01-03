package Entities;
import java.util.*;
import java.io.*;

/** An entity class corresponding to a task, i.e. a plan.
 */
public class Task extends Plan implements Serializable {

    /** Constructor for Task.
     * @param name the Task's name
     * @param description the Task's description
     */
    public Task(String name, String description) {
        super(name, description);
    }

    /** String representation of a Task.
     * @return Task as a String
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("Task | " + getName() + " (" + getPlanID() + ")");
        if (!getDescription().equals(""))
            str.append(" | \"").append(getDescription()).append("\"");

        if (getEndTime() == null)
            str.append("\n└ No Deadline Assigned");
        else
            str.append("\n└ | Due ").append(getEndTime());

        if (getAllSubPlans().size() > 0) {
            str.append("\n└ Subevents/subtasks: \n  ");
            for (Plan p : getAllSubPlans()) {
                str.append(p.toStringSubplan().replace("\n", "\n    "));
            }
        }
        return str.toString();
    }

    /** Whether the plan is a task.
     * @return true
     */
    @Override
    public boolean isTask(){
        return true;
    }
}
