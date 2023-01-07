package Shell.Controller;
import HelperClasses.NotFoundException;
import Shell.Presenter.ShellPresenter;
import UseCase.*;

/** The part of the Shell responsible for what to do when in comment edit mode.
 */
public class ShellMenuEditComment extends ShellMenu {

    /** Constructor for ShellMenuEditComment.
     * @param p the presenter
     * @param plans the plan manager
     * @param logs the logging manager
     * @param comms the comment manager
     */
    public ShellMenuEditComment(ShellPresenter p, PlanManager plans, CommentManager comms, LoggingManager logs) {
        commands.put("1", this::back);
        commands.put("2", () -> viewComments(p, plans, comms));
        commands.put("3", () -> addComment(p, logs, plans, comms));
        commands.put("4", () -> editComment(p, plans, comms));
        commands.put("5", () -> deleteComment(p, plans, comms));
        commands.put("6", () -> clearComments(p, plans, comms));
        commands.put("7", () -> reply(p, plans, comms, logs));
    }

    /** Run the Comment edit mode Presenter.
     */
    @Override
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("Comment Editor. Choose the commands below:",
                "Back", "View Comments", "Add Comment", "Edit Comment", "Delete Comment", "Clear Comments",
                "Reply to Comment"));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Go back a stage in MenuStates
     */
    private void back() {
        setMenuState(MenuState.EDIT_PLAN);
    }

    /** View Comments on a Plan.
     * @param p the presenter
     * @param plans the plan manager
     * @param comms the comment manager
     */
    private void viewComments(ShellPresenter p, PlanManager plans, CommentManager comms) {
        try {
            if (comms.getComments(plans.getCurrPlanID()).isEmpty())
                p.displayResult("No comments yet.\n");
            else
                p.displayResult(comms.toString(plans.getCurrPlanID()));
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** ADd Comment to a Plan.
     * @param p the presenter
     * @param logs the logging manager
     * @param plans the plan manager
     * @param comms the comment manager
     */
    private void addComment(ShellPresenter p, LoggingManager logs, PlanManager plans, CommentManager comms) {
        String[] args = p.getArguments(false, "comment text");
        String commentId = comms.addComment(plans.getCurrPlanID(), logs.getCurrUsername(), args[0]);
        p.displayResult("Successfully created comment \"" + args[0] + "\" with id " + commentId + "\n");
    }

    /** Edit Comments on a Plan.
     * @param p the presenter
     * @param plans the plan manager
     * @param comms the comment manager
     */
    private void editComment(ShellPresenter p, PlanManager plans, CommentManager comms) {
        String[] args = p.getArguments(false, "comment ID to edit", "new comment text");
        try {
            comms.editComment(plans.getCurrPlanID(), args[0], args[1]);
            p.displayResult("Successfully changed text of comment.\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** Delete Comment on a Plan.
     * @param p the presenter
     * @param plans the plan manager
     * @param comms the comment manager
     */
    public void deleteComment(ShellPresenter p, PlanManager plans, CommentManager comms) {
        String[] args = p.getArguments(false, "comment ID to delete");
        try {
            comms.deleteComment(plans.getCurrPlanID(), args[0]);
            p.displayResult("Successfully deleted comment of ID " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** Reply to a comment on a Plan.
     * @param p the presenter
     * @param plans the plan manager
     * @param comms the comment manager
     * @param logs the logging manager
     */
    private void reply(ShellPresenter p, PlanManager plans, CommentManager comms, LoggingManager logs) {
        try {
            String[] args = p.getArguments(false, "comment ID to reply to", "comment text");
            comms.addReply(plans.getCurrPlanID(), args[0], logs.getCurrUsername(), args[1]);
            p.displayResult("Successfully replied to comment of ID " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /** Clear Comments on a Plan.
     * @param p the presenter
     * @param plans the plan manager
     * @param comms the comment manager
     */
    private void clearComments(ShellPresenter p, PlanManager plans, CommentManager comms) {
        p.displayResult("Are you sure? Type \"clear\" to continue.");
        String args = p.getInput();
        if (args.equals("clear")) {
            comms.clearComments(plans.getCurrPlanID());
            p.displayResult("Successfully cleared all comments.\n");
        }
    }
}
