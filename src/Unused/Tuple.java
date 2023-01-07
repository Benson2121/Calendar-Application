package Unused;

import java.util.List;

/**
 * This is a class that mimics a tuple in python
 * @author Richard Yin
 */public class Tuple {
    private final List<String> arguments;

    /** Constructor for the tuple takes in any list of strings
     */
    public Tuple(List<String> arguments) {
        this.arguments = arguments;
    }

    /** Return the ith object of the tuple
     */
    public String get(int i) {
        return arguments.get(i);
    }
}
