package Gateway;
import java.io.*;

/** A gateway shell class, responsible for saving information.
 * @author Richard Yin
 */
public class ShellSaver implements Saver, Serializable {
    private final String fileName = "phase1/src/CSC207Data.ser";

    /**
     * Saves a serializable object to a file with name filename.
     * @param saveable A serializable object.
     */
    public void save(Serializable saveable) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(saveable);
        oos.close();
    }

    /**
     * Loads a serializable object from a file with name filename.
     * @return The serialized object
     */
    public Object load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        return ois.readObject();
    }
}
