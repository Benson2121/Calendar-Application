package UseCase;
import Entities.*;
import HelperClasses.*;
import java.util.*;
import java.io.*;

/** Use case manager class for labels.
 * @author Richard Yin
 */
public class LabelManager implements Serializable {
    private Map<String, Map<String, Label>> labels = new HashMap<>(); // Map of planIDs to a map of label names to labels

    public Map<String, Map<String, Label>> getLabels() {
        return labels;
    }
    public String addThematicLabel(String labelName, String planID) {
        Label l = new Label(labelName, true);
        labels.get(planID).put(l.getName(), l);
        return "Successfully added thematic label \"" + labelName + "\". \n";
    }
    public String addProgressLabel(String labelName, String planID) {
        Label l = new Label(labelName, false);
        labels.get(planID).put(l.getName(), l);
        return "Successfully added progress label \"" + labelName + "\". \n";
    }
    public String setLabel(String labelName1, String labelName2, String planID) throws NotFoundException {
        Label l = labels.get(planID).get(labelName1);
        if (l == null)
            throw new NotFoundException(labelName1);
        l.setName(labelName2);
        labels.get(planID).put(labelName2, l);
        labels.get(planID).remove(labelName1);
        return "Successfully changed label name from " + labelName1 + " to " + labelName2 + ". \n";
    }
    public boolean isThematic(String labelName, String planID) throws NotFoundException {
        Label l = labels.get(planID).get(labelName);
        if (l == null)
            throw new NotFoundException(labelName);
        return l.isThematic();
    }
    public String deleteLabel(String labelName, String planID) throws NotFoundException {
        Label l = labels.get(planID).remove(labelName);
        if (l == null)
            throw new NotFoundException(labelName);
        return "Successfully deleted label name " + l.getName() + ". \n";
    }
    public String clearLabels(String planID) {
        labels.get(planID).clear();
        return "Successfully cleared labels. \n";
    }
    public String toString(String planID) {
        StringBuilder str = new StringBuilder("Labels of " + planID + ": \n");
        for (Label l : labels.get(planID).values()) {
            str.append("- ").append(l.toString()).append("\n");
        }
        return str.toString();
    }

    // Methods on updating the map when planIDS are created/changed/deleted
    /**
     * Deletes all labels associated with all plans associated with a user.
     * @param planIDs A list of all the user's planIDs.
     */
    public void handleClearUserPlans(List<String> planIDs) {
        for (String planID : planIDs) {
            labels.remove(planID);
        }
    }
    public void handleNewPlan(String planID) {
        if (!containsPlanID(planID))
            labels.put(planID, new HashMap<>());
    }
    public void handleDeletePlan(String planID) throws NotFoundException {
        if (!containsPlanID(planID))
            throw new NotFoundException(planID);
        labels.remove(planID);
    }
    public void handleSetPlanName(String oldID, String newID) {
        labels.put(newID, labels.get(oldID));
        labels.remove(oldID);
    }
    public boolean containsPlanID(String planID) {
        return labels.containsKey(planID);
    }
}
