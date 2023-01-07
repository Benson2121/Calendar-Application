package UseCaseTest;

import Entities.Event;
import Entities.Task;
import Entities.TimeParser;
import Gateway.InterfaceSaver;
import Gateway.Saver;
import HelperClasses.InvalidCommandException;
import HelperClasses.NotFoundException;
import UseCase.PlanManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlanManagerTest {
    @Test(timeout = 50)
    public void getAllPlanIDsTest() {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("123");
        assertEquals(0, plans.getAllPlanIDs("123").size());
        plans.createEvent("abc", "456", "123");
        assertEquals(1, plans.getAllPlanIDs("123").size());
        plans.createEvent("def", "123", "123");
        assertEquals(2, plans.getAllPlanIDs("123").size());
    }
    @Test(timeout = 50)
    public void CreateEventTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("lalala");
        String plan1_id = plans.createEvent("1", "2", "lalala");
        assertEquals(plan1_id, plans.getPlan(plan1_id, "lalala").getPlanID());
    }
    @Test(timeout = 50)
    public void CreateTaskTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        String task_id = plans.createTask("bbb", "lula", "aaa");
        Task t = (Task) plans.getPlan(task_id, "aaa");
        assertEquals("bbb", t.getName());
        plans.removePlan(task_id, "aaa");
        assertFalse(plans.getAllPlanIDs("aaa").contains(task_id));
    }
    @Test(timeout = 50)
    public void getAllPlansTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("bbb");
        assertEquals(0, plans.getAllPlans("bbb").size());
        plans.createEvent("abc", "def", "bbb");
        assertEquals(1, plans.getAllPlans("bbb").size());
        plans.createTask("abc", "def", "bbb");
        assertEquals(2, plans.getAllPlans("bbb").size());
    }
    @Test(timeout = 50)
    public void getPlanTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("111");
        String EventID = plans.createEvent("abc", "def", "111");
        assertEquals("abc", plans.getPlan(EventID, "111").getName());
    }
    @Test(timeout = 50)
    public void removePlanTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("111");
        String taskID = plans.createTask("abc", "def", "111");
        assertEquals(1, plans.getAllPlans("111").size());
        plans.removePlan(taskID, "111");
        assertEquals(0, plans.getAllPlans("111").size());
    }
    @Test(timeout = 50)
    public void changePlanNameTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("lulu");
        String plan_id = plans.createEvent("ccc", "123", "lulu");
        Event e = (Event) plans.getPlan(plan_id, "lulu");
        assertEquals("ccc", e.getName());
        plans.changePlanName("new name", plan_id, "lulu");
        assertEquals("The new name should be \"new name\"", "new name", e.getName());
    }
    @Test(timeout = 50)
    public void changePlaneDescriptionTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("lulu");
        String plan_id = plans.createEvent("ccc", "123", "lulu");
        Event e = (Event) plans.getPlan(plan_id, "lulu");
        plans.changePlanDescription("new des", plan_id, "lulu");
        assertEquals("The new name should be \"new des\"", "new des", e.getDescription());
    }
    @Test(timeout = 200)
    public void setTimeTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        TimeParser timeParser = new TimeParser();
        plans.handleCreateCalendar("aaa");
        String plan_id = plans.createEvent("bbb", "lala", "aaa");
        Event e = (Event) plans.getPlan(plan_id, "aaa");
        plans.setTime(plan_id, "08/10", "08/11", "aaa");
        assertEquals("event e starts at", timeParser.parseTime("08/10"), e.getStartTime());
        assertEquals("event e ends at", timeParser.parseTime("08/11"), e.getEndTime());
    }
    @Test(timeout =  50)
    public void setDeadlineTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        TimeParser timeParser = new TimeParser();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        String task_id = plans.createTask("bbb", "lala", "aaa");
        Task t = (Task) plans.getPlan(task_id, "aaa");
        plans.setDeadline(task_id, "08/12", "aaa");
        assertEquals("Task t ends at", timeParser.parseTime("08/12"), t.getEndTime());
    }
    @Test(timeout = 50)
    public void nestTest() throws InvalidCommandException, NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        String parent_id = plans.createEvent("abc", "def", "aaa");
        String child_id = plans.createTask("lal", "lulu", "aaa");
        plans.nest(parent_id, child_id, "aaa");
        Event parent = (Event) plans.getPlan(parent_id, "aaa");
        Task child = (Task) plans.getPlan(child_id, "aaa");
        assertTrue("Child is nested in Parent", child.isChildOf(parent));
    }
    @Test(timeout = 200)
    public void unnestTest() throws InvalidCommandException, NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        String parent_id = plans.createEvent("abc", "def", "aaa");
        String child_id = plans.createTask("lal", "lulu", "aaa");
        plans.nest(parent_id, child_id, "aaa");
        Event parent = (Event) plans.getPlan(parent_id, "aaa");
        Task child = (Task) plans.getPlan(child_id, "aaa");
        assertTrue("Child is nested in Parent", child.isChildOf(parent));
        plans.unnest(parent_id, child_id, "aaa");
        assertFalse("Child is not nested in Parent", child.isChildOf(parent));
    }
    @Test(timeout = 50)
    public void clearTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        plans.createEvent("abc", "def", "aaa");
        plans.createEvent("abd", "def", "aaa");
        plans.createEvent("abc", "def", "aaa");
        assertEquals("Calendar aaa has 3 plans", 3, plans.getAllPlans("aaa").size());
        String task_id = plans.createTask("abc", "def", "aaa");
        Task t = (Task) plans.getPlan(task_id, "aaa");
        assertEquals("Calendar aaa has 4 plans", 4, plans.getAllPlans("aaa").size());
        plans.clearEvents("aaa");
        assertEquals("Calendar aaa has 1 task", 1, plans.getAllPlans("aaa").size());
        assertEquals("The left plan is task", task_id, plans.getAllPlans("aaa").get(0).getPlanID());
        plans.createEvent("abc", "def", "aaa");
        plans.createTask("abc", "def", "aaa");
        plans.clearTasks("aaa");
        assertEquals("Calendar aaa has 1 task left", 1, plans.getAllPlans("aaa").size());
        assertFalse("calendar aaa does not contain any task", plans.getAllPlans("aaa").contains(t));
    }
    @Test(timeout = 50)
    public void clearAllPlans() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        plans.createTask("abc", "adef", "aaa");
        plans.createEvent("def", "asd", "aaa");
        assertEquals(2, plans.getAllPlans("aaa").size());
        plans.createEvent("ads", "sad", "aaa");
        plans.createTask("asd", "qwe", "aaa");
        assertEquals(4, plans.getAllPlans("aaa").size());
        plans.clearAllPlans("aaa");
        assertEquals(0, plans.getAllPlans("aaa").size());
    }
    @Test(timeout = 50)
    public void setCurrPlanIDTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        String taskID = plans.createTask("asd", "sad", "aaa");
        plans.setCurrPlanID(taskID, "aaa");
        assertEquals(taskID, plans.getCurrPlanID());
        String eventID = plans.createEvent("asd", "asd", "aaa");
        plans.setCurrPlanID(eventID, "aaa");
        assertEquals(eventID, plans.getCurrPlanID());
    }
    @Test(timeout = 50)
    public void isEventTest() throws NotFoundException {
        PlanManager plans = new PlanManager();
        InterfaceSaver save = new Saver();
        plans.setSaver(save);
        plans.handleCreateCalendar("aaa");
        String taskID = plans.createTask("sad", "asd", "aaa");
        assertFalse(plans.isEvent(taskID, "aaa"));
        String eventID = plans.createEvent("sad", "asd", "aaa");
        assertTrue(plans.isEvent(eventID, "aaa"));
    }
}
