package Shell.Controller;
import Gateway.*;
import Shell.Presenter.ShellPresenter;
import UseCase.*;
import java.io.IOException;
import java.util.*;

/** The Shell Controller class, responsible for connecting all menu classes together.
 * @author Richard Yin
 */
public class ShellController {
    private final CalendarManager cals = new CalendarManager();
    private final PlanManager plans = new PlanManager();
    private final LabelManager labs = new LabelManager();
    private final CommentManager comms = new CommentManager();
    private final HistoryManager hists = new HistoryManager();
    private final UserManager users = new UserManager();
    private final LoggingManager logs = new LoggingManager();
    private final InterfaceSaver s = new Saver();
    private final ShellPresenter p = new ShellPresenter();
    private final Map<MenuState, ShellMenu> menus;

    /**
     * Initializes a copy of all manager classes and maps menustates to shell menu objects
     */
    public ShellController() {
        cals.setSaver(s);
        plans.setSaver(s);
        labs.setSaver(s);
        hists.setSaver(s);
        users.setSaver(s);
        comms.setSaver(s);

        menus = new HashMap<>();
        menus.put(MenuState.LOG_OUT, new ShellMenuLoggedOut(p, users, logs, hists));
        menus.put(MenuState.USER_MENU, new ShellMenuUser(p, s, plans, cals, logs, labs, hists, users, comms));
        menus.put(MenuState.ADMIN_USER_MENU, new ShellMenuUserAdmin(p, s, plans, cals, logs, labs, users, hists, comms));
        menus.put(MenuState.EDIT_CALENDAR, new ShellMenuEditCalendar(p, plans, cals, users, logs, labs, hists, comms));
        menus.put(MenuState.EDIT_PLAN, new ShellMenuEditPlan(p, cals, plans, labs, comms));
        menus.put(MenuState.EDIT_LABEL_EVENT, new ShellMenuEditLabelEvent(p, s, cals, plans, labs, logs, hists));
        menus.put(MenuState.EDIT_LABEL_TASK, new ShellMenuEditLabelTask(p, s, cals, plans, labs, logs, hists));
        menus.put(MenuState.EDIT_COMMENT, new ShellMenuEditComment(p, plans, comms, logs));

        /* The idea is to map a menu-state to a menu class which can be called with run(). I did this because:
        - It follows the Open-Closed Principle. You only have to add an extra key-value pair for a new menu, and the
          run() method will never have to be changed.
        - It prevents coupling of menu classes, for example if you want to go back and forth between menus and the menu
          classes have to call each other's methods. Instead, this class calls every menu separately
        - We are not going to have so many menus to the point where loading and memory becomes an issue in this shell

        To deal with knowledge that a menu must know (eg. the edit label menu knowing what plan it's editing),
        this information is stored in Use Case classes in the form of variables like currentPlan.

        For phase 2, we got rid of ShellData as per suggestions from phase 1 feedback, but as a result, there are
        data clumps and many-argument methods.
        */
        load();
    }

    /**
     * Load the data if it exists, otherwise create a new admin account.
     */
    private void load() {
        try {
            cals.load();
            plans.load();
            labs.load();
            hists.load();
            users.load();
            comms.load();
            p.displayResult("Successfully loaded pre-existing database.\n");
        } catch (IOException | ClassNotFoundException | NullPointerException e1) {
            p.displayResult("Either no database found or there was an error. Initialized new unsaved database.");
            String[] args = p.getArguments("01", "admin account username before starting", "a password for the admin account");
            try {
                users.createAdminUser(args[0], args[1]);
                p.displayResult("Successfully created new admin user " + args[0]);
                hists.addHistory(args[0], "Created user " + args[0]);
                ((ShellMenuUser) menus.get(MenuState.USER_MENU)).save(p, cals, plans, labs, users, hists, comms);
            } catch (Exception e2) {
                p.displayResult(e2.toString());
            }
        }
    }

    /** Execute one run of a MenuState. */
    public void run() {
        menus.get(menus.get(MenuState.LOG_OUT).getMenuState()).run(p);
    }
}
