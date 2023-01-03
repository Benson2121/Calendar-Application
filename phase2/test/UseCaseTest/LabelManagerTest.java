package UseCaseTest;

import Gateway.InterfaceSaver;
import Gateway.Saver;
import HelperClasses.NotFoundException;
import UseCase.LabelManager;
import org.junit.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class LabelManagerTest {
    @Test(timeout = 50)
    public void handleNewPlanTest(){
        LabelManager labs = new LabelManager();
        InterfaceSaver save = new Saver();
        labs.setSaver(save);
        labs.handleNewPlan("abc", true);
        assertTrue(labs.containsPlanID("abc"));
        labs.handleNewPlan("def", false);
        assertTrue(labs.containsPlanID("def"));
    }
    @Test(timeout = 50)
    public void handleDeletePlanTest(){
        LabelManager labs = new LabelManager();
        InterfaceSaver save = new Saver();
        labs.setSaver(save);
        labs.handleNewPlan("abc", true);
        assertTrue(labs.containsPlanID("abc"));
        labs.handleNewPlan("def", false);
        assertTrue(labs.containsPlanID("def"));
        labs.handleDeletePlan("abc");
        assertFalse(labs.containsPlanID("abc"));
        labs.handleDeletePlan("def");
        assertFalse(labs.containsPlanID("def"));
    }
    @Test(timeout = 50)
    public void addThematicLabelTest(){
        LabelManager labs = new LabelManager();
        InterfaceSaver save = new Saver();
        labs.setSaver(save);
        labs.handleNewPlan("abc", true);
        labs.addThematicLabel("aaa", "abc");
        Set<String> labelsID = new HashSet<>();
        labelsID.add("aaa");
        assertTrue(labs.getPlanIDsWithLabels(labelsID).contains("abc"));
    }
    @Test(timeout = 50)
    public void setThematicLabelTest() throws NotFoundException {
        LabelManager labs = new LabelManager();
        InterfaceSaver save = new Saver();
        labs.setSaver(save);
        labs.handleNewPlan("abc", true);
        labs.addThematicLabel("aaa", "abc");
        labs.setThematicLabel("aaa", "bbb", "abc");
        Set<String> labelsID = new HashSet<>(List.of("bbb"));
        assertTrue(labs.getPlanIDsWithLabels(labelsID).contains("abc"));
    }
}
