package UseCase;
import java.io.*;
import Entities.*;
import HelperClasses.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

/** Use Case class PlanManager, which stores all instances of Plans/SubPlans and methods to create, nest, un-nest,
 * clear, and change Plans.
 * We understand this class is a bloater. Unfortunately, due to poor communication among all of us, we didn't discover
 * this until it was too late to change. In phase 2, our idea is probably to use nested classes or split some related
 * methods like view___() into a separate helper class.
 */
public class PlanManager implements Serializable {

    private Map<String, List<Plan>> allPlans; // Map of CalendarID to its Plans
    private final TimeParser timeParser = new TimeParser();
    private String currPlanID = null;

    public List<String> getAllPlanIDs(String CalendarID) {
        List<Plan> plans = allPlans.get(CalendarID);
        List<String> ids = new ArrayList<>();
        if (plans != null) {
            for (Plan p : plans) {
                ids.add(p.getPlanID());
            }
        }
        return ids;
    }

    public void handleCreateCalendar(String calendarID) {
        allPlans.put(calendarID, new ArrayList<>());
    }

    public PlanManager(){
        allPlans = new HashMap<>();
    }
    public String[] createTask(String name, String description, String CalendarID){
        Task t = new Task(name, description);
        List<String> ids = getAllPlanIDs(CalendarID);
        t.setPlanID(t.assignID(name, ids, CalendarID));
        allPlans.get(CalendarID).add(t);
        return new String[] {"Successfully created task \"" + name + "\" with ID \"" + t.getPlanID() + "\". \n", t.getPlanID()};
    }
    public String[] createEvent(String name, String description, String CalendarID){
        Event e = new Event(name, description);
        List<String> ids = getAllPlanIDs(CalendarID);
        e.setPlanID(e.assignID(name, ids, CalendarID));
        allPlans.get(CalendarID).add(e);
        return new String[] {"Successfully created event \"" + name + "\" with ID \"" + e.getPlanID() + "\". \n", e.getPlanID()};
    }
    public List<Plan> getAllPlans(String CalendarID){
        return allPlans.get(CalendarID);
    }
    public Plan getPlan(String planID, String CalendarID) throws NotFoundException{
        for (Plan item : allPlans.get(CalendarID)){
            if (Objects.equals(item.getPlanID(), planID)){
                return item;
            }
        }
        throw new NotFoundException(planID);
    }
    public String removePlan(String PlanID, String CalendarID) throws NotFoundException {
        allPlans.get(CalendarID).remove(getPlan(PlanID, CalendarID));
        return "Plan successfully removed. \n";
    }
    public String[] changePlanName(String name, String planID, String CalendarID) throws NotFoundException {
        Plan p = getPlan(planID, CalendarID);
        p.setName(name);
        List<String> ids = getAllPlanIDs(CalendarID);
        p.setPlanID(p.assignID(name, ids, CalendarID));
        return new String[]{"Plan name updated to \"" + name + "\" and ID changed to " + p.getPlanID() + ". \n", p.getPlanID()};
    }
    public String changePlanDescription(String description, String planID, String CalendarID) throws NotFoundException {
        Plan p = getPlan(planID, CalendarID);
        p.setDescription(description);
        return "Plan description updated to \"" + description + "\". \n";
    }
    /** nest() Nests a SubPlan within another Plan.
     *
     * @param parentPlanID the PlanID of the parent Plan.
     * @param subPlanID the PlanID of the subPlan.
     * @param CalendarID the CalendarID of the parent Plan and subPlan.
     */
    public String nest(String parentPlanID, String subPlanID, String CalendarID) throws NotFoundException, InvalidCommandException {
        Plan Parent = getPlan(parentPlanID, CalendarID);
        Plan SubPlan = getPlan(subPlanID, CalendarID);
        Parent.addSubPlan(SubPlan);
        return "Successfully nested \"" + SubPlan.getName() + "\" in \"" + Parent.getName() + "\". \n";
    }
    /** unnest() Breaks the nest relationship between the Parent Plan and SubPlan.
     *
     * @param parentPlanID the PlanID of the parent Plan.
     * @param subPlanID the PlanID of the subPlan.
     * @param CalendarID the CalendarID of the parent Plan and subPlan.
     */
    public String unnest(String parentPlanID, String subPlanID, String CalendarID) throws NotFoundException, InvalidCommandException {
        Plan Parent = getPlan(parentPlanID, CalendarID);
        Plan SubPlan = getPlan(subPlanID, CalendarID);
        Parent.removeSubPlan(SubPlan);
        return "Successfully unnested \"" + SubPlan.getName() + "\" from \"" + Parent.getName() + "\". \n";
    }
    public String clearTasks(String CalenderID){
        allPlans.get(CalenderID).removeIf(Plan::isTask);
        return "Successfully removed all Tasks. \n";
    }
    public String clearEvents(String CalenderID){
        allPlans.get(CalenderID).removeIf(item -> !item.isTask());
        return "Successfully removed all Events. \n";
    }
    public String clearAllPlans(String CalenderID){
        for (Plan item: allPlans.get(CalenderID)){
            allPlans.get(CalenderID).remove(item);
        }
        return "Successfully removed all Plans. \n";
    }
    // Removes the calendar ID from the mapping if a calendar is deleted.
    public void handleClearCalendar(String calendarID) {
        allPlans.remove(calendarID);
    }
    public void setCurrPlanID(String PlanID, String CalendarID) throws NotFoundException {
        getPlan(PlanID, CalendarID);
        this.currPlanID = PlanID;
    }
    public String getCurrPlanID() {
        return this.currPlanID;
    }
    public void addRepeatDay(Event e, DayOfWeek Day){
        if (!e.getRepeat().contains(Day)){
            e.addRepeat(Day);
        }
    }
    public void removeRepeatDay(Event e, DayOfWeek Day){
        e.getRepeat().remove(Day);
    }
    public boolean isEvent(String planID, String CalendarID) throws NotFoundException {
        return (getPlan(planID, CalendarID) instanceof Event);
    }
    public Task getTask(String planID, String CalendarID) throws NotFoundException {
        return (Task) getPlan(planID, CalendarID);
    }
    public boolean isTask(String planID, String CalendarID) throws NotFoundException {
        return (getPlan(planID, CalendarID) instanceof Task);
    }
    public String setTime(String eventID, String startTime, String endTime, String calendarID) throws NotFoundException {
        for (Plan p : allPlans.get(calendarID)) {
            if (p.getPlanID().equals(eventID)) {
                ((Event) p).setStartTime(timeParser.parseTime(startTime));
                p.setEndTime(timeParser.parseTime(endTime));
                return "Successfully changed start/end times of event ID " + eventID + ".\n";
            }
        }
        throw new NotFoundException(eventID);
    }
    public String setDeadline(String taskID, String deadline, String calendarID) throws NotFoundException {
        for (Plan p : allPlans.get(calendarID)) {
            if (p.getPlanID().equals(taskID)) {
                p.setEndTime(timeParser.parseTime(deadline));
                return "Successfully changed deadline of task ID " + taskID + ". \n";
            }
        }
        throw new NotFoundException(taskID);
    }
    /**  viewSchedule() Display all the Plan in the Calendar
     *
     * @param allPlans List of Plan
     * @return a print String with each line being a plan
     */
    public String viewSchedule(List<Plan> allPlans){
        StringBuilder s = new StringBuilder();
        for (Plan p:allPlans){
            s.append(p);
            s.append("\n");
        }
        return s.toString();
    }

