package Controller;
import java.util.Scanner;

/** The presenter/UI class, responsible for printing information.
 * @author Richard Yin
 */
public class ShellPresenter {
    public void displayResult(String result) {
        System.out.println(result);
    }

    /** Prints the main menu (before logging on or after logging out) and receives an input
     * @return the user's input
     */
    public String loggedOutPrompter() {
        System.out.println("CSC207: Phase 0, Group 0253");
        System.out.println("Please enter the numbers corresponding to the following commands:");
        System.out.println("1: Log In");
        System.out.println("2: New User");
        System.out.println("3: Exit");
        return getInput();
    }

    /** Prints a regular user's menu after logging in and receives an input
     * @return the user's input
     */
    public String userPrompter() {
        System.out.println("Please enter the numbers corresponding to the following commands:");
        System.out.println("1: View User History");
        System.out.println("2: Log Out");
        System.out.println("3: Delete Own Account");
        return getInput();
    }

    /** Prints an admin user's menu after logging in and recieves an input
     * @return the user's input
     */
    public String adminUserPrompter() {
        System.out.println("Please enter the numbers corresponding to the following commands:");
        System.out.println("1: View User History");
        System.out.println("2: Log Out");
        System.out.println("3: Delete Own Account");
        System.out.println("4: New Admin User");
        System.out.println("5: Ban User");
        System.out.println("6: Unban User");
        System.out.println("7: Delete Non-Admin Account");
        return getInput();
    }

    /** Prompts the user to input additional arguments and returns them
     * @param args a string array of arguments that the user must give
     * @return a string array of the user's inputs to those arguments
     */
    public String[] getArguments(String[] args) {
        String[] inputs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            System.out.println("Please give an input " + args[i] + ":");
            inputs[i] = getInput();
        }
        return inputs;
    }

    /** Prints a new line where the user must type something.
     * @return the user's input
     */
    public String getInput() {
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
