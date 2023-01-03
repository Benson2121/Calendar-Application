package Entities;
import java.io.*;

/** An entity class corresponding to a label for a plan.
 * Examples of thematic labels include "Project1", "August", "Important"
 * Examples of progress labels include "Not Started", "Complete", "In Progress"
 * Events should only contain thematic labels, while Tasks can contain both.
 */
public class Label implements Serializable {
    private String name;
    private final boolean isThematic;

    public Label(String name, boolean isThematic){
        this.name = name;
        this.isThematic = isThematic;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isThematic() {
        return isThematic;
    }

    @Override
    public String toString() {
        return name + (isThematic ? " (Thematic)" : " (Progress)");
    }
}
