package EntityTest;

import Entities.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HistoryTest {

    @Test(timeout = 50)
    public void historyTest(){
        History h = new History("12345", "Log in");
        assertNotNull(h);

    }

    @Test(timeout = 50)
    public void  historyMethodTest(){
        History h = new History("12345", "Log in");
        assertEquals("incorrect userName", "12345", h.getUsername());
        assertEquals("incorrect Action", "Log in", h.getAction());
    }
}
