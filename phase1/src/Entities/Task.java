package Entities;
import java.util.*;
import java.io.*;

/** An entity class corresponding to a task.
 */
public class Task extends Plan implements Serializable {
    private List<String> comments; // Not implemented yet in phase 1

    public Task(String name, String description) {
        super(name, description);
        this.comments = new ArrayList<>();
    }
    public List<String> getComments(){
        return comments;
    }
    public void addComment(String comment){
        comments.add(comment);
    }
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
    @Override
    public boolean isTask(){
        return true;
    }
}
