//package UseCase;
//import Gateway.*;
//import Presenter.ShellPresenter;
//import Presenter.Presenter;
//import java.io.*;
//
///** The ShellData class, a use case class containing all Shell data in the form of the manager classes.
// * Primary purpose of existence is to avoid data clumps & too much argument parameters in the shell menu classes.
// * Another purpose of existence is to allow saving just one object, rather than all manager classes.
// *
// * @author Richard Yin
// */
//public class ShellData implements Serializable {
//    private boolean allDataSaved = true;
//    private UserManager users;
//    private HistoryManager history;
//    private LoggingManager logging;
//    private CalendarManager calendar;
//    private PlanManager plans;
//    private LabelManager labels;
//    private final Presenter presenter = new ShellPresenter();
//    private final Saver saver = new ShellSaver();
//
//    public boolean getAllDataSaved() {
//        return allDataSaved;
//    }
//    public void setAllDataSaved(boolean allDataSaved) {
//        this.allDataSaved = allDataSaved;
//    }
//    public UserManager getUsers() {
//        return users;
//    }
//    public HistoryManager getHistory() {
//        return history;
//    }
//    public LoggingManager getLogging() {
//        return logging;
//    }
//    public Presenter getPresenter() {
//        return presenter;
//    }
//    public CalendarManager getCalendar() {
//        return calendar;
//    }
//    public LabelManager getLabels() {
//        return labels;
//    }
//    public PlanManager getPlans() {
//        return plans;
//    }
//    public String saveData() {
//        try {
//            saver.save(this);
//            return "Successfully saved data.";
//        } catch (IOException e) {
//            return "Something wrong happened and the data couldn't be saved.";
//        }
//    }
//    public boolean fetchData() {
//        try {
//            /* I know the phase 1 sheet said to try not to cast outside the gateway, but I would like to explain why:
//
//            - If the cast occurs inside the ShellSaver, ShellSaver and Saver will depend on ShellData, so I cannot call
//              them from here, meaning I'd have to move this method somewhere else:
//               - ShellController - I would have to add setters for all variables here that'd only be
//                 used for one class, and I feel ShellController's main purpose is coordinating menus and not
//                 saving/loading data, so this would violate the Single Responsibility Principle.
//
//               - ShellSaver - I don't want to do this because fetchData() and saveData() serve as what I call "command"
//                 methods, ie. use case methods that are meant to be called by menu classes and return a String, which is
//                 displayed by the Presenter. If I move command methods to the Gateway, I'm essentially treating the
//                 Gateway as a Use Case Manager class, which doesn't feel right, especially since I'd only be able to
//                 access the Gateway from a separate pathway than the other Manager classes (ie. from this class)
//
//             - I need a method other than a getter/setter here, otherwise this class is a data class. I would love to
//               not need this class, but I created it in the first place because the code smells site told me to extract
//               method parameters into a class to stop the data clump code smell. And I am not sure what methods to add
//               in this class, if not these methods.
//             */
//            ShellData loaded = (ShellData) saver.load();
//            users = loaded.getUsers();
//            history = loaded.getHistory();
//            logging = loaded.getLogging();
//            calendar = loaded.getCalendar();
//            plans = loaded.getPlans();
//            labels = loaded.getLabels();
//            return true;
//        } catch (IOException | ClassNotFoundException e) {
//            users = new UserManager();
//            history = new HistoryManager();
//            logging = new LoggingManager();
//            calendar = new CalendarManager();
//            plans = new PlanManager();
//            labels = new LabelManager();
//            return false;
//        }
//    }
//}
