package Shell.Controller;
import HelperClasses.*;
import Shell.Presenter.ShellPresenter;
import UseCase.*;

/** An abstract class containing common commands between the event/task edit menus.
 * @author Richard Yin
 */
public class ShellMenuEditPlan extends ShellMenu {

    /** Constructor for ShellMenuEditPlan.
     * @param p the presenter
     * @param plans the plan manager
     * @param cals the calendar manager
     * @param labs the label manager
     * @param comms the comment manager
     */
    public ShellMenuEditPlan(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, CommentManager comms) {
        commands.put("1", this::back);
        commands.put("2", () -> addEvent(p, cals, plans, labs, comms));
        commands.put("3", () -> addTask(p, cals, plans, labs, comms));
        commands.put("4", () -> addSubevent(p, cals, plans, labs, comms));
        commands.put("5", () -> addSubtask(p, cals, plans, labs, comms));
        commands.put("6", () -> editName(p, cals, plans, labs, comms));
        commands.put("7", () -> editDescription(p, cals, plans));
        commands.put("8", () -> editLabels(p, cals, plans));
        commands.put("9", () -> editTime(p, cals, plans));
        commands.put("10", () -> nestPlan(p, cals, plans));
        commands.put("11", () -> unnestPlan(p, cals, plans));
        commands.put("12", () -> deletePlan(p, cals, plans, labs, comms));
        commands.put("13", () -> clearEvents(p, cals, plans));
        commands.put("14", () -> clearTasks(p, cals, plans));
        commands.put("15", () -> viewEvents(p, cals, plans));
        commands.put("16", () -> viewTasks(p, cals, plans));
        commands.put("17", () -> editComments(p, cals, plans));
    }

