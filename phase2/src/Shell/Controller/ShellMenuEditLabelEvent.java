package Shell.Controller;
import Gateway.*;
import HelperClasses.NotFoundException;
import Shell.Presenter.ShellPresenter;
import UseCase.*;

import java.io.IOException;

/** The part of the Shell responsible for what to do when in label edit mode for an event.
 * @author Richard Yin
 */
public class ShellMenuEditLabelEvent extends ShellMenu {

    /** Constructor for ShellMenuEditLabelEvent.
     * @param p the presenter
     * @param s the saver
     * @param plans the plan manager
     * @param cals the calendar manager
     * @param logs the logging manager
     * @param labs the label manager
     * @param hists the history manager
     */
    public ShellMenuEditLabelEvent(ShellPresenter p, InterfaceSaver s, CalendarManager cals, PlanManager plans, LabelManager labs, LoggingManager logs, HistoryManager hists) {
        commands.put("1", () -> back(p, s, hists, logs, cals));
        commands.put("2", () -> viewLabels(p, plans, labs));
        commands.put("3", () -> editLabel(p, plans, labs));
        commands.put("4", () -> deleteLabel(p, plans, labs));
        commands.put("5", () -> clearLabels(p, plans, labs));
        commands.put("6", () -> addThematicLabel(p, plans, labs));
    }

    /** Run the Label Event edit mode Presenter.
     */
    @Override
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("Label Editor. Choose the commands below:",
                "Back", "View Labels", "Edit Label", "Delete Label", "Clear Labels", "Add Thematic Label"));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Go back to the edit event menu.
     * @param p The presenter
     * @param s The saver
     * @param hists The history manager
     * @param logs The logging manager
     * @param cals The calendar manager
     */
    private void back(ShellPresenter p, InterfaceSaver s, HistoryManager hists, LoggingManager logs, CalendarManager cals) {
        setMenuState(MenuState.EDIT_PLAN);
        try {
            if (s.changesMade())
                hists.addHistory(logs.getCurrUsername(), "Edited calendar " + cals.getCalendar(cals.getCurrCalendarID()));
        } catch (IOException | NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Edit the name of a thematic label.
     * @param p The presenter
     * @param plans The plan manager
     * @param labs The label manager
     */
    private void editLabel(ShellPresenter p, PlanManager plans, LabelManager labs) {
        try {
            String[] args = p.getArguments(false, "name of the label to edit", "new label");
            labs.setThematicLabel(args[0], args[1], plans.getCurrPlanID());
            p.displayResult("Successfully changed label name " + args[0] + " to " + args[1] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Delete a thematic label.
     * @param p The presenter
     * @param plans The plan manager
     * @param labs The label manager
     */
    private void deleteLabel(ShellPresenter p, PlanManager plans, LabelManager labs) {
        try {
            String[] args = p.getArguments(false, "name of the label to delete");
            labs.deleteThematicLabel(args[0], plans.getCurrPlanID());
            p.displayResult("Successfully deleted label name " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Remove all thematic labels for a plan.
     * @param p The presenter
     * @param plans The plan manager
     * @param labs The label manager
     */
    private void clearLabels(ShellPresenter p, PlanManager plans, LabelManager labs) {
        p.displayResult("Are you sure? Type \"clear\" to continue.");
        String args = p.getInput();
        if (args.equals("clear")) {
            labs.clearLabels(plans.getCurrPlanID());
            p.displayResult("Successfully cleared all labels\n");
        }
    }

    /**
     * Add a thematic label.
     * @param p The presenter
     * @param plans The plan manager
     * @param labs The label manager
     */
    private void addThematicLabel(ShellPresenter p, PlanManager plans, LabelManager labs) {
        String[] args = p.getArguments(false, "name for the new label");
        labs.addThematicLabel(args[0], plans.getCurrPlanID());
        p.displayResult("Successfully added thematic label name " + args[0] + "\n");
    }

    /**
     * Display all thematic labels.
     * @param p The presenter
     * @param plans The plan manager
     * @param labs The label manager
     */
    private void viewLabels(ShellPresenter p, PlanManager plans, LabelManager labs) {
        p.displayResult(labs.toString(plans.getCurrPlanID()));
    }
}
