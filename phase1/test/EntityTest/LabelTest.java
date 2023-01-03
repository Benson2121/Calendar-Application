package EntityTest;

import Entities.Label;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LabelTest {

    @Test(timeout = 50)
    public void testLabel(){
        Label l1 = new Label("CSC207", true);
        Label l2 = new Label("CSC236", false);
        assertEquals("CSC207", l1.getName());
        assertEquals(true, l1.isThematic());
        assertEquals("CSC207 (Thematic)", l1.toString());
        l1.setName("CSC258");
        assertEquals("CSC258", l1.getName());
        assertEquals(false, l2.isThematic());

    }
}
