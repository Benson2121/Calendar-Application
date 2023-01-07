package UseCase;
import java.io.*;
import Entities.*;
import HelperClasses.*;
import UseCase.DisplayPlans.*;
import java.util.*;

/** Use Case class PlanManager, which stores all instances of Plans/SubPlans and methods to create, nest, un-nest,
 * clear, and change Plans.
 * We understand this class is a bloater. Unfortunately, due to poor communication among all of us, we didn't discover
 * this until it was too late to change. In phase 2, our idea is probably to use nested classes or split some related
 * methods like view___() into a separate helper class.
 */
public class PlanManager extends Saveable implements Serializable {
    private Map<String, List<Plan>> allPlans; // Map of CalendarID to its Plans
    private transient final TimeParser timeParser = new TimeParser();
    private transient String currPlanID = null;
    private transient PlanDisplayer planDisplayer;

    /** getAllPlanIDs() Give all the plan ID of the given calendarID
     * @param calendarID a String of the Calendar's calendarID
     * @return a List of String with planID
     */
    public List<String> getAllPlanIDs(String calendarID) {
        List<Plan> plans = allPlans.get(calendarID);
        List<String> ids = new ArrayList<>();
        if (plans != null) {
            for (Plan p : plans) {
                ids.add(p.getPlanID());
            }
        }
        return ids;
    }

    /** handleCreateCalendar()
     * Create a calendarID as key to store in the allPlans
     * @param calendarID a String of the Calendar's calendarID
     */
    public void handleCreateCalendar(String calendarID) {
        allPlans.put(calendarID, new ArrayList<>());
    }

    /** PlanManager Constructor of the PlanManager
     * Initiate a map allPlans
     */
    public PlanManager(){
        allPlans = new HashMap<>();
    }

    /** createTask() Create a Task in the Calendar with calendarID
     * @param name a String of the name of the Task
     * @param description a String of the description of the Task
     * @param calendarID a String of the Calendar's calendarID
     * @return a String of the created Task's planID
     */
    public String createTask(String name, String description, String calendarID){
        Task t = new Task(name, description);
        List<String> ids = getAllPlanIDs(calendarID);
        t.setPlanID(t.assignID(name, ids, calendarID));
        allPlans.get(calendarID).add(t);
        makeChange();
        return t.getPlanID();
    }

    /** createEvent() Create an Event in the Calendar with calendarID
     * @param name a String of the name of the Event
     * @param description a String of the description of the Event
     * @param calendarID a String of the Calendar's calendarID
     * @return a String of the created Event's planID
     */
    public String createEvent(String name, String description, String calendarID){
        Event e = new Event(name, description);
        List<String> ids = getAllPlanIDs(calendarID);
        e.setPlanID(e.assignID(name, ids, calendarID));
        allPlans.get(calendarID).add(e);
        makeChange();
        return e.getPlanID();
    }

    /** getAllPlans() Give all the Plans in the Calendar with calendarID
     * @param calendarID a String of Calendar's calendarID
     * @return a List of Plans stored in the Calendar with calendarID
     * @throws NotFoundException throw NotFoundException when calendarID does not exist
     */
    public List<Plan> getAllPlans(String calendarID) throws NotFoundException {
        if (allPlans.containsKey(calendarID))
            return allPlans.get(calendarID);
        else
            throw new NotFoundException(calendarID);
    }

