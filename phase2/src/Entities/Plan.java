package Entities;
import HelperClasses.*;
import java.util.*;
import java.time.*;

/** An entity class corresponding to an event/task, ie. a plan.
 */
public abstract class Plan extends NameDuplicatable implements Comparable<Plan> {
    private String planID;
    private String name;
    private String description;
    private List<Plan> subPlans;
    private LocalDateTime endTime;

    /** Constructor for Plan.
     * @param name the Plan's name
     * @param description the Plan's description
     */
    public Plan(String name, String description) {
        this.name = name;
        this.description = description;
        this.subPlans = new ArrayList<>();
    }

    /** Get name of plan
     * @return name of plan
     */
    public String getName() {
        return this.name;
    }

    /** Get description of plan
     * @return description of plan
     */
    public String getDescription() {
        return this.description;
    }

    /** Set new name of plan
     * @param newName new name of plan
     */
    public void setName(String newName) {
        name = newName;
    }

    /** Set new description of plan
     * @param newDescription new description of plan
     */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /** Get planID of plan
     * @return planID of plan
     */
    public String getPlanID() {
        return planID;
    }

    /** Set new planID of plan
     * @param planID new planID of plan
     */
    public void setPlanID(String planID) {
        this.planID = planID;
    }

    /** Get all SubPlans of plan
     * @return List of SubPlans
     */
    public List<Plan> getAllSubPlans() {
        return this.subPlans;
    }

    /** Get specific SubPlan of plan
     * @param subPlanID the PlanID of the SubPlan
     * @return SubPlan
     * @throws NotFoundException - SubPlan is not found
     */
    public Plan getSubPlan(String subPlanID) throws NotFoundException {
        for (Plan item : subPlans) {
            if (Objects.equals(item.getPlanID(), subPlanID))
                return item;
        }
        throw new NotFoundException(subPlanID);
    }

    /** Check if plan is already child or parent plan
     * @param plan potential child/parent plan
     * @return true or false if plan is a child or parent
     */
    public boolean isChildOf(Plan plan) {
        if (plan.getAllSubPlans().contains(this))
            return true;
        else if (plan.getAllSubPlans().isEmpty() || this == plan)
            return false;
        else {
            for (Plan subPlan : plan.getAllSubPlans()) {
                if (isChildOf(subPlan))
                    return true;
            }
        }
        return false;
    }

    /** Add a subPlan
     * @param child the nested, child plan
     * @throws InvalidCommandException nesting cannot occur
     */
    public void addSubPlan(Plan child) throws InvalidCommandException {
        if (!child.isChildOf(this))
            this.subPlans.add(child);
        else
            throw new InvalidCommandException("nest");
    }

    /** Remove a subPlan
     * @param child the nested, child plan
     * @throws InvalidCommandException nesting cannot be undone
     */
    public void removeSubPlan(Plan child) throws InvalidCommandException {
        if (child.isChildOf(this))
            this.subPlans.remove(child);
        else
            throw new InvalidCommandException("unnest");
    }

    /** If Plan is a Task.
     */
    public abstract boolean isTask();

    /** Get String version of SubPlan
     * @return SubPlan string representation
     */
    public String toStringSubplan() {
        return "└ " + this;
    }

    /** Get end time of Plan
     * @return Plan's end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /** Set end time of Plan
     * @param endTime Plan's new end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /** Compare Plans.
     * @param other Plan to compare to
     */
    @Override
    public int compareTo(Plan other) {
        return this.getEndTime().compareTo(other.getEndTime());
    }


}
