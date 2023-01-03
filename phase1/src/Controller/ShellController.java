package Controller;
import HelperClasses.DuplicateUsernameException;
import UseCase.ShellData;
import java.util.*;

/** The Shell Controller class, responsible for connecting all menu classes together.
 * @author Richard Yin
 */
public class ShellController {
    private ShellData data;
    private Map<MenuState, ShellMenu> menus;

    public ShellController() {
        data = new ShellData();
        load(data);

        menus = new HashMap<>();
        /* The idea is to map a menu-state to a menu class which can be called with run(). I did this because:
        - It follows the Open-Closed Principle. You only have to add an extra key-value pair for a new menu, and the
          run() method will never have to be changed.
        - It prevents coupling of menu classes, for example if you want to go back and forth between menus and the menu
          classes have to call each other's methods. Instead, this class calls every menu separately
        - We are not going to have so many menus to the point where loading and memory becomes an issue in this shell

        In order to deal with knowledge that a menu must know (eg. the edit label menu knowing what plan it's editing),
        this information is stored in Use Case classes in the form of variables like currentPlan. */
        menus.put(MenuState.LOG_OUT, new ShellMenuLoggedOut(data));
        menus.put(MenuState.USER_MENU, new ShellMenuUser(data));
        menus.put(MenuState.ADMIN_USER_MENU, new ShellMenuAdminUser(data));
        menus.put(MenuState.EDIT_CALENDAR, new ShellMenuEditCalendar(data));
        menus.put(MenuState.EDIT_PLAN, new ShellMenuEditPlan(data));
        menus.put(MenuState.EDIT_LABEL, new ShellMenuEditLabel(data));
    }

    /**
     * Load the data if it exists, otherwise create a new admin account.
     * @param data The shell's data.
     */
    private void load(ShellData data) {
        if (data.fetchData())
            data.getPresenter().displayResult("Successfully loaded pre-existing database.");
        else {
            try {
                data.getPresenter().displayResult("Either no database found or there was an error. Initialized new unsaved database.");
                String[] args = data.getPresenter().getArguments("01", "admin account username before starting", "a password for the admin account");
                data.getPresenter().displayResult(data.getUsers().createAdminUser(args[0], args[1]));
                data.saveData();
            } catch (DuplicateUsernameException e) {
                data.getPresenter().displayResult("Something went wrong!");
            }
        }
    }

    /**
     * Run through one menustate.
     */
    public void run() {
        menus.get(menus.get(MenuState.LOG_OUT).getMenuState()).run(data);
    }
}
