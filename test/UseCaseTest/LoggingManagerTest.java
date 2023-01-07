package UseCaseTest;

import Entities.AdminUser;
import Entities.RegularUser;
import Entities.User;
import UseCase.LoggingManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoggingManagerTest {
    @Test(timeout = 50)
    public void logInTest(){
        User u = new RegularUser("abc", "456");
        LoggingManager logs = new LoggingManager();
        logs.logIn(u);
        assertEquals(u.getUsername(), logs.getCurrUsername());
        assertEquals(u.getPassword(), logs.getCurrPassword());
    }
    @Test(timeout = 50)
    public void lofOutTest(){
        User u = new RegularUser("abc", "456");
        LoggingManager logs = new LoggingManager();
        logs.logIn(u);
        assertEquals(u.getUsername(), logs.getCurrUsername());
        assertEquals(u.getPassword(), logs.getCurrPassword());
        logs.logOut();
        assertTrue(logs.loggedOut());
    }
    @Test(timeout = 50)
    public void loggedOutTest(){
        LoggingManager logs = new LoggingManager();
        assertTrue(logs.loggedOut());
        User u = new RegularUser("abc", "def");
        logs.logIn(u);
        assertTrue(logs.userLoggedIn());
        logs.logOut();
        assertTrue(logs.loggedOut());
    }
    @Test(timeout = 50)
    public void userLoggedInTest(){
        LoggingManager logs = new LoggingManager();
        User u = new RegularUser("abc", "def");
        logs.logIn(u);
        assertTrue(logs.userLoggedIn());
        assertFalse(logs.adminUserLoggedIn());
        logs.logOut();
        assertTrue(logs.loggedOut());
    }
    @Test(timeout = 50)
    public void adminUserLoggedIn(){
        LoggingManager logs = new LoggingManager();
        User u = new AdminUser("abc", "def");
        logs.logIn(u);
        assertTrue(logs.adminUserLoggedIn());
        assertFalse(logs.userLoggedIn());
        logs.logOut();
        assertTrue(logs.loggedOut());
    }
}