    /** Run the Plan edit mode Presenter.
     */
    @Override
    public void run(ShellPresenter p) {
        handleInput(p, p.prompter("Event/Task Editor. Choose the commands below:",
                "Back", "Add Event", "Add Task", "Add Subevent",
                "Add Subtask", "Edit Name", "Edit Description", "Edit Labels",
                "Edit Time", "Nest Event/Task", "Un-nest Event/Task", "Delete Event/Task",
                "Clear Events", "Clear Tasks", "View All Events", "View All Tasks", "Edit Comments"));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Go back to the edit calendar menu. 
     */
    private void back() {
        setMenuState(MenuState.EDIT_CALENDAR);
    }

    /**
     * Create a new event.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param comms The comment manager
     */
    private void addEvent(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, CommentManager comms) {
        String[] args = p.getArguments("01", "name for the event", "description for the event");
        String result = plans.createEvent(args[0], args[1], cals.getCurrCalendarID());
        p.displayResult("Successfully created event with ID: " + result + "\n");
        labs.handleNewPlan(result, true);
        comms.handleNewPlan(result);
    }

    /**
     * Create a new task.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param comms The comment manager
     */
    private void addTask(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, CommentManager comms) {
        String[] args = p.getArguments("01", "name for the task", "description for the task");
        String result = plans.createTask(args[0], args[1], cals.getCurrCalendarID());
        p.displayResult("Successfully created task with ID: " + result + "\n");
        labs.handleNewPlan(result, false);
        comms.handleNewPlan(result);
    }

    /**
     * Create a new subevent.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param comms The comment manager
     */
    private void addSubevent(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, CommentManager comms) {
        String[] args = p.getArguments("001", "parent event ID", "name for the subevent", "description for the subevent");
        try {
            String result = plans.createEvent(args[1], args[2], cals.getCurrCalendarID());
            p.displayResult("Successfully created Subevent with ID: " + result);
            labs.handleNewPlan(result, true);
            comms.handleNewPlan(result);
            plans.nest(args[0], result, cals.getCurrCalendarID());
            p.displayResult("Successfully nested new subevent " + result + " in " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            p.displayResult(e + " because " + args[0] + " is not a parent event of " + args[1] + "\n");
        }
    }

    /**
     * Create a new subtask.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param comms The comment manager
     */
    private void addSubtask(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, CommentManager comms) {
        String[] args = p.getArguments("001", "parent task ID", "name for the subtask", "description for the subtask");
        try {
            String result = plans.createTask(args[1], args[2], cals.getCurrCalendarID());
            p.displayResult("Successfully created Subtask with ID: " + result);
            labs.handleNewPlan(result, false);
            comms.handleNewPlan(result);
            plans.nest(args[0], result, cals.getCurrCalendarID());
            p.displayResult("Successfully nested new subtask " + result + " in " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            p.displayResult(e + " because " + args[0] + " is not a parent task of " + args[1] + "\n");
        }
    }

    /**
     * Create a new subevent.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param comms The comment manager
     */
    private void editName(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, CommentManager comms) {
        String[] args = p.getArguments(false, "event/task ID", "name to change to");
        try {
            String result = plans.changePlanName(args[1], args[0], cals.getCurrCalendarID());
            p.displayResult("Successfully changed plan name.\n");
            labs.handleSetPlanName(args[0], result);
            comms.handleSetPlanName(args[0], result);
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Edit the description of a plan
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void editDescription(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        String[] args = p.getArguments(false, "event/task ID", "description to change to");
        try {
            plans.changePlanDescription(args[1], args[0], cals.getCurrCalendarID());
            p.displayResult("Successfully changed description of " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Go to the label edit menu
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void editLabels(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        String[] args = p.getArguments(false, "event/task ID");
        try {
            plans.setCurrPlanID(args[0], cals.getCurrCalendarID());
            if (plans.isEvent(plans.getCurrPlanID(), cals.getCurrCalendarID()))
                setMenuState(MenuState.EDIT_LABEL_EVENT);
            else
                setMenuState(MenuState.EDIT_LABEL_TASK);
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Edit the start/end time of a event or a deadline of a task.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void editTime(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        String planID = p.getArguments(false, "event/task ID")[0];
        try {
            if (plans.isEvent(planID, cals.getCurrCalendarID())) {
                String[] args = p.getArguments(false, "event start time (YYYY/MM/DD HH:MM, time/year is omittable)", "event end time (YYYY/MM/DD HH:MM, time/year is omittable)");
                plans.setTime(planID, args[0], args[1], cals.getCurrCalendarID());
                p.displayResult("Successfully changed start/end times of event ID" + planID + "\n");
            } else {
                String[] args = p.getArguments(false, "task deadline time (YYYY/MM/DD HH:MM, time/year is omittable)");
                plans.setDeadline(planID, args[0], cals.getCurrCalendarID());
                p.displayResult("Successfully changed deadline of task ID" + planID + "\n");
            }
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Nest one plan under another.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void nestPlan(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        String[] args = p.getArguments(false, "parent event/task ID", "child event/task ID");
        try {
            plans.nest(args[0], args[1], cals.getCurrCalendarID());
            p.displayResult("Successfully nested " + args[1] + " in " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            p.displayResult(e + " because " + args[0] + " is already related to " + args[1] + " nesting-wise.\n");
        }
    }

    /**
     * Unnest one plan from another.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void unnestPlan(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        String[] args = p.getArguments(false, "parent event/task ID", "child event/task ID");
        try {
            plans.unnest(args[0], args[1], cals.getCurrCalendarID());
            p.displayResult("Successfully unnested " + args[1] + " from " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        } catch (InvalidCommandException e) {
            p.displayResult(e + " because " + args[0] + " is not a parent of " + args[1] + "\n");
        }
    }

    /**
     * Delete a plan.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     * @param labs The label manager
     * @param comms The comment manager
     */
    private void deletePlan(ShellPresenter p, CalendarManager cals, PlanManager plans, LabelManager labs, CommentManager comms) {
        String[] args = p.getArguments(false, "event/task ID to delete");
        try {
            labs.handleDeletePlan(args[0]);
            comms.handleDeletePlan(args[0]);
            plans.removePlan(args[0], cals.getCurrCalendarID());
            p.displayResult("Successfully deleted event/task ID " + args[0] + "\n");
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * Clear all tasks.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void clearTasks(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        p.displayResult("Are you sure? Type \"clear\" to continue.");
        String args = p.getInput();
        if (args.equals("clear")) {
            plans.clearTasks(cals.getCurrCalendarID());
            p.displayResult("Successfully cleared all tasks. \n");
        }
    }

    /**
     * Clear all events.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void clearEvents(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        p.displayResult("Are you sure? Type \"clear\" to continue.");
        String args = p.getInput();
        if (args.equals("clear")) {
            plans.clearEvents(cals.getCurrCalendarID());
            p.displayResult("Successfully cleared all events. \n");
        }
    }

    /**
     * View all events.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void viewEvents(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        try {
            String result = plans.viewAllEvents(cals.getCurrCalendarID());
            if (result.length() == 0)
                p.displayResult("No events have been scheduled yet.\n");
            else
                p.displayResult(result);
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * View all tasks.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void viewTasks(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        try {
            String result = plans.viewAllTasks(cals.getCurrCalendarID());
            if (result.length() == 0)
                p.displayResult("No tasks have been scheduled yet.\n");
            else
                p.displayResult(result);
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }

    /**
     * View all comments.
     * @param p The presenter
     * @param cals The calendar manager
     * @param plans The plan manager
     */
    private void editComments(ShellPresenter p, CalendarManager cals, PlanManager plans) {
        String[] args = p.getArguments(false, "event/task ID");
        try {
            plans.setCurrPlanID(args[0], cals.getCurrCalendarID());
            if (plans.isEvent(plans.getCurrPlanID(), cals.getCurrCalendarID()))
                setMenuState(MenuState.EDIT_COMMENT);
            else
                setMenuState(MenuState.EDIT_COMMENT);
        } catch (NotFoundException e) {
            p.displayResult(e.toString());
        }
    }
}
