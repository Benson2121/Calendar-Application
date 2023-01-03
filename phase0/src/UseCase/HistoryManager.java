package UseCase;
import Entities.History;
import java.time.*;
import java.util.*;

/** Use Case class HistoryManager, which stores all instances of History and contains methods to manipulate them
 * @author Richard Yin
 */
public class HistoryManager {
    private Map<LocalDateTime, History> histories;   // A map of username to History

    public HistoryManager() {
        histories = new LinkedHashMap<>();
    }

//    public void clearHistories() {
//        histories.clear();
//    }

    /** addHistory() adds an instance of history to histories.
     * You DO NOT have to specify the time; this function automatically does it.
     *
     * @param username the username of the user who did an action
     * @param action a description of the action (eg. "Login")
     */
    public void addHistory(String username, String action) {
        History newHistory = new History(username, action);
        histories.put(LocalDateTime.now(), newHistory);
    }

//    /** getHistories() with no arguments returns histories in its entirety
//     * @return a printable string, each line representing a History in String form.
//     */
//    public String getHistories() {
//        StringBuilder strHistory = new StringBuilder();
//        histories.forEach((k,v) -> strHistory.append(k.toString()).append(" | ").append(v.toString()).append("\n"));
//        // From https://www.geeksforgeeks.org/iterate-map-java/
//
//        return strHistory.toString();
//    }

    /** getHistoriesByUser() returns histories filtered by username
     * @param username the user to filter history to
     * @return a printable string, each line representing a History in String form.
     */
    public String getHistoriesByUser(String username) {
        StringBuilder strHistory = new StringBuilder();
        for (Map.Entry<LocalDateTime, History> entry : histories.entrySet()) {
            LocalDateTime k = entry.getKey();
            History v = entry.getValue();
            if (v.getUsername().equals(username))
                strHistory.append(k.toString()).append(" | ").append(v).append("\n");
        }
        // From https://www.geeksforgeeks.org/iterate-map-java/
        return strHistory.toString();
    }

    /** Deletes a user's entire history.
     * @param username the username of the user whose history is to be deleted
     */
    public void clearHistoriesByUser(String username) {
        Map<LocalDateTime, History> newHistories = new LinkedHashMap<>();
        for (Map.Entry<LocalDateTime, History> entry : histories.entrySet()) {
            LocalDateTime k = entry.getKey();
            History v = entry.getValue();
            if (!v.getUsername().equals(username))
                newHistories.put(k, v);
        }
        histories = newHistories;
    }
}
