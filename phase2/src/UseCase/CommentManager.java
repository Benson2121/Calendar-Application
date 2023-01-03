package UseCase;
import Entities.Comment;
import HelperClasses.NotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class CommentManager extends Saveable implements Serializable {
    private Map<String, Map<String, Comment>> comments = new HashMap<>(); // Map of PlanID to a Map of comment String to Comment

    /** Add a reply to a Comment.
     * @param planID a String of planID which contains comment you want to add a reply to
     * @param commentID a String of commentID you want to reply to
     * @param username the username of who is making reply
     * @param text the reply's text
     */
    public void addReply(String planID, String commentID, String username, String text) throws NotFoundException {
        getComment(planID, commentID).addReply(comments.get(planID).get(addComment(planID, username, text)));
        makeChange();
    }

    /** Create a comment and add it to a plan.
     * @param planID a String of planID you want to add a comment to
     * @param username the username of who is making the comment
     * @param text a String of Comment
     * @return the ID of the added comment
     */
    public String addComment(String planID, String username, String text) {
        Comment c = new Comment(text, username);
        c.setId(c.assignID(c.getText(), comments.get(planID).keySet(), username));
        comments.get(planID).put(c.getId(), c);
        makeChange();
        return c.getId();
    }

    /** Edit a comment.
     * @param planID the planID to which the comment belongs to
     * @param commentID the ID of the comment to be changed
     * @param text the new comment text
     * @throws NotFoundException the comment's ID cannot be found
     */
    public void editComment(String planID, String commentID, String text) throws NotFoundException {
        getComment(planID, commentID).setText(text);
        makeChange();
    }

    /** Delete a comment.
     * @param planID a String of planID you want to delete a comment from
     * @param commentID the ID of the comment you want to delete
     * @throws NotFoundException the comment's ID cannot be found
     */
    public void deleteComment(String planID, String commentID) throws NotFoundException {
        List<String> replyIDs = new ArrayList<>();
        for (Comment c : getComment(planID, commentID).getReplies()) {
            replyIDs.add(c.getId());
        }
        Map<String, Comment> coms = comments.get(planID);
        for (String c : coms.keySet()) {
            if (replyIDs.contains(c))
                coms.remove(c);
        }
        coms.remove(commentID);
        makeChange();
    }

    /** Get a comment.
     * @param planID the planID to which the comment belongs to
     * @param commentID the ID of the comment
     * @return the comment
     * @throws NotFoundException the comment's ID cannot be found
     */
    public Comment getComment(String planID, String commentID) throws NotFoundException {
        Map<String, Comment> coms = comments.get(planID);
        if (coms == null)
            throw new NotFoundException(planID);
        Comment com = coms.get(commentID);
        if (com == null)
            throw new NotFoundException(commentID);
        return com;
    }

    /** Clear all comments for a plan with PlanID.
     * @param planID a String of the planID
     */
    public void clearComments(String planID) {
        comments.get(planID).clear();
        makeChange();
    }

    /** Get a map of Comments with String of comment as the key and the reply of this comment as the value
     * @param planID a String of the Plan's planID you want to get comments for
     * @return a map of Comments with String of comment as the key and reply Comment as the value
     * @throws NotFoundException a plan with PlanID cannot be found
     */
    public Map<String, Comment> getComments(String planID) throws NotFoundException {
        Map<String, Comment> comment_reply = comments.get(planID);
        if (comment_reply == null) {
            throw new NotFoundException(planID);
        }
        return comment_reply;
    }

    /** toString() Display Comments for Plan with given planID
     * @param planID a String of the Plan's planID to display comments
     * @return a String with comments and replies being displayed
     */
    public String toString(String planID) throws NotFoundException {
        StringBuilder str = new StringBuilder("Comments of " + planID + ": \n");
        Map<String, Comment> coms = comments.get(planID);
        if (coms == null)
            throw new NotFoundException(planID);

        List<String> replyIDsStrings = new ArrayList<>();
        for (String commentID : coms.keySet()) {
            if (!replyIDsStrings.contains(commentID)) {
                str.append(getComment(planID, commentID)).append("\n");
                replyIDsStrings.addAll(getAllReplyIDs(planID, commentID));
            }
        }
        return str.toString();
    }

    private List<String> getAllReplyIDs(String planID, String commentID) throws NotFoundException {
        List<String> replyIDs = new ArrayList<>();
        replyIDs.add(commentID);
        for (Comment reply : getComment(planID, commentID).getReplies()) {
            replyIDs.addAll(getAllReplyIDs(planID, reply.getId()));
        }
        return replyIDs;
    }

    // Methods on updating the map when planIDS are created/changed/deleted

    /** Deletes all labels associated with all Plans associated with a user.
     * @param planIDs A list of all the user's planIDs.
     */
    public void handleClearUserPlans(List<String> planIDs) {
        for (String planID : planIDs) {
            comments.remove(planID);
            makeChange();
        }
    }

    /** Handle creating a new plan.
     * Create a new plan and store in the thematicLabels and progressLabels
     * @param planID a String of the Plan's plan ID to create
     */
    public void handleNewPlan(String planID) {
        comments.put(planID, new HashMap<>());
        makeChange();
    }

    /** Handle deleting a plan.
     * @param planID a String of the Plan's planID to delete
     */
    public void handleDeletePlan(String planID) {
        if (comments.containsKey(planID)) {
            comments.remove(planID);
            makeChange();
        }
    }

    /** Handling changing a plan's name. Change a Plan's planID
     * @param oldID a String of the Plan's original planID
     * @param newID a String of the Plan's new planID
     */
    public void handleSetPlanName(String oldID, String newID) {
        comments.put(newID, comments.get(oldID));
        comments.remove(oldID);
        for (String commentID : comments.get(newID).keySet()) {
            comments.get(newID).get(commentID).setUsername(newID);
        }
        makeChange();
    }

    /** Return True if a planID is a key in comments
     * @param planID plan id
     * @return a Boolean shows whether a Plan exists or not
     */
    public Boolean containsPlanID(String planID){
        return comments.containsKey(planID);
    }

    @Override
    public void save() throws IOException {
        getSaver().save(this, "comments");
    }

    @Override
    public void load() throws IOException, ClassNotFoundException {
        comments = getSaver().load(this, "comments").comments;
    }
}
