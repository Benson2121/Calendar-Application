package Gateway;
import java.io.*;

/** Interface for gateway saver. Created to invert a dependency and allow ShellData to access ShellSaver.
 * @author Richard Yin
 */
public interface Saver {
    void save(Serializable saveable) throws IOException;
    Object load() throws IOException, ClassNotFoundException;
}
