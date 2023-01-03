package UseCaseTest;

import Entities.Calendar;
import Entities.RegularUser;
import Entities.User;
import HelperClasses.NotFoundException;
import UseCase.CalendarManager;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CalendarManagerTest {

    @Test(timeout = 50)
    public void CalendarManagerMethodTest() {
        CalendarManager c = new CalendarManager();
        String s = c.createCalendar("abc", "def");
        Calendar cal = c.getCalendar("abc");
        assertEquals("abc", cal.getName());
        String calendarID = "abc";
        User u = new RegularUser("123", "456");
        c.addOwner(calendarID, u);
        assertEquals(u, c.getCalendar(calendarID).getOwner());
        c.deleteCalendar(calendarID);
    }
}