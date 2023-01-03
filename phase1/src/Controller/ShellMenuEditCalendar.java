package Controller;
import UseCase.ShellData;

import java.util.*;

/** The part of the Shell responsible for what to do when in calendar edit mode.
 * @author Richard Yin
 */
public class ShellMenuEditCalendar extends ShellMenu {
    public ShellMenuEditCalendar(ShellData data) {
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> back(data));
        commands.put("2", () -> viewSchedule(data));
        commands.put("3", () -> viewScheduleByLabel(data));
        commands.put("4", () -> viewUpcoming(data));
        commands.put("5", this::editPlans);
    }
    public void run(ShellData data) {
        handleInput(data, data.getPresenter().editCalendarPrompter());
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void back(ShellData data) {
        if (data.getLogging().adminUserLoggedIn())
            setMenuState(MenuState.ADMIN_USER_MENU);
        else
            setMenuState(MenuState.USER_MENU);
    }
    private void viewSchedule(ShellData data){
        if (data.getPlans().getAllPlans(data.getCalendar().getCurrCalendarID()).isEmpty())
            data.getPresenter().displayResult("Nothing in schedule yet. \n");
        else
            data.getPresenter().displayResult(data.getPlans().viewSchedule(data.getPlans().getAllPlans(data.getCalendar().getCurrCalendarID())));
    }
    private void viewScheduleByLabel(ShellData data) {
        if (data.getPlans().getAllPlans(data.getCalendar().getCurrCalendarID()).isEmpty()) {
            data.getPresenter().displayResult("Nothing in schedule yet. \n");
            return;
        }
        List<String> args = new ArrayList<>();
        String label = data.getPresenter().getArguments(false, "label to filter for")[0];
        while (!label.equals("")) {
            args.add(label);
            label = data.getPresenter().getArguments(true,  "another label to filter for (just enter nothing to stop adding labels)")[0];
        }

        String result = data.getPlans().viewScheduleByLabel(data.getCalendar().getCurrCalendarID(), data.getLabels().getLabels(), args);
        if (result.length() == 0)
            data.getPresenter().displayResult("No events/tasks found with specified labels.");
        else
            data.getPresenter().displayResult(result);
    }
    private void viewUpcoming(ShellData data) {
        if (data.getPlans().getAllPlans(data.getCalendar().getCurrCalendarID()).isEmpty()) {
            data.getPresenter().displayResult("Nothing in schedule yet. \n");
            return;
        }
        String result = data.getPlans().viewUpcoming(data.getPlans().getAllPlans(data.getCalendar().getCurrCalendarID()));
        if (result.length() == 0)
            data.getPresenter().displayResult("No upcoming events/tasks.");
        else
            data.getPresenter().displayResult(result);
    }
    private void editPlans() {
        setMenuState(MenuState.EDIT_PLAN);
    }
}