    /** viewScheduleByLabel() Display the all plans that have a certain label
     *
     * @param labelNames List of String is the name of the label
     * @param labelMap A map of PlanIDs to a map of LabelNames to Labels
     * @param calendarID the current calendar ID
     * @return a print String categorized by label
     */
    public String viewScheduleByLabel(String calendarID, Map<String, Map<String, Label>> labelMap, List<String> labelNames) {
        StringBuilder s = new StringBuilder();
        for (String label:labelNames) { // Iterate through labels
            for (Plan p:allPlans.get(calendarID)) { // Iterate through all plans in a calendar
                if (labelMap.containsKey(p.getPlanID())) { // Check if labels contains the plan
                    if (labelMap.get(p.getPlanID()).containsKey(label)) { // Check if the plan contains the label
                        s.append(p).append("\n");
                    }
                }
            }
        }
        return s.toString();
    }

    /** ViewUpcoming() Display the plans that has an end time after current Time
     *
     * @param allPlans List of Plan
     * @return a print String with each line be a upcoming plan
     */
    public String viewUpcoming(List<Plan> allPlans) {
        StringBuilder s = new StringBuilder("Upcoming: \n");
        for (Plan p:allPlans) {
            if (p.getEndTime() != null && p.getEndTime().isAfter(LocalDateTime.now()))
                s.append(p).append("\n");
        }
        return s.toString();
    }

    public String viewEvents(String currCalendarID) {
        StringBuilder str = new StringBuilder();
        for (Plan p : allPlans.get(currCalendarID)) {
            if (!p.isTask()) {
                str.append(p).append("\n");
            }
        }
        return str.toString();
    }

    public String viewTasks(String currCalendarID) {
        StringBuilder str = new StringBuilder();
        for (Plan p : allPlans.get(currCalendarID)) {
            if (p.isTask()) {
                str.append(p).append("\n");
            }
        }
        return str.toString();
    }
}
