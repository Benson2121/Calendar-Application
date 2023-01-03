package EntityTest;

import Entities.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test(timeout = 50)
    public  void testDefaultAdminUser(){
        AdminUser u = new AdminUser();
    }
    @Test(timeout = 50)
    public void testAdminUser() {
        AdminUser u = new AdminUser("12345", "678910");
    }

    @Test(timeout = 50)
    public void testAdminUserMethod() {
        AdminUser u = new AdminUser("12345", "678910");
        assertEquals("An AdminUser", true, u.isAdmin());
    }
    @Test(timeout = 50)
    public void testDefaultRegularUser(){
        RegularUser u = new RegularUser();
    }

    @Test(timeout = 50)
    public void testRegularUser(){
        RegularUser u = new RegularUser("12345", "678910");

    }
    @Test(timeout = 50)
    public void testRegularUserMethod(){
        RegularUser u = new RegularUser("12345", "678910");
        assertEquals("incorrect Banned Status", false, u.getIsBanned());
        u.setIsBanned(true);
        assertEquals("incorrect Banned Status", true, u.getIsBanned());
        assertEquals("Not a AdminUser", false, u.isAdmin());
    }

    @Test(timeout = 50)
    public void testUserAuthenticate(){
        RegularUser u = new RegularUser("12345", "678910");
        assertEquals("Unmatched Username", false,
                u.authenticate("123456", "678910"));
        assertEquals("Unmatched Password", false,
                u.authenticate("12345", "6789"));
        assertEquals("Matched Username and Password", true,
                u.authenticate("12345", "678910"));
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