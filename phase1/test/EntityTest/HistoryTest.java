package EntityTest;

import Entities.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HistoryTest {

    @Test(timeout = 50)
    public void historyTest(){
        History h = new History("12345", History.Action.log_in);

    }

    @Test(timeout = 50)
    public void  historyMethodTest(){
        History h = new History("12345", History.Action.log_in);
        assertEquals("incorrect userName", "12345", h.getUsername());
        assertEquals("incorrect Action", History.Action.log_in, h.getAction());
    }
}
