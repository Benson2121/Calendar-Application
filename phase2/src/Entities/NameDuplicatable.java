package Entities;

import java.io.*;

/** An entity class corresponding to something with duplicatable names and thus must have unique IDs.
 * @author Richard Yin
 */
abstract public class NameDuplicatable implements Serializable {

    /** Returns a unique ID to a task/event/calendar that uses IDs. This ID is made up of the first 5 characters of the
     * username and name, plus an extra integer if there are duplicates.
     * @param name The name of the thing that need an ID.
     * @param ids The list of IDs of all objects with the same class as name.
     * @param username The user's username.
     * @return A unique ID.
     */
    public String assignID(String name, Iterable<String> ids, String username) {
        String userPart = username.length() >= 6 ? username.substring(0, 5) : username;
        String namePart = name.length() >= 6 ? name.substring(0, 5) : name;
        int i = 0;
        for (String id : ids) {
            if ((userPart + namePart).equals(id) || id.endsWith(String.valueOf(i)))
                i += 1;
        }
        String numPart = i == 0 ? "" : String.valueOf(i);
        return userPart + namePart + numPart;
    }
}
