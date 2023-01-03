package EntityTest;

import Entities.Calendar;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CalendarTest {

    @Test(timeout = 50)
    public void testCalendar(){
        Calendar c = new Calendar("Alpha", "Mars");
        assertEquals("Alpha", c.getName());
        assertEquals("Mars", c.getDescription());
        c.setCalendarID("114514");
        assertEquals("114514", c.getCalendarID());
        c.setDescription("hello");
        assertEquals("hello", c.getDescription());
    }
}
