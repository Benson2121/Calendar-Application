package Controller;
import Entities.*;
import HelperClasses.*;
import UseCase.ShellData;

/** The part of the Shell responsible for what to do when logged out.
 * @author Richard Yin
 */
public class ShellMenuLoggedOut extends ShellMenu {
    public ShellMenuLoggedOut(ShellData data) {
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> logIn(data));
        commands.put("2", () -> newUser(data));
        commands.put("3", () -> System.exit(0));
    }
    public void run(ShellData data) {
        handleInput(data, data.getPresenter().loggedOutPrompter());
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void logIn(ShellData data) {
        String[] args = data.getPresenter().getArguments("01", "username", "password");
        try {
            data.getPresenter().displayResult(data.getLogging().logIn(data.getUsers().authenticateUser(args[0], args[1])));
            if (data.getLogging().userLoggedIn()) {
                data.getHistory().addHistory(args[0], History.Action.log_in);
                setMenuState(MenuState.USER_MENU);
                if (!data.getCalendar().setCurrCalendarID(args[0])) {
                    data.getCalendar().createCalendar(args[0], "");
                    data.getPlans().handleCreateCalendar(args[0]);
                }
                data.setAllDataSaved(true);
            } else if (data.getLogging().adminUserLoggedIn()) {
                data.getHistory().addHistory(args[0], History.Action.log_in);
                setMenuState(MenuState.ADMIN_USER_MENU);
                if (!data.getCalendar().setCurrCalendarID(args[0])) {
                    data.getCalendar().createCalendar(args[0], "");
                    data.getPlans().handleCreateCalendar(args[0]);
                }
                data.setAllDataSaved(true);
            }
        } catch (NotFoundException e) {
            data.getPresenter().displayResult("Invalid username or password. Please try again. \n");
        } catch (NoPermissionException e) {
            data.getPresenter().displayResult("Cannot log in because username " + args[0] + " is banned. \n");
        }
    }
    private void newUser(ShellData data) {
        String[] args = data.getPresenter().getArguments("01", "username for the new user", "password for the new user");
        try {
            data.getPresenter().displayResult(data.getUsers().createUser(args[0], args[1]));
            // Create blank calendar for User. ID is just username. Will add option to create multiple calendars in phase 2
            data.getCalendar().createCalendar(args[0], "");
            data.getPlans().handleCreateCalendar(args[0]);
            data.saveData();
        } catch (DuplicateUsernameException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
}
