package EntityTest;

import Entities.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    @Test(timeout = 50)
    public  void testDefaultAdminUser(){
        AdminUser u = new AdminUser();
        assertNotNull(u);
    }
    @Test(timeout = 50)
    public void testAdminUser() {
        AdminUser u = new AdminUser("12345", "678910");
        assertNotNull(u);
    }

    @Test(timeout = 50)
    public void testAdminUserMethod() {
        AdminUser u = new AdminUser("12345", "678910");
        assertTrue("An AdminUser", u.isAdmin());
    }
    @Test(timeout = 50)
    public void testDefaultRegularUser(){
        RegularUser u = new RegularUser();
        assertNotNull(u);
    }

    @Test(timeout = 50)
    public void testRegularUser(){
        RegularUser u = new RegularUser("12345", "678910");
        assertNotNull(u);
    }
    @Test(timeout = 50)
    public void testRegularUserMethod(){
        RegularUser u = new RegularUser("12345", "678910");
        assertFalse("incorrect Banned Status", u.getIsBanned());
        u.setIsBanned(true);
        assertTrue("incorrect Banned Status", u.getIsBanned());
        assertFalse("Not a AdminUser", u.isAdmin());
    }

    @Test(timeout = 50)
    public void testUserAuthenticate(){
        RegularUser u = new RegularUser("12345", "678910");
        assertFalse("Unmatched Username", u.authenticate("123456", "678910"));
        assertFalse("Unmatched Password", u.authenticate("12345", "6789"));
        assertTrue("Matched Username and Password", u.authenticate("12345", "678910"));
    }

    @Test(timeout = 50)
    public void testSetterUserName(){
        RegularUser u = new RegularUser("12345", "wasd");
        assertEquals("12345", u.getUsername());
        assertEquals("wasd", u.getPassword());
        u.setUsername("23456");
        assertEquals("23456", u.getUsername());
        u.setPassword("1234");
        assertEquals("1234", u.getPassword());
    }


}