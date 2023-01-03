package Entities;
import java.time.*;
import java.io.*;

/** An entity class representing a single instance of a recorded action.
 * @author Richard Yin
 */
public class History implements Serializable {
    public enum Action {
        log_out, log_in
    }
    private final Action action;       // what is the action
    private final String username;     // who did the action
    private final LocalDateTime time;  // when did the action happen

    /** Constructor
     * @param action a description of the action performed
     * @param username the username of the user who performed the action.
     */
    public History(String username, Action action) {
        this.username = username;
        this.action = action;
        this.time = LocalDateTime.now();
    }
    public Action getAction() {
        return action;
    }
    public String getUsername() {
        return username;
    }
    public LocalDateTime getTime() {
        return time;
    }
    @Override
    public String toString() {
        return time + " | " + username + " | " + action.name().replace('_', ' ');
    }
}
