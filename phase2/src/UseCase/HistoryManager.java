package UseCase;
import Entities.History;
import java.time.*;
import java.util.*;
import java.io.*;

/** Use Case class HistoryManager, which stores all instances of History and contains methods to manipulate them
 * @author Richard Yin
 */
public class HistoryManager extends Saveable implements Serializable {
    private Map<LocalDateTime, History> histories;   // A map of time to History

    /** Constructor for HistoryManager.
     */
    public HistoryManager() {
        histories = new LinkedHashMap<>();
    }


    /** addHistory() adds an instance of history to histories.
     * You DO NOT have to specify the time; this function automatically does it.
     * @param username the username of the user who did an action
     * @param action a description of the action (eg. "Login")
     */
    public void addHistory(String username, String action) throws IOException {
        History newHistory = new History(username, action);
        histories.put(newHistory.getTime(), newHistory);
        save();
    }

    /** getHistories() with no arguments returns histories in its entirety
     * @return a printable string, each line representing a History in String form.
     */
    public String getHistories() {
        StringBuilder strHistory = new StringBuilder();
        histories.forEach((k,v) -> strHistory.append(v.toString()).append("\n"));
        // From https://www.geeksforgeeks.org/iterate-map-java/
        return strHistory.toString();
    }

    /** getHistoriesByUser() returns histories filtered by username
     * @param username the user to filter history by
     * @return a printable string, each line representing a History in String form.
     */
    public String getHistoriesByUser(String username) {
        StringBuilder strHistory = new StringBuilder();
        for (Map.Entry<LocalDateTime, History> entry : histories.entrySet()) {
            History v = entry.getValue();
            if (v.getUsername().equals(username))
                strHistory.append(v).append("\n");
        }
        // From https://www.geeksforgeeks.org/iterate-map-java/
        return strHistory.toString();
    }

    /** clearHistoriesByUser() Deletes a user's entire history.
     * @param username the username of the user whose history is to be deleted
     */
    public void clearHistoriesByUser(String username) throws IOException {
        Map<LocalDateTime, History> newHistories = new LinkedHashMap<>();
        for (Map.Entry<LocalDateTime, History> entry : histories.entrySet()) {
            LocalDateTime k = entry.getKey();
            History v = entry.getValue();
            if (!v.getUsername().equals(username))
                newHistories.put(k, v);
        }
        histories = newHistories;
        save();
    }

    /** handleChangeUsername() handles changing a username, retaining the user's history.
     * @param username the user who wants to change their username
     * @param newUsername the user's new username
     */
    public void handleChangeUsername(String username, String newUsername) throws IOException {
        Map<LocalDateTime, History> newHistories = new LinkedHashMap<>();
        for (Map.Entry<LocalDateTime, History> entry : histories.entrySet()) {
            LocalDateTime k = entry.getKey();
            History v = entry.getValue();
            if (v.getUsername().equals(username))
                newHistories.put(k, new History(newUsername, v.getAction()));
            else
                newHistories.put(k, v);
        }
        histories = newHistories;
        save();
    }

    /** Save the current state.
     * @throws IOException error regarding files
     */
    public void save() throws IOException {
        getSaver().save(this, "histories");
    }

    /** Load the current state.
     * @throws IOException error regarding files
     * @throws ClassNotFoundException cannot find class in classpath
     */
    public void load() throws IOException, ClassNotFoundException {
        histories = getSaver().load(this, "histories").histories;
    }

    /** getHistoriesByUserAsList() returns histories in List format.
     * @param username the user to filter history by
     * @return a list of all User's history
     */
    private List<History> getHistoriesByUserAsList(String username) {
        List<History> hists = new ArrayList<>();
        for (History h : histories.values()) {
            if (h.getUsername().equals(username))
                hists.add(h);
        }
        return hists;
    }

    /** getHistoryPropertyAsList() returns history property in List format.
     * @param username the user to filter history by
     * @param property either date/time/action
     * @return a list of all User's history property
     */
    public List<String> getHistoryPropertyAsList(String username, String property) {
        List<History> hists = getHistoriesByUserAsList(username);
        List<String> l = new ArrayList<>();
        for (History h : hists) {
            switch (property) {
                case "date":
                    l.add(h.getTime().toLocalDate().toString());
                    break;
                case "time":
                    l.add(h.getTime().toLocalTime().toString().substring(0, 11));
                    break;
                case "action":
                    l.add(h.getAction());
            }
        }
        return l;
    }
}
