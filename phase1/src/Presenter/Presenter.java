package Presenter;

/** Interface for presenter. Created to invert a dependency and allow ShellData to access ShellPresenter.
 * @author Richard Yin
 */
public interface Presenter {
    String getInput();
    void displayResult(String result);
    String[] getArguments(String allowEmpty, String... args);
    String[] getArguments(boolean allowEmpty, String... args);
    String loggedOutPrompter();
    String userPrompter();
    String adminUserPrompter();
    String editCalendarPrompter();
    String editPlanPrompter();
    String editLabelPrompter(boolean eventPrompter);
}
