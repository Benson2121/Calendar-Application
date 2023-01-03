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

    public Plan(String name, String description) {
        this.name = name;
        this.description = description;
        this.subPlans = new ArrayList<>();
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setName(String newName) {
        name = newName;
    }
    public void setDescription(String newDescription) {
        description = newDescription;
    }
    public String getPlanID() {
        return planID;
    }
    public void setPlanID(String planID) {
        this.planID = planID;
    }
    public List<Plan> getAllSubPlans() {
        return this.subPlans;
    }
    public Plan getSubPlan(String subPlanID) throws NotFoundException {
        for (Plan item : subPlans) {
            if (Objects.equals(item.getPlanID(), subPlanID)) {
                return item;
            }
        }
        throw new NotFoundException(subPlanID);
    }
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
    public void addSubPlan(Plan child) throws InvalidCommandException {
        if (!child.isChildOf(this))
            this.subPlans.add(child);
        else {
            throw new InvalidCommandException("nest");
        }
    }
    public void removeSubPlan(Plan child) throws InvalidCommandException {
        if (child.isChildOf(this))
            this.subPlans.remove(child);
        else {
            throw new InvalidCommandException("unnest");
        }
    }
    public abstract boolean isTask();
    public String toStringSubplan() {
        return "└ " + this;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // Unused
    @Override
    public int compareTo(Plan other) {
        return this.getEndTime().compareTo(other.getEndTime());
    }
}
