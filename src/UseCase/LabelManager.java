package UseCase;
import Entities.*;
import HelperClasses.*;
import java.util.*;
import java.io.*;

/** Use case manager class for labels.
 * @author Richard Yin
 */
public class LabelManager extends Saveable implements Serializable {
    private Map<String, Map<String, Label>> thematicLabels = new HashMap<>(); // Map of planIDs to a map of thematic label names to thematic labels
    private Map<String, Label.ProgressLabels> progressLabels = new HashMap<>(); // Map of planIDs to a map of progress labels

    /** Create a Thematic Label to a plan
     * @param labelName a String of the name of the Thematic Label
     * @param planID a String of planID you want to add a Label to
     */
    public void addThematicLabel(String labelName, String planID) {
        Label l = new Label(labelName);
        thematicLabels.get(planID).put(l.getName(), l);
        makeChange();
    }

    /** Add a ProgressLabel to a plan with given planID
     * @param labelName a String of the name of the ProgressLabel
     * @param planID a String of the planID you want to add a Label to
     */
    public void setProgressLabel(Label.ProgressLabels labelName, String planID) {
        progressLabels.put(planID, labelName);
        makeChange();
    }

    /** Change the name of a Thematic Label from labelName1 to LabelName 2
     * @param labelName1 a String of the original name of the Thematic Label
     * @param labelName2 a String of the new name of the Thematic Label
     * @param planID a String of the planID which the Thematic Label belongs to
     * @throws NotFoundException If a planID cannot be found
     */
    public void setThematicLabel(String labelName1, String labelName2, String planID) throws NotFoundException {
        Label l = getThematicLabel(planID, labelName1);
        l.setName(labelName2);
        thematicLabels.get(planID).put(labelName2, l);
        thematicLabels.get(planID).remove(labelName1);
        makeChange();
    }

    /** Delete a Thematic Label with labelName in plan with PlanID
     * @param labelName a String of the name of the Thematic label to delete
     * @param planID a String of the planID the Thematic label belongs to
     * @throws NotFoundException If a planID cannot be found
     */
    public void deleteThematicLabel(String labelName, String planID) throws NotFoundException {
        getThematicLabels(planID).remove(labelName);
        makeChange();
    }

    /** Clear all the Thematic Labels of a plan with planID
     * @param planID a String of the Plan's planID to clear labels for
     */
    public void clearLabels(String planID) {
        thematicLabels.get(planID).clear();
        makeChange();
    }

    /** Get a Thematic Label
     * @param planID a String of the Plan's plan ID the Thematic Label belongs to
     * @param labelName a String of the Thematic Label's name
     * @return a Label with given label name and planID
     * @throws NotFoundException If a planID cannot be found
     */
    private Label getThematicLabel(String planID, String labelName) throws NotFoundException {
        Label lab = getThematicLabels(planID).get(labelName);
        if (lab == null)
            throw new NotFoundException(labelName);
        return lab;
    }

    /** Get a map of Labels with Label Name as the key and Label as the value
     * @param planID a String of the Plan's planID you want get labels for
     * @return a map of Labels with Label Name as the key and Label as the value
     * @throws NotFoundException If a planID cannot be found
     */
    private Map<String, Label> getThematicLabels(String planID) throws NotFoundException {
        Map<String, Label> labs = thematicLabels.get(planID);
        if (labs == null)
            throw new NotFoundException(planID);
        return labs;
    }

    /** Display labels in Plan with given planID
     * @param planID a String of the Plan's planID to display labels
     * @return a String with labels being displayed
     */
    public String toString(String planID) {
        StringBuilder str = new StringBuilder("Labels of " + planID + ": \n");
        for (Label l : thematicLabels.get(planID).values()) {
            str.append("- ").append(l.toString()).append("\n");
        }
        return str.toString();
    }

    // Methods on updating the map when planIDS are created/changed/deleted
    /** Deletes all labels associated with all Plans associated with a user.
     * @param planIDs A list of all the user's planIDs.
     */
    public void handleClearUserPlans(List<String> planIDs) {
        for (String planID : planIDs) {
            thematicLabels.remove(planID);
            makeChange();
        }
    }

    /** handleNewPlan()
     * Create a new plan and store in the thematicLabels and progressLabels
     * @param planID a String of the Plan's plan ID to create
     * @param newEvent a Boolean of whether the Plan is a Task or event
     */
    public void handleNewPlan(String planID, boolean newEvent) {
        thematicLabels.put(planID, new HashMap<>());
        if (!newEvent)
            progressLabels.put(planID, Label.ProgressLabels.NOT_STARTED);
        makeChange();
    }

    /** handleDeletePlan() Delete a Plan
     * @param planID a String of the Plan's planID to delete
     */
    public void handleDeletePlan(String planID) {
        if (containsPlanID(planID)) {
            thematicLabels.remove(planID);
            progressLabels.remove(planID);
            makeChange();
        }
    }

    /** handleSetPlanName() Change a Plan's planID
     * @param oldID a String of the Plan's original planID
     * @param newID a String of the Plan's new planID
     */
    public void handleSetPlanName(String oldID, String newID) {
        thematicLabels.put(newID, thematicLabels.get(oldID));
        thematicLabels.remove(oldID);
        makeChange();
    }

    /** containsPlanID() Check whether a Plan exists or not
     * @param planID a String of a Plan's planID
     * @return a Boolean shows whether a Plan exists or not
     */
    public boolean containsPlanID(String planID) {
        return thematicLabels.containsKey(planID);
    }

    /** getPlanIDsWithLabels() Give all the planIDs with a Set of Label names
     * @param labels a Set of Label's name
     * @return a Set of planID
     */
    public Set<String> getPlanIDsWithLabels(Set<String> labels) {
        Set<String> planIDs = new HashSet<>();
        for (String label : labels) {
            for (String planID : thematicLabels.keySet()) {
                if (thematicLabels.get(planID).containsKey(label))
                    planIDs.add(planID);
            }
        }
        return planIDs;
    }

    /** Save the current state.
     * @throws IOException error regarding files
     */
    public void save() throws IOException {
        getSaver().save(this, "labels");
    }

    /** Load the current state.
     * @throws IOException error regarding files
     * @throws ClassNotFoundException cannot find class in classpath
     */
    public void load() throws IOException, ClassNotFoundException {
        LabelManager l = getSaver().load(this, "labels");
        progressLabels = l.progressLabels;
        thematicLabels = l.thematicLabels;
    }
}
