package Shell.Presenter;
import java.util.*;
import java.io.*;

/** The presenter/UI class, responsible for printing information.
 * @author Richard Yin
 */
public class ShellPresenter implements Serializable {

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
    /** Prompt the user to input additional arguments and returns them.
     * @param allowEmpty A boolean or binary string.
     *                   If a binary string, 0/1 at index i means the ith argument in inputs cannot/can be empty.
     *                   If a boolean, then that boolean will apply to all arguments in inputs.
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

    /** Prompt the user to input additional arguments or non-empty arguments.
     * @param allowEmpty boolean if given input was empty
     * @param args The string parameters that the user must give
     * @return A string array of the user's inputs to those arguments
     */
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

    /** Prompt the user to input additional arguments and returns them.
     * @param allowed A list of strings. The method will only accept inputs inside the list.
     * @param args The string parameters that the user must give
     * @return A string array of the user's inputs to those arguments
     */
    public String[] getArguments(List<String> allowed, String... args) {
        String[] inputs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            System.out.println("Please give an input " + args[i] + " (allowed:" + allowed + "):");
            inputs[i] = getInput();
            if (!allowed.contains(inputs[i])) {
                while (!allowed.contains(inputs[i])) {
                    System.out.println("Invalid input (allowed: " + allowed + "). Please try again.");
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

    /**
     * Method that displays menu text to users in the interface and asks for an input.
     * If they're <= 5 commands, they will be printed out in one column.
     * If they're > 5 commands, they will be printed out in two columns.
     * @param firstLine The first string to print out. Describes what menu the user is at.
     * @param args The strings of each command to print out.
     * @return The user's input
     */
    public String prompter(String firstLine, String... args) {
        System.out.println(firstLine);
        if (args.length <= 5) {
            int i = 1;
            for (String arg: args) {
                System.out.println(i + ": " + arg);
                i += 1;
            }
        } else {
            int halfwayPoint = (args.length + args.length % 2) / 2;
            // From https://stackoverflow.com/questions/26725296/finding-the-longest-string-in-an-array-of-strings but modified
            int maxLeftColWidth = List.of(args).subList(0, halfwayPoint).stream().map(String::length).max(Integer::compareTo).get();
            for (int i = 0; i < halfwayPoint; i++) {
                String leftCol = i + 1 + ": " + args[i] + " ".repeat(maxLeftColWidth + 3 - args[i].length());
                String rightCol = halfwayPoint + i < args.length ? halfwayPoint + i + 1 + ": " + args[halfwayPoint + i] : "";
                System.out.println(leftCol + rightCol);
            }
        }
        return getInput();
    }
}
