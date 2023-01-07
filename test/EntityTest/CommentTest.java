package EntityTest;

import Entities.Comment;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    @Test(timeout = 50)
    public void createCommentTest(){
        Comment c = new Comment("hello", "user123");
        assertEquals("hello", c.getText());
        assertEquals("user123", c.getUsername());
    }

    @Test(timeout = 50)
    public void setTextTest(){
        Comment c = new Comment("hello", "user123");
        assertEquals("hello", c.getText());
        c.setText("bye");
        assertEquals("bye", c.getText());
    }

    @Test(timeout = 50)
    public void setUsernameTest(){
        Comment c = new Comment("hello", "user123");
        assertEquals("user123", c.getUsername());
        c.setUsername("user456");
        assertEquals("user456", c.getUsername());
    }

    @Test(timeout = 50)
    public void addReplyTest(){
        Comment c1 = new Comment("hello", "user123");
        Comment c2 = new Comment("how are you?", "user456");
        c1.addReply(c2);
        List<Comment> replies = c1.getReplies();
        assertTrue(replies.contains(c2));
    }

    @Test(timeout = 50)
    public void removeReplyTest(){
        Comment c1 = new Comment("hello", "user123");
        Comment c2 = new Comment("how are you?", "user456");
        c1.addReply(c2);
        c1.removeReply(c2);
        List<Comment> replies = c1.getReplies();
        assertTrue(replies.isEmpty());
    }

}
