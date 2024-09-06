import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    @Test
    public void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Task", "Description");
        task.setId(1);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть пустой.");
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
    }

    @Test
    public void remove() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Task", "Description");
        task.setId(1); // Установка ID задачи
        historyManager.add(task);
        historyManager.remove(task.getId());
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления задачи.");
    }
}
