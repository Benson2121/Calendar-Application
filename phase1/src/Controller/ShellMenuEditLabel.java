package Controller;

import HelperClasses.NotFoundException;
import UseCase.ShellData;

/** The part of the Shell responsible for what to do when in label edit mode.
 * @author Richard Yin
 */
public class ShellMenuEditLabel extends ShellMenu {
    public ShellMenuEditLabel(ShellData data) {
        commands.put("1", this::back);
        commands.put("2", () -> viewLabels(data));
        commands.put("3", () -> editLabel(data));
        commands.put("4", () -> deleteLabel(data));
        commands.put("5", () -> clearLabels(data));
        commands.put("6", () -> addThematicLabel(data));
        commands.put("7", () -> addProgressLabel(data));
    }
    private String currPlanID(ShellData data) {
        return data.getPlans().getCurrPlanID();
    }
    public void run(ShellData data) {
        try {
            if (data.getPlans().isEvent(currPlanID(data), data.getCalendar().getCurrCalendarID())) {
                commands.remove("7");
                handleInput(data, data.getPresenter().editLabelPrompter(true));
            } else {
                commands.put("7", () -> addProgressLabel(data));
                handleInput(data, data.getPresenter().editLabelPrompter(false));
            }
        } catch (NotFoundException e) {
            data.getPresenter().displayResult("Something went very wrong!");
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void back() {
        setMenuState(MenuState.EDIT_PLAN);
    }
    private void editLabel(ShellData data) {
        try {
            String[] args = data.getPresenter().getArguments(false, "name of the label to edit", "new label");
            data.getPresenter().displayResult(data.getLabels().setLabel(args[0], args[1], currPlanID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void deleteLabel(ShellData data) {
        try {
            String[] args = data.getPresenter().getArguments(false, "name of the label to delete");
            data.getPresenter().displayResult(data.getLabels().deleteLabel(args[0], currPlanID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void clearLabels(ShellData data) {
        data.getPresenter().displayResult("Are you sure? Type \"clear\" to continue.");
        String args = data.getPresenter().getInput();
        data.setAllDataSaved(false);
        if (args.equals("clear")) {
            data.getPresenter().displayResult(data.getLabels().clearLabels(currPlanID(data)));
        }
    }
    private void addThematicLabel(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "name for the new label");
        data.getPresenter().displayResult(data.getLabels().addThematicLabel(args[0], currPlanID(data)));
        data.setAllDataSaved(false);
    }
    private void addProgressLabel(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "name for the new label");
        data.getPresenter().displayResult(data.getLabels().addProgressLabel(args[0], currPlanID(data)));
        data.setAllDataSaved(false);
    }
    private void viewLabels(ShellData data) {
        data.getPresenter().displayResult(data.getLabels().toString(currPlanID(data)));
    }
}
