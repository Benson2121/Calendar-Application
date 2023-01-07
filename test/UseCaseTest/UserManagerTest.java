package UseCaseTest;

import Entities.Calendar;
import Entities.RegularUser;
import Entities.User;
import Gateway.Saver;
import HelperClasses.DuplicateUsernameException;
import HelperClasses.InvalidCommandException;
import HelperClasses.NoPermissionException;
import HelperClasses.NotFoundException;
import UseCase.UserManager;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class UserManagerTest {
    private UserManager Helper() throws DuplicateUsernameException, NotFoundException, InvalidCommandException {
        UserManager users = new UserManager();
        Saver save = new Saver();
        users.setSaver(save);
        users.createUser("abc", "def");
        users.createUser("abc2", "def");
        users.addCalendar("aaa", "abc");
        users.addCalendar("efg", "abc2");
        users.addCalendar("def", "abc2");
        users.shareCalendar("def", "abc", "abc2", "edit");
        users.shareCalendar("efg", "abc", "abc2", "none");
        return users;
    }

    @Test(timeout = 50)
    public void CreateAndGetUserTest() throws DuplicateUsernameException, NotFoundException {
        UserManager users = new UserManager();
        users.createUser("person", "12345");
        assertEquals("12345", users.getUser("person").getPassword());
    }

    @Test(timeout = 50)
    public void SetPasswordTest() throws DuplicateUsernameException, NotFoundException, IOException {
        UserManager users = new UserManager();
        users.createUser("person", "12345");
        assertNotNull(users);
        users.setPassword("person", "123456789");
        assertEquals("123456789", users.getUser("person").getPassword());
    }
    @Test(timeout = 50)
    public void SetUsernameTest() throws NotFoundException, DuplicateUsernameException, IOException {
        UserManager users = new UserManager();
        users.createUser("person", "12345");
        users.setUsername("person", "person2");
        assertEquals("person2", users.getUser("person2").getUsername());
    }

    @Test(timeout = 50)
    public void AddCalendarTest() throws DuplicateUsernameException, NotFoundException {
        UserManager users = new UserManager();
        users.createUser("person", "12345");
        Calendar c = new Calendar("Work", "Go to Work");
        users.addCalendar(c.getCalendarID(), "person");
        assertNotNull(users.getOwnedCalendars("person"));
    }
    @Test(timeout = 50)
    public void getAllRegularUsernamesTest() throws DuplicateUsernameException {
        UserManager users = new UserManager();
        users.createUser("a", "b");
        users.createUser("b", "c");
        users.createAdminUser("c", "d");
        assertTrue(users.getAllRegularUsernames().contains("a"));
        assertTrue(users.getAllRegularUsernames().contains("b"));
        assertFalse(users.getAllRegularUsernames().contains("c"));
    }
    @Test(timeout = 50)
    public  void getAllUsernamesTest() throws DuplicateUsernameException {
        UserManager users = new UserManager();
        users.createUser("a", "b");
        users.createUser("b", "c");
        users.createAdminUser("c", "d");
        assertTrue(users.getAllUsernames().contains("a"));
        assertTrue(users.getAllUsernames().contains("b"));
        assertTrue(users.getAllUsernames().contains("c"));
    }
    @Test(timeout = 50)
    public void createAdminUserTest() throws DuplicateUsernameException, NotFoundException {
        UserManager users = new UserManager();
        users.createAdminUser("abc", "def");
        assertEquals("def", users.getUser("abc").getPassword());
    }
    @Test(timeout = 50)
    public void deleteUserTest() throws DuplicateUsernameException, NotFoundException, InvalidCommandException, NoPermissionException, IOException {
        UserManager users = new UserManager();
        users.createUser("abc", "def");
        assertEquals("def", users.getUser("abc").getPassword());
        User u = users.getUser("abc");
        users.deleteUser("abc", true);
        assertFalse(users.getAllUsernames().contains("abc"));
    }
    @Test(timeout = 50)
    public void banUserTest() throws DuplicateUsernameException, NotFoundException, InvalidCommandException, IOException {
        UserManager users = new UserManager();
        users.createUser("abc", "def");
        RegularUser u = (RegularUser) users.getUser("abc");
        assertFalse(u.getIsBanned());
        users.banUser("abc");
        assertTrue(u.getIsBanned());
    }
    @Test(timeout = 50)
    public void unbanUserTest() throws DuplicateUsernameException, InvalidCommandException, NotFoundException, IOException {
        UserManager users = new UserManager();
        users.createUser("abc", "def");
        RegularUser u = (RegularUser) users.getUser("abc");
        assertFalse(u.getIsBanned());
        users.banUser("abc");
        assertTrue(u.getIsBanned());
        users.unbanUser("abc");
        assertFalse(u.getIsBanned());
    }
    @Test(timeout = 100)
    public void addCalendarTest() throws DuplicateUsernameException, NotFoundException {
        UserManager users = new UserManager();
        users.createUser("abc", "def");
        RegularUser u = (RegularUser) users.getUser("abc");
        assertEquals(0, u.getOwnedCalendarsID().size());
        users.addCalendar("def", "abc");
        assertEquals(1, u.getOwnedCalendarsID().size());
    }
    @Test(timeout = 100)
    public void deleteCalendarTest() throws DuplicateUsernameException, NotFoundException, NoPermissionException, InvalidCommandException {
        UserManager users = new UserManager();
        users.createUser("abc", "def");
        RegularUser u = (RegularUser) users.getUser("abc");
        assertEquals(0, u.getOwnedCalendarsID().size());
        users.addCalendar("def", "abc");
        assertEquals(1, u.getOwnedCalendarsID().size());
        users.createUser("abc2", "asd");
        users.shareCalendar("def","abc2", "abc", "edit");
        assertTrue(users.getUser("abc2").getEditableCalendarsID().contains("def"));
        users.deleteCalendar("def", "abc");
        assertFalse(u.getOwnedCalendarsID().contains("def"));
        assertFalse(u.getEditableCalendarsID().contains("def"));
    }
    @Test(timeout = 50)
    public void clearCalendarsTest() throws NotFoundException, DuplicateUsernameException, NoPermissionException, InvalidCommandException {
        UserManager users = Helper();
        assertTrue(users.getOwnedCalendars("abc").contains("aaa"));
        assertTrue(users.getEditableCalendars("abc").contains("def"));
        assertFalse(users.getViewableCalendars("abc").contains("efg"));
        users.clearCalendars("abc");
        assertEquals(0, users.getOwnedCalendars("abc").size());
        assertEquals(0, users.getViewableCalendars("abc").size());
        assertEquals(0, users.getEditableCalendars("abc").size());
    }
    @Test(timeout = 50)
    public void shareCalendarTest() throws DuplicateUsernameException, NotFoundException, InvalidCommandException {
        UserManager users = Helper();
        assertTrue(users.getOwnedCalendars("abc").contains("aaa"));
        assertTrue(users.getEditableCalendars("abc").contains("def"));
        assertFalse(users.getViewableCalendars("abc").contains("efg"));
        users.shareCalendar("efg", "abc", "abc2", "edit");
        assertEquals(0, users.getViewableCalendars("abc").size());
        assertEquals(2, users.getEditableCalendars("abc").size());
        assertTrue(users.getEditableCalendars("abc").contains("efg"));
    }
    @Test(timeout = 50)
    public void deleteOrRemoveAccess() throws DuplicateUsernameException, NotFoundException, InvalidCommandException {
        UserManager users = Helper();
        users.deleteOrRemoveAccess("aaa", "abc");
        users.deleteOrRemoveAccess("def", "abc");
        users.deleteOrRemoveAccess("efg", "abc");
        assertEquals(0, users.getViewableCalendars("abc").size());
        assertEquals(0, users.getOwnedCalendars("abc").size());
        assertEquals(0, users.getEditableCalendars("abc").size());
    }
    @Test(timeout = 50)
    public void getOwnedCalendars() throws DuplicateUsernameException, NotFoundException {
        UserManager users = new UserManager();
        Saver save = new Saver();
        users.setSaver(save);
        users.createUser("abc", "def");
        users.addCalendar("aaa", "abc");
        users.addCalendar("bbb", "abc");
        assertTrue(users.getOwnedCalendars("abc").contains("aaa"));
        assertTrue(users.getOwnedCalendars("abc").contains("bbb"));
    }
    @Test(timeout = 50)
    public void getEditableCalendarsTest() throws DuplicateUsernameException, NotFoundException, NoPermissionException, InvalidCommandException {
        UserManager users = Helper();
        assertFalse(users.getEditableCalendars("abc").contains("aaa"));
        assertTrue(users.getEditableCalendars("abc").contains("def"));
        assertFalse(users.getEditableCalendars("abc").contains("efg"));
    }
    @Test(timeout = 50)
    public void getViewableCalendarsTest() throws DuplicateUsernameException, NotFoundException, NoPermissionException, InvalidCommandException {
        UserManager users = Helper();
        users.shareCalendar("def", "abc", "abc2", "none");
        users.shareCalendar("efg", "abc", "abc2", "none");
        assertFalse(users.getViewableCalendars("abc").contains("def"));
        assertFalse(users.getViewableCalendars("abc").contains("efg"));
        assertEquals(0, users.getViewableCalendars("abc").size());
    }
    @Test(timeout = 50)
    public void validateCalendarForEditAccessTest() throws DuplicateUsernameException, NotFoundException, NoPermissionException, InvalidCommandException {
        UserManager users = Helper();
        assertTrue(users.validateCalendarForEditAccess("abc", "def"));
    }
}