    /** getPlan() Give a Plan with planID in the Calendar
     * @param planID a String of the Plan's planID
     * @param calendarID a String of the Calendar's calendarID
     * @return a Plan
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public Plan getPlan(String planID, String calendarID) throws NotFoundException {
        if (allPlans.get(calendarID) == null)
            throw new NotFoundException(calendarID);
        for (Plan item : allPlans.get(calendarID)){
            if (Objects.equals(item.getPlanID(), planID))
                return item;
        }
        throw new NotFoundException(planID);
    }

    /** getAllPlanWithPlanIDs() Get all plans with given PlanIDs
     * @param planIDs a List of PlanIDs
     * @param calendarID a String of the Calendar's calendarID
     * @return a List of Plans
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public List<Plan> getAllPlanWithPlanIDs(Set<String> planIDs, String calendarID) throws NotFoundException {
        List<Plan> plans = new ArrayList<>();
        for (String planID : planIDs){
            plans.add(getPlan(planID, calendarID));
        }
        return plans;
    }

    /** removePlan() Remove a Plan from a Calendar
     * @param PlanID a String of the Plan's planID
     * @param calendarID a String of the Calendar's calendarID
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public void removePlan(String PlanID, String calendarID) throws NotFoundException {
        allPlans.get(calendarID).remove(getPlan(PlanID, calendarID));
        makeChange();
    }

    /** changePlanName() Change the name of the Plan in the Calendar
     * @param name a String of the new name of the Plan
     * @param planID a String of the Plan's planID
     * @param calendarID a String of the Calendar's calendarID
     * @return a String of the Plan's ca
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public String changePlanName(String name, String planID, String calendarID) throws NotFoundException {
        Plan p = getPlan(planID, calendarID);
        p.setName(name);
        List<String> ids = getAllPlanIDs(calendarID);
        p.setPlanID(p.assignID(name, ids, calendarID));
        makeChange();
        return p.getPlanID();
    }

    /** changePlanDescription() Change the description of the Plan in the Calendar
     * @param description a String of the new description of the Plan
     * @param planID a String of the Plan's planID
     * @param calendarID a String of the Calendar's calendarID
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public void changePlanDescription(String description, String planID, String calendarID) throws NotFoundException {
        Plan p = getPlan(planID, calendarID);
        p.setDescription(description);
        makeChange();
    }

    /** nest() Nests a SubPlan within another Plan.
     * @param parentPlanID the planID of the parent Plan.
     * @param subPlanID the planID of the subPlan.
     * @param calendarID the calendarID of the parent Plan and subPlan.
     */
    public void nest(String parentPlanID, String subPlanID, String calendarID) throws NotFoundException, InvalidCommandException {
        Plan Parent = getPlan(parentPlanID, calendarID);
        Plan SubPlan = getPlan(subPlanID, calendarID);
        Parent.addSubPlan(SubPlan);
        makeChange();
    }

    /** unnest() Breaks the nest relationship between the Parent Plan and SubPlan.
     *
     * @param parentPlanID the PlanID of the parent Plan.
     * @param subPlanID the PlanID of the subPlan.
     * @param calendarID the calendarID of the parent Plan and subPlan.
     */
    public void unnest(String parentPlanID, String subPlanID, String calendarID) throws NotFoundException, InvalidCommandException {
        Plan Parent = getPlan(parentPlanID, calendarID);
        Plan SubPlan = getPlan(subPlanID, calendarID);
        Parent.removeSubPlan(SubPlan);
        makeChange();
    }

    /** clearTasks() Clear all the Tasks in the Calendar
     * @param calenderID a String of the Calendar's calendarID
     */
    public void clearTasks(String calenderID){
        allPlans.get(calenderID).removeIf(Plan::isTask);
        makeChange();
    }

    /** clearEvents() Clear all the Events in the Calendar
     * @param calenderID a String of the Calendar's calendarID
     */
    public void clearEvents(String calenderID){
        allPlans.get(calenderID).removeIf(item -> !item.isTask());
        makeChange();
    }

    /** clearAllPlans() Clear all the Plans in the Calendar
     * @param calenderID a String of the Calendar's calendarID
     * @throws NotFoundException throw NotFoundException when calendarID does not exist
     */
    public void clearAllPlans(String calenderID) throws NotFoundException{
        if (!allPlans.containsKey(calenderID))
            throw new NotFoundException(calenderID);
        ArrayList<Plan> remove_plans = new ArrayList<>(allPlans.get(calenderID));
        for (Plan item: remove_plans) {
            allPlans.get(calenderID).remove(item);
        }
        makeChange();
    }

    /** handleClearCalendar() Removes the calendar ID from the mapping if a calendar is deleted
     * @param calendarID a String of the Calendar's calendarID
     */
    public void handleClearCalendar(String calendarID) {
        allPlans.remove(calendarID);
        makeChange();
    }

    /** setCurrPlanID() Set the CurrPlanID to be planID
     * @param planID a String of Plan's planID
     * @param calendarID a String of the Calendar's calendarID
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public void setCurrPlanID(String planID, String calendarID) throws NotFoundException {
        getPlan(planID, calendarID);
        this.currPlanID = planID;
    }

    /** getCurrPlanID() Give the currPlanID
     * @return a String of the current planID
     */
    public String getCurrPlanID() {
        return this.currPlanID;
    }

