package Entities;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/** An entity class corresponding to a Comment.
 */
public class Comment extends NameDuplicatable implements Serializable {
    private String text;
    private String username;
    private String id;
    private final LocalDateTime posted = LocalDateTime.now();
    private LocalDateTime updated = null;
    private final List<Comment> replies = new ArrayList<>();

    /** Constructor for Comment.
     */
    public Comment() {
        text = username = null;
    }

    /** Constructor for Comment.
     * @param text the comment's text
     * @param username the commenter's username
     */
    public Comment(String text, String username) {
        this.text = text;
        this.username = username;
    }

    /** Get the current comment
     * @return comment
     */
    public String getText() {
        return text;
    }

    /** Set the current comment
     * @param text the new/first comment
     */
    public void setText(String text) {
        this.text = text;
        updated = LocalDateTime.now();
    }

    /** String representation of a Comment.
     * @return Comment as a String
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(username + " | " + posted);
        if (updated != null)
            s.append(" (updated ").append(updated).append(")");
        s.append(" | id: ").append(id).append("\n").append(text);
        if (replies.size() > 0) {
            for (Comment c : replies) {
                s.append(("\n" + c.toNestedString()).replaceAll("\n", "\n    "));
            }
        }
        return s.toString();
    }

    /** Nested String representation of a Comment and its replies.
     * @return Comment and its replies as a String
     */
    private String toNestedString() {
        StringBuilder s = new StringBuilder("└ " + username + " | " + posted);
        if (updated != null)
            s.append(" (updated ").append(updated).append(")");
        s.append(" | id: ").append(id).append("\n  ").append(text);
        if (replies.size() > 0) {
            for (Comment c : replies) {
                s.append(("\n" + c.toNestedString()).replaceAll("\n", "\n    "));
            }
        }
        return s.toString();

    }
    /** Get username of who made comment.
     * @return commenter's username
     */
    public String getUsername() {
        return username;
    }

    /** Set username of who made comment.
     * @param username  commenter's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Add a reply to a comment.
     * @param c reply comment
     */
    public void addReply(Comment c) {
        replies.add(c);
    }

    /** Remove a reply to a comment.
     * @param c reply comment to be removed
     */
    public void removeReply(Comment c) {
        replies.remove(c);
    }

    /** Get all comment's replies.
     * @return comment's replies
     */
    public List<Comment> getReplies() {
        return replies;
    }

    /** Get comment's ID.
     * @return comment's ID
     */
    public String getId() {
        return id;
    }

    /** Set comment's ID.
     * @param id comment's new/first ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /** If comment has a reply with certain ID.
     * @param id the id of possible reply
     * @return true/false if comment contains reply with id.
     */
    public boolean containsReplyId(String id) {
        for (Comment c : replies) {
            if (c.getId().equals(id))
                return true;
        }
        return false;
    }
}
