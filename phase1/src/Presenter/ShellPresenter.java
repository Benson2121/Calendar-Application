package Presenter;
import java.util.Scanner;
import java.io.*;

/** The presenter/UI class, responsible for printing information.
 * @author Richard Yin
 */
public class ShellPresenter implements Presenter, Serializable {
    /** Prints a new line where the user must type something.
     * @return the user's input
     */
    public String getInput() {
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    public void displayResult(String result) {
        System.out.println(result);
    }
    /** Prompts the user to input additional arguments and returns them
     * @param allowEmpty A boolean or binary string. If a binary string, 0/1 at index i means the ith argument in String
     *                   cannot/can be empty. If a boolean, then the boolean applies to all arguments of the String.
     * @param args The string parameters that the user must give
     * @return A string array of the user's inputs to those arguments
     */
    // https://stackoverflow.com/questions/7243145/java-method-with-unlimited-arguments
    public String[] getArguments(String allowEmpty, String... args) {
        String[] inputs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            System.out.println("Please give an input " + args[i] + ":");
            inputs[i] = getInput();
            if (inputs[i].equals("") && allowEmpty.charAt(i) == '0') {
                while (inputs[i].equals("")) {
                    System.out.println("Please give a non-empty input.");
                    inputs[i] = getInput();
                }
            }
        }
        return inputs;
    }
    public String[] getArguments(boolean allowEmpty, String... args) {
        String[] inputs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            System.out.println("Please give an input " + args[i] + ":");
            inputs[i] = getInput();
            if (inputs[i].equals("") && !allowEmpty) {
                while (inputs[i].equals("")) {
                    System.out.println("Please give a non-empty input.");
                    inputs[i] = getInput();
                }
            }
        }
        return inputs;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* I realized I was supposed to have separate presenter classes too late before the deadline, so we left our
    presenter as such. However, I believe my presenter has been designed so that is simple enough to not need multiple
    classes, since there are not many menus in the first place. */
    public String loggedOutPrompter() {
        System.out.println("CSC207: Phase 0, Group 0253, Main Menu");
        System.out.println("Please enter the numbers corresponding to the following commands:");
        System.out.println("1: Log In");
        System.out.println("2: New User");
        System.out.println("3: Exit");
        return getInput();
    }
    public String userPrompter() {
        System.out.println("Logged in as User. Choose the commands below:");
        System.out.println("1: View User History");
        System.out.println("2: Log Out");
        System.out.println("3: Delete Own Account");
        System.out.println("4: Edit Calendar");
        System.out.println("5: Save");
        return getInput();
    }
    public String adminUserPrompter() {
        System.out.println("Logged in as AdminUser. Choose the commands below:");
        System.out.println("1: View User History      6: Unban User");
        System.out.println("2: Log Out                7: Delete Non-Admin Account");
        System.out.println("3: Delete Own Account     8: Edit Calendar");
        System.out.println("4: New Admin User         9: Save");
        System.out.println("5: Ban User");
        return getInput();
    }
    public String editCalendarPrompter() {
        System.out.println("Calendar Editor. Choose the commands below:");
        System.out.println("1: Back");
        System.out.println("2: View Schedule");
        System.out.println("3: View Schedule by Label");
        System.out.println("4: View Upcoming");
        System.out.println("5: Edit Events/Tasks");
        return getInput();
    }
    public String editPlanPrompter() {
        System.out.println("Event/Task Editor. Choose the commands below:");
        System.out.println("1: Back                9: Edit Time");
        System.out.println("2: Add Event           10: Nest Event/Task");
        System.out.println("3: Add Task            11: Un-nest Event/Task");
        System.out.println("4: Add Subevent        12: Delete Event/Task");
        System.out.println("5: Add Subtask         13: Clear Events");
        System.out.println("6: Edit Name           14: Clear Tasks");
        System.out.println("7: Edit Description    15: View All Events");
        System.out.println("8: Edit Labels         16: View All Tasks");
        return getInput();
    }
    public String editLabelPrompter(boolean eventPrompter) {
        System.out.println("Label Editor. Choose the commands below:");
        System.out.println("1: Back");
        System.out.println("2: View Labels");
        System.out.println("3: Edit Label");
        System.out.println("4: Delete Label");
        System.out.println("5: Clear Labels");
        System.out.println("6: Add Thematic Label");
        if (!eventPrompter)
            System.out.println("7: Add Progress Label");
        return getInput();
    }
}
