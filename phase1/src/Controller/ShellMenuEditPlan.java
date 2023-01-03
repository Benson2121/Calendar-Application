package Controller;

import HelperClasses.*;
import UseCase.ShellData;

/** An abstract class containing common commands between the event/task edit menus.
 * @author Richard Yin
 */
public class ShellMenuEditPlan extends ShellMenu {
    public ShellMenuEditPlan(ShellData data) {
        commands.put("1", this::back);
        commands.put("2", () -> addEvent(data));
        commands.put("3", () -> addTask(data));
        commands.put("4", () -> addSubevent(data));
        commands.put("5", () -> addSubtask(data));
        commands.put("6", () -> editName(data));
        commands.put("7", () -> editDescription(data));
        commands.put("8", () -> editLabels(data));
        commands.put("9", () -> editTime(data));
        commands.put("10", () -> nestPlan(data));
        commands.put("11", () -> unnestPlan(data));
        commands.put("12", () -> deletePlan(data));
        commands.put("13", () -> clearEvents(data));
        commands.put("14", () -> clearTasks(data));
        commands.put("15", () -> viewEvents(data));
        commands.put("16", () -> viewTasks(data));
    }
    private String currCalendarID(ShellData data) {
        return data.getCalendar().getCurrCalendarID();
    }
    public void run(ShellData data) {
        handleInput(data, data.getPresenter().editPlanPrompter());
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void back() {
        setMenuState(MenuState.EDIT_CALENDAR);
    }
    private void addEvent(ShellData data) {
        String[] args = data.getPresenter().getArguments("01", "name for the event", "description for the event");
        String[] result = data.getPlans().createEvent(args[0], args[1], currCalendarID(data));
        data.getPresenter().displayResult(result[0]);
        data.getLabels().handleNewPlan(result[1]);
        data.setAllDataSaved(false);
    }
    private void addTask(ShellData data) {
        String[] args = data.getPresenter().getArguments("01", "name for the task", "description for the task");
        String[] result = data.getPlans().createTask(args[0], args[1], currCalendarID(data));
        data.getPresenter().displayResult(result[0]);
        data.getLabels().handleNewPlan(result[1]);
        data.setAllDataSaved(false);
    }
    private void addSubevent(ShellData data) {
        String[] args = data.getPresenter().getArguments("001", "parent event ID", "name for the subevent", "description for the subevent");
        try {
            String[] result = data.getPlans().createEvent(args[1], args[2], currCalendarID(data));
            data.getPresenter().displayResult(result[0]);
            data.getLabels().handleNewPlan(result[1]);
            data.getPresenter().displayResult(data.getPlans().nest(args[0], result[1], currCalendarID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e + " because " + args[0] + " is not a parent event of " + args[1]);
        }
    }
    private void addSubtask(ShellData data) {
        String[] args = data.getPresenter().getArguments("001", "parent task ID", "name for the subtask", "description for the subtask");
        try {
            String[] result = data.getPlans().createTask(args[1], args[2], currCalendarID(data));
            data.getPresenter().displayResult(result[0]);
            data.getLabels().handleNewPlan(result[1]);
            data.getPresenter().displayResult(data.getPlans().nest(args[0], result[1], currCalendarID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e + " because " + args[0] + " is not a parent task of " + args[1]);
        }
    }
    private void editName(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "event/task ID", "name to change to");
        try {
            String[] result = data.getPlans().changePlanName(args[1], args[0], currCalendarID(data));
            data.getPresenter().displayResult(result[0]);
            data.getLabels().handleSetPlanName(args[0], result[1]);
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void editDescription(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "event/task ID", "description to change to");
        try {
            data.getPresenter().displayResult(data.getPlans().changePlanDescription(args[1], args[0], currCalendarID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void editLabels(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "event/task ID");
        try {
            data.getPlans().setCurrPlanID(args[0], currCalendarID(data));
            setMenuState(MenuState.EDIT_LABEL);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void editTime(ShellData data) {
        String planID = data.getPresenter().getArguments(false, "event/task ID")[0];
        try {
            if (data.getPlans().isEvent(planID, currCalendarID(data))) {
                String[] args = data.getPresenter().getArguments(false, "event start time (YYYY/MM/DD HH:MM, time/year is omittable)", "event end time (YYYY/MM/DD HH:MM, time/year is omittable)");
                data.getPresenter().displayResult(data.getPlans().setTime(planID, args[0], args[1], currCalendarID(data)));
            } else {
                String[] args = data.getPresenter().getArguments(false, "task deadline time (YYYY/MM/DD HH:MM, time/year is omittable)");
                data.getPresenter().displayResult(data.getPlans().setDeadline(planID, args[0], currCalendarID(data)));
            }
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void nestPlan(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "parent event/task ID", "child event/task ID");
        try {
            data.getPresenter().displayResult(data.getPlans().nest(args[0], args[1], currCalendarID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e + " because " + args[0] + " is already related to " + args[1] + " nesting-wise.\n");
        }
    }
    private void unnestPlan(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "parent event/task ID", "child event/task ID");
        try {
            data.getPresenter().displayResult(data.getPlans().unnest(args[0], args[1], currCalendarID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e + " because " + args[0] + " is not a parent of " + args[1] + ".\n");
        }
    }
    private void deletePlan(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "event/task ID to delete");
        try {
            if (data.getLabels().getLabels().containsKey(args[0]))
                data.getLabels().handleDeletePlan(args[0]);
            data.getPresenter().displayResult(data.getPlans().removePlan(args[0], currCalendarID(data)));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void clearTasks(ShellData data) {
        data.getPresenter().displayResult("Are you sure? Type \"clear\" to continue.");
        String args = data.getPresenter().getInput();
        if (args.equals("clear")) {
            data.getPresenter().displayResult(data.getPlans().clearTasks(currCalendarID(data)));
            data.setAllDataSaved(false);
        }
    }
    private void clearEvents(ShellData data) {
        data.getPresenter().displayResult("Are you sure? Type \"clear\" to continue.");
        String args = data.getPresenter().getInput();
        if (args.equals("clear")) {
            data.getPresenter().displayResult(data.getPlans().clearEvents(currCalendarID(data)));
            data.setAllDataSaved(false);
        }
    }
    private void viewEvents(ShellData data) {
        data.getPresenter().displayResult(data.getPlans().viewEvents(currCalendarID(data)));
    }
    private void viewTasks(ShellData data) {
        data.getPresenter().displayResult(data.getPlans().viewTasks(currCalendarID(data)));
    }
}
