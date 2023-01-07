package Gateway;
import java.io.*;

/** A gateway shell class, responsible for saving information.
 * @author Richard Yin
 */
public class Saver implements InterfaceSaver {
    private boolean changesMade = false;
    private final String path = "phase2/src/";

    /** Saves a serializable object to a file with name filename.
     * @param saveable A serializable object.
     */
    public void save(Serializable saveable, String fileName) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "CSC207_" + fileName + ".ser"));
        oos.writeObject(saveable);
        oos.close();
    }

    /** Note all changes have been accounted for.
     */
    public void saveAll() {
        changesMade = false;
    }

    /** Return if there are current changes pending.
     */
    public boolean changesMade() {
        return changesMade;
    }

    /** Note a change has been made.
     */
    public void makeChange() {
        changesMade = true;
    }

    /**
     * Loads a serializable object from a file with name filename.
     * @return The serialized object
     */
    public <T> T load(T item, String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path + "CSC207_" + fileName + ".ser"));
        return ((T) ois.readObject());
    }
}
