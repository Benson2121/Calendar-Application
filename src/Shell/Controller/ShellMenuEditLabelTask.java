package Shell.Controller;
import Entities.Label;
import Gateway.*;
import Shell.Presenter.ShellPresenter;
import UseCase.*;
import java.util.*;

/** The part of the Shell responsible for what to do when in label edit mode for a task.
 * @author Richard Yin
 */
public class ShellMenuEditLabelTask extends ShellMenuEditLabelEvent {

    /** Constructor for ShellMenuEditLabelTask.
     * @param p the presenter
     * @param s the saver
     * @param plans the plan manager
     * @param cals the calendar manager
     * @param logs the logging manager
     * @param labs the label manager
     * @param hists the history manager
     */
    public ShellMenuEditLabelTask(ShellPresenter p, InterfaceSaver s, CalendarManager cals, PlanManager plans, LabelManager labs, LoggingManager logs, HistoryManager hists) {
        super(p, s, cals, plans, labs, logs, hists);
        commands.put("7", () -> setProgressLabel(p, plans, labs));
    }

    /** Run the Label Task edit mode Presenter.
     */
    @Override
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("Label Editor. Choose the commands below:",
                "Back", "View Labels", "Edit Label", "Delete Label", "Clear Labels", "Add Thematic Label", "Set Progress Label"));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Set the enum of a progress label to a certain option.
     * @param p The presenter
     * @param plans The plan manager
     * @param labs The label manager
     */
    private void setProgressLabel(ShellPresenter p, PlanManager plans, LabelManager labs) {
        String arg = p.getArguments(List.of("not started", "in progress", "complete", "stalled"), "progress label")[0];
        Map<String, Label.ProgressLabels> l = Map.of(
                "not started", Label.ProgressLabels.NOT_STARTED,
                "in progress", Label.ProgressLabels.IN_PROGRESS,
                "complete", Label.ProgressLabels.COMPLETE,
                "stalled", Label.ProgressLabels.STALLED
        );
        labs.setProgressLabel(l.get(arg), plans.getCurrPlanID());
        p.displayResult("Successfully changed progress label to \"" + arg + "\"\n");
    }
}
