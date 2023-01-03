package Controller;
import Entities.History;
import HelperClasses.*;
import UseCase.ShellData;

/** The part of the Shell responsible for what to do when logged in as a regular user.
 * @author Richard Yin
 */
public class ShellMenuUser extends ShellMenu {
    /** Shell prompter loop when logged in as a regular user.
     */
    public ShellMenuUser(ShellData data) {
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> viewHistory(data));
        commands.put("2", () -> logOut(data));
        commands.put("3", () -> deleteOwnAccount(data));
        commands.put("4", () -> editCalendar(data));
        commands.put("5", () -> save(data));
    }
    public void run(ShellData data) {
        handleInput(data, data.getPresenter().userPrompter());
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void viewHistory(ShellData data) {
        data.getPresenter().displayResult(data.getHistory().getHistoriesByUser(data.getLogging().getCurrUsername()));
    }
    protected void logOut(ShellData data) {
        if (!data.getAllDataSaved()) {
            data.getPresenter().displayResult("Changes have been made. Type \"yes\" to log out without saving.");
            if (!data.getPresenter().getInput().equals("yes"))
                return;
        }
        data.getHistory().addHistory(data.getLogging().getCurrUsername(), History.Action.log_out);
        data.getPresenter().displayResult(data.getLogging().logOut());
        setMenuState(MenuState.LOG_OUT);

    }
    protected void deleteOwnAccount(ShellData data) {
        try {
            data.getHistory().clearHistoriesByUser(data.getLogging().getCurrUsername());
            // Delete account data
            String currCalendarID = data.getCalendar().getCurrCalendarID();
            if (currCalendarID != null) {
                data.getCalendar().deleteCalendar(currCalendarID);
                data.getPlans().handleClearCalendar(currCalendarID);
                data.getLabels().handleClearUserPlans(data.getPlans().getAllPlanIDs(currCalendarID));
                data.getCalendar().setCurrCalendarID(null);
            }
            data.getPresenter().displayResult(data.getUsers().deleteUser(data.getLogging().getCurrUsername(), true));
            data.getLogging().logOut();
            setMenuState(MenuState.LOG_OUT);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    protected void editCalendar(ShellData data) {
        // Set current calendar ID to the user's name - we're implementing multiple calendars in phase 2
        data.getCalendar().setCurrCalendarID(data.getLogging().getCurrUsername());
        setMenuState(MenuState.EDIT_CALENDAR);
    }
    protected void save(ShellData data) {
        data.getPresenter().displayResult(data.saveData());
        data.setAllDataSaved(true);
    }
}
