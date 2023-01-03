package EntityTest;

import Entities.Calendar;
import Entities.RegularUser;
import Entities.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalendarTest {

    @Test(timeout = 50)
    public  void testCalendar(){
        Calendar c = new Calendar("abcde", "lalala");
        assertEquals("abcde", c.getName());
        assertEquals("lalala", c.getDescription());
        c.setCalendarID("123");
        assertEquals("123", c.getCalendarID());
        User u = new RegularUser("123", "456");
        c.setOwner(u);
        assertEquals(u, c.getOwner());
        c.setDescription("lululu");
        assertEquals("lululu", c.getDescription());
    }
}
