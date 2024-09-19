import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ManagersTest {
    @Test
    public void shouldReturnDefaultTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Менеджер задач не должен быть null");
    }

    @Test
    public void shouldReturnDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории не должен быть null");
    }
}