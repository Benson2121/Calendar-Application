package EntityTest;

import Entities.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdminUserTest {
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
}