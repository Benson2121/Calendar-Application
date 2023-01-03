package UseCaseTest;

import Gateway.InterfaceSaver;
import Gateway.Saver;
import HelperClasses.NotFoundException;
import UseCase.CommentManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentManagerTest {
    @Test(timeout = 50)
    public void handleNewPlanTest() {
        CommentManager comms = new CommentManager();
        InterfaceSaver save = new Saver();
        comms.setSaver(save);
        comms.handleNewPlan("abc");
        assertTrue(comms.containsPlanID("abc"));
        comms.handleNewPlan("def");
        assertTrue(comms.containsPlanID("def"));
    }

    @Test(timeout = 50)
    public void handleDeletePlanTest(){
        CommentManager comms = new CommentManager();
        InterfaceSaver save = new Saver();
        comms.setSaver(save);
        comms.handleNewPlan("abc");
        assertTrue(comms.containsPlanID("abc"));
        comms.handleNewPlan("def");
        assertTrue(comms.containsPlanID("def"));
        comms.handleDeletePlan("abc");
        assertFalse(comms.containsPlanID("abc"));
        comms.handleDeletePlan("def");
        assertFalse(comms.containsPlanID("def"));
    }

    @Test(timeout = 50)
    public void addCommentTest() throws NotFoundException {
        CommentManager comms = new CommentManager();
        InterfaceSaver save = new Saver();
        comms.setSaver(save);
        String planID = "123";
        comms.handleNewPlan(planID);
        String id = comms.addComment(planID, "abc", "hi");
        assertTrue(comms.getComments(planID).containsKey(id));
    }

    @Test(timeout = 50)
    public void editCommentTest() throws NotFoundException {
        CommentManager comms = new CommentManager();
        InterfaceSaver save = new Saver();
        comms.setSaver(save);
        comms.handleNewPlan("123");
        String id = comms.addComment("123", "abc", "hi");
        comms.editComment("123", id, "hello");
        assertEquals(comms.getComment("123", id).getText(), "hello");
    }

    @Test(timeout = 50)
    public void addReplyTest() throws NotFoundException {
        CommentManager comms = new CommentManager();
        InterfaceSaver save = new Saver();
        comms.setSaver(save);
        comms.handleNewPlan("123");
        String id = comms.addComment("123", "abc", "hi");
        comms.addReply("123", id, "abc", "hello");
        assertEquals(comms.getComments("123").get(id).getReplies().get(0).getText(), "hello");
    }
}
