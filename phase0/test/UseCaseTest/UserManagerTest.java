package UseCaseTest;
import Entities.*;
import HelperClasses.*;
import UseCase.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class UserManagerTest {

    @Test(timeout = 50)
    public void UserManagerCreateUserTest() throws DuplicateUsernameException, UserNotFoundException {
        UserManager m = new UserManager();
        m.createUser("123", "456");
        RegularUser r = (RegularUser) m.getUser("123");
        assertEquals("User does not exist", "123", r.getUsername());
        assertEquals("Not an Admin User", false, r.isAdmin());
    }

    @Test(timeout = 50)
    public void UserManagerCreatAdminUserTest() throws DuplicateUsernameException, UserNotFoundException {
        UserManager m = new UserManager();
        m.createAdminUser("123", "456");
        AdminUser a = (AdminUser) m.getUser("123");
        assertEquals("Username does not exist", "123",a.getUsername());
    }

    @Test(timeout = 50)
    public void LogInTest() throws DuplicateUsernameException, UserNotFoundException, NoPermissionException {
        UserManager m = new UserManager();
        m.createUser("123", "456");
        m.logIn("123", "456");
        assertEquals(m.userLoggedIn(), true);
        m.logOut();
        assertEquals(m.userLoggedIn(), false);
    }

    @Test(timeout = 50)
    public void banUserTest() throws DuplicateUsernameException, UserNotFoundException, NoPermissionException, InvalidCommandException {
        UserManager m = new UserManager();
        m.createAdminUser("123", "456");
        m.createUser("abc", "def");
        m.logIn("123", "456");
        RegularUser u1 = (RegularUser) m.getUser("abc");
        assertEquals(u1.getIsBanned(), false);
        m.banUser("abc");
        assertEquals(u1.getIsBanned(), true);
        m.unbanUser("abc");
        assertEquals(u1.getIsBanned(), false);
    }

}
