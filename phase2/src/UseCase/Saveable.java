package UseCase;
import Gateway.InterfaceSaver;

import java.io.IOException;

/**
 * A class with data that is saveable.
 * @author Richard Yin.
 */
abstract public class Saveable {
    private InterfaceSaver saver;
    public void setSaver(InterfaceSaver saver) {
        this.saver = saver;
    }
    public void saveAll() {
        saver.saveAll();
    }
    protected InterfaceSaver getSaver() {
        return saver;
    }

    /** Save the current state.
     * @throws IOException error regarding files
     */
    public abstract void save() throws IOException;

    /** Load the current state.
     * @throws IOException error regarding files
     * @throws ClassNotFoundException cannot find class in classpath
     */
    public abstract void load() throws IOException, ClassNotFoundException;
    public void makeChange() {
        if(saver!=null){
            saver.makeChange();
        }
    }
}
