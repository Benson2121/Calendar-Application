package EntityTest;

import Entities.Label;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LabelTest {

    @Test(timeout = 50)
    public void testLabel(){
        Label label1 = new Label("military");
        assertEquals("military", label1.getName());
        label1.setName("foreign");
        assertEquals("foreign", label1.getName());

    }
}
