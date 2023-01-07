package EntityTest;

import Entities.*;
import HelperClasses.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlanTest {
    @Test(timeout = 50)
    public void testPlan() throws InvalidCommandException, NotFoundException {
        Event e = new Event("csc207", "due tonight");
        assertEquals("csc207", e.getName());
        assertEquals("due tonight", e.getDescription());
        TimeParser t = new TimeParser();
        e.setStartTime(t.parseTime("2022/07/22 12:20"));
        assertEquals(t.parseTime("2022/07/22 12:20"), e.getStartTime());
        e.setEndTime(t.parseTime("2022/07/22 12:40"));
        assertEquals(t.parseTime("2022/07/22 12:40"), e.getEndTime());
        e.setPlanID("12345");
        assertEquals("12345", e.getPlanID());
        Task task = new Task("sta", "finish");
        e.addSubPlan(task);
        assertTrue(e.getAllSubPlans().contains(task));
        Task task2 = new Task("123", "908");
        assertFalse(e.getAllSubPlans().contains(task2));
        assertEquals(task, e.getSubPlan(task.getPlanID()));
        assertTrue(task.isChildOf(e));
        assertFalse(task2.isChildOf(e));
        e.removeSubPlan(task);
        assertEquals(0, e.getAllSubPlans().size());
    }
}
