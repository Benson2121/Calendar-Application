package UseCaseTest;

import Entities.Calendar;
import Gateway.InterfaceSaver;
import Gateway.Saver;
import HelperClasses.NotFoundException;
import UseCase.CalendarManager;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CalendarManagerTest {
    @Test(timeout = 50)
    public void CreateCalendarTest() throws NotFoundException {
        CalendarManager cals = new CalendarManager();
        InterfaceSaver save = new Saver();
        cals.setSaver(save);
        String calendarID = cals.createCalendar("abc", "def", "123");
        Calendar c = cals.getCalendar(calendarID);
        assertEquals("Calendar c's name is", "abc", c.getName());
        assertEquals("Calendar c's description", "def", c.getDescription());
    }
    @Test(timeout = 200)
    public void deleteCalendarTest() throws NotFoundException {
        CalendarManager cals = new CalendarManager();
        InterfaceSaver save = new Saver();
        cals.setSaver(save);
        String calendarID = cals.createCalendar("abc", "def", "123");
        assertTrue(cals.containCalendar(calendarID));
        cals.deleteCalendar(calendarID);
        assertFalse(cals.containCalendar(calendarID));
    }
    @Test(timeout = 50)
    public void changeCalendarNameTest() throws NotFoundException {
        CalendarManager cals = new CalendarManager();
        InterfaceSaver save = new Saver();
        cals.setSaver(save);
        String calendarID = cals.createCalendar("abc", "def", "123");
        Calendar c = cals.getCalendar(calendarID);
        assertEquals("abc", c.getName());
        cals.changeCalendarName("bcd", calendarID);
        assertEquals("bcd", c.getName());
    }
    @Test(timeout = 50)
    public void changeCalendarDescriptionTest() throws NotFoundException {
        CalendarManager cals = new CalendarManager();
        InterfaceSaver save = new Saver();
        cals.setSaver(save);
        String calendarID = cals.createCalendar("abc", "def", "123");
        Calendar c = cals.getCalendar(calendarID);
        assertEquals("def", c.getDescription());
        cals.changeCalendarDescription("aaa", calendarID);
        assertEquals("aaa", c.getDescription());
    }
    @Test(timeout = 50)
    public void setCurrCalendarIDTest(){
        CalendarManager cals = new CalendarManager();
        InterfaceSaver save = new Saver();
        cals.setSaver(save);
        String calendarID = cals.createCalendar("abc", "def", "123");
        cals.setCurrCalendarID(calendarID);
        assertEquals(calendarID, cals.getCurrCalendarID());
    }
    @Test(timeout = 50)
    public void containCalendarTest() throws NotFoundException {
        CalendarManager cals = new CalendarManager();
        InterfaceSaver save = new Saver();
        cals.setSaver(save);
        String calendarID = cals.createCalendar("abc", "def", "123");
        assertTrue(cals.containCalendar(calendarID));
        cals.deleteCalendar(calendarID);
        assertFalse(cals.containCalendar(calendarID));
    }
    @Test(timeout = 50)
    public void deleteCalendarsTest() throws NotFoundException {
        CalendarManager cals = new CalendarManager();
        InterfaceSaver save = new Saver();
        cals.setSaver(save);
        String calendarID1 = cals.createCalendar("abc", "def", "123");
        String calendarID2 = cals.createCalendar("abs", "sad", "sad");
        assertTrue(cals.containCalendar(calendarID1));
        assertTrue(cals.containCalendar(calendarID2));
        List<String> calendarIDs = new ArrayList<>(List.of(calendarID1, calendarID2));
        cals.deleteCalendars(calendarIDs);
        assertFalse(cals.containCalendar(calendarID1));
        assertFalse(cals.containCalendar(calendarID2));
    }
}