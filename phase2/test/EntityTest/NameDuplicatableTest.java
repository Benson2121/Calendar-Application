package EntityTest;

import Entities.*;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertEquals;

public class NameDuplicatableTest {
    @Test
    public void testAssignID() {
        NameDuplicatable t = new Event("name", "desc");
        ArrayList<String> ids = new ArrayList<>();
        String t1 = t.assignID("hi", ids, "hi");
        ids.add(t1);
        String t2 = t.assignID("hi", ids, "hi");
        ids.add(t1);
        String t3 = t.assignID("hi", ids, "hi");
        assertEquals("First instance should be hihi", "hihi", t1);
        assertEquals("Second instance should be hihi1", "hihi1", t2);
        assertEquals("Third instance should be hihi2", "hihi2", t3);
        Set<String> mySet = new HashSet<>();
        mySet.add("hihi");
        mySet.add("hihi1");
        mySet.add("hihi2");
        ArrayList<String> ids2 = new ArrayList<>(mySet);
        String t4 = t.assignID("hi", ids2, "hi");
        assertEquals("Fourth instance should be hihi3", "hihi3", t4);
    }
}
