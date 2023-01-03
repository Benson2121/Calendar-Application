package Entities;
import java.io.*;

/** An entity class corresponding to a label for a plan.
 * Examples of thematic labels include "Project1", "August", "Important"
 * Examples of progress labels include "Not Started", "Complete", "In Progress"
 * Events should only contain thematic labels, while Tasks can contain both.
 */
public class Label implements Serializable {
    private String name;

    public enum ProgressLabels {
        IN_PROGRESS, COMPLETE, NOT_STARTED, STALLED
    }

    /** Constructor for Label.
     * @param name the Label's username
     */
    public Label(String name){
        this.name = name;
    }

    /** Get the Label's name.
     * @return name of label
     */
    public String getName() {
        return name;
    }

    /** Set the Label's name.
     * @param name name of label
     */
    public void setName(String name) {
        this.name = name;
    }

    /** String representation of a Label.
     * @return Label's name as a String
     */
    @Override
    public String toString() {
        return name;
    }
}


