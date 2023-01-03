package Controller;
import HelperClasses.*;
import UseCase.ShellData;

/** The part of the Shell responsible for what to do when logged in as an admin user.
 * @author Richard Yin
 */
public class ShellMenuAdminUser extends ShellMenuUser {
    public ShellMenuAdminUser(ShellData data) {
        super(data);
        // From https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
        commands.put("1", () -> viewHistory(data));
        commands.put("2", () -> logOut(data));
        commands.put("3", () -> deleteOwnAccount(data));
        commands.put("4", () -> newAdminUser(data));
        commands.put("5", () -> banUser(data));
        commands.put("6", () -> unbanUser(data));
        commands.put("7", () -> deleteNonAdminAccount(data));
        commands.put("8", () -> editCalendar(data));
        commands.put("9", () -> save(data));
    }
    @Override
    public void run(ShellData data) {
        handleInput(data, data.getPresenter().adminUserPrompter());
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void newAdminUser(ShellData data) {
        String[] args = data.getPresenter().getArguments("01", "username for the new admin user",
                "password for the new admin user");
        try {
            data.getPresenter().displayResult(data.getUsers().createAdminUser(args[0], args[1]));
            data.getCalendar().createCalendar(args[0], "");
            data.getPlans().handleCreateCalendar(args[0]);
            data.saveData();
        } catch (DuplicateUsernameException e) {
            data.getPresenter().displayResult(e.toString());
        }
    }
    private void banUser(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "username of the user to ban");
        try {
            data.getPresenter().displayResult(data.getUsers().banUser(args[0]));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e + ": the target username is an admin \n");
        }
    }
    private void unbanUser(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "username of the user to unban");
        try {
            data.getPresenter().displayResult(data.getUsers().unbanUser(args[0]));
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e + ": the target username is an admin \n");
        }
    }
    private void deleteNonAdminAccount(ShellData data) {
        String[] args = data.getPresenter().getArguments(false, "username of the account to delete");
        try {
            data.getPresenter().displayResult(data.getUsers().deleteUser(args[0], false));
            data.getHistory().clearHistoriesByUser(args[0]);
            // CalendarID is currently set to the name of a user so far, it will be expanded upon properly in phase 2
            String currCalendarID = args[0];
            // Delete all user data
            if (currCalendarID != null) {
                data.getCalendar().deleteCalendar(currCalendarID);
                data.getPlans().handleClearCalendar(currCalendarID);
                data.getLabels().handleClearUserPlans(data.getPlans().getAllPlanIDs(currCalendarID));
                data.getCalendar().setCurrCalendarID(null);
            }
            data.setAllDataSaved(false);
        } catch (NotFoundException e) {
            data.getPresenter().displayResult(e.toString());
        } catch (InvalidCommandException e) {
            data.getPresenter().displayResult(e + ": the target username is an admin \n");
        }
    }
}
