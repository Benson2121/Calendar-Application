package UseCaseTest;

import Gateway.InterfaceSaver;
import Gateway.Saver;
import org.junit.Test;
import UseCase.HistoryManager;
import java.io.IOException;
import static org.junit.Assert.*;

public class HistoryManagerTest {

    @Test(timeout = 100)
    public void getHistoryTest() throws IOException {
        InterfaceSaver save = new Saver();
        HistoryManager history = new HistoryManager();
        history.setSaver(save);
        history.addHistory("user", "log out");
        assertNotNull(history.getHistories());
    }

    @Test(timeout = 100)
    public void clearHistoryTest() throws IOException {
        InterfaceSaver save = new Saver();
        HistoryManager history = new HistoryManager();
        history.setSaver(save);
        history.addHistory("user", "log out");
        history.clearHistoriesByUser("user");
        assertTrue(history.getHistories().isEmpty());
    }

    @Test(timeout = 100)
    public void addHistoryTest() throws IOException {
        InterfaceSaver save = new Saver();
        HistoryManager history = new HistoryManager();
        history.setSaver(save);
        history.addHistory("user", "log out");
        assertTrue(history.getHistoriesByUser("user").contains("log out"));
    }
}