    /** isEvent() Check whether the Plan is an Event or not
     * @param planID a String of the Plan's planID
     * @param calendarID a String of the Calendar's calendarID
     * @return a Boolean shows whether the Plan is an Event or not
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public boolean isEvent(String planID, String calendarID) throws NotFoundException {
        return (getPlan(planID, calendarID) instanceof Event);
    }

    /** setTime() Set the start time and end time for an Event in a Calendar
     * @param eventID a String of the Event's planID
     * @param startTime a String of the start time
     * @param endTime a String of the end time
     * @param calendarID a String of the Calendar's calendarID
     * @throws NotFoundException throw NotFoundException when calendarID or planID does not exist
     */
    public void setTime(String eventID, String startTime, String endTime, String calendarID) throws NotFoundException {
        boolean found = false;
        for (Plan p : allPlans.get(calendarID)) {
            if (p.getPlanID().equals(eventID)) {
                ((Event) p).setStartTime(timeParser.parseTime(startTime));
                p.setEndTime(timeParser.parseTime(endTime));
                makeChange();
                found = true;
            }
        }
        if (!found) {
            throw new NotFoundException(eventID);
        }
    }

    /** setDeadline() Set the deadline of a Task in a Calendar
     * @param taskID a String of the Task's planID
     * @param deadline a String of the deadline time
     * @param calendarID a String of the Calendar's calendarID
     * @throws NotFoundException throw NotFoundException when calendarID or taskID does not exist
     */
    public void setDeadline(String taskID, String deadline, String calendarID) throws NotFoundException {
        boolean found = false;
        for (Plan p : allPlans.get(calendarID)) {
            if (p.getPlanID().equals(taskID)) {
                p.setEndTime(timeParser.parseTime(deadline));
                makeChange();
                found = true;
            }
        }
        if (!found) {
            throw new NotFoundException(taskID);
        }
    }

    private void setDisplayer(PlanDisplayer planDisplayer){
        this.planDisplayer = planDisplayer;
    }

    /** View a Calendar's Schedule.
     * @param calendarID the CalendarID of Calendar to view
     * @return string representation of Calendar's schedule
     */
    public String viewSchedule(String calendarID) throws NotFoundException {
        setDisplayer(new AllPlansPlanDisplayer());
        return planDisplayer.display(getAllPlans(calendarID));
    }

    /** View a Calendar's Schedule based on a label.
     * @param calendarID the CalendarID of Calendar to view
     * @param planIDs Calendar's Plans' PlanIDs
     * @return string representation of Calendar's schedule by label
     */
    public String viewScheduleByLabel(Set<String> planIDs, String calendarID) throws NotFoundException {
        setDisplayer(new AllPlansPlanDisplayer());
        return planDisplayer.display(getAllPlanWithPlanIDs(planIDs, calendarID));
    }

    /** View upcoming plans in a Calendar.
     * @param calendarID the CalendarID of Calendar to view
     * @return string representation of Calendar's upcoming events
     */
    public String viewAllUpcoming(String calendarID) throws NotFoundException {
        setDisplayer(new AllUpcomingPlansPlanDisplayer());
        return planDisplayer.display(getAllPlans(calendarID));
    }

    /** View all events in a Calendar.
     * @param calendarID the CalendarID of Calendar to view
     * @return string representation of all the Calendar's events
     */
    public String viewAllEvents(String calendarID) throws NotFoundException {
        setDisplayer(new AllEventsPlanDisplayer());
        return planDisplayer.display(getAllPlans(calendarID));
    }

    /** View all tasks in a Calendar.
     * @param calendarID the CalendarID of Calendar to view
     * @return string representation of all the Calendar's tasks
     */
    public String viewAllTasks(String calendarID) throws NotFoundException {
        setDisplayer(new AllTasksPlanDisplayer());
        return planDisplayer.display(getAllPlans(calendarID));
    }

    /** Save the current state.
     * @throws IOException error regarding files
     */
    public void save() throws IOException {
        getSaver().save(this, "plans");
    }

    /** Load the current state.
     * @throws IOException error regarding files
     * @throws ClassNotFoundException cannot find class in classpath
     */
    public void load() throws IOException, ClassNotFoundException {
        allPlans = getSaver().load(this, "plans").allPlans;
    }
}
