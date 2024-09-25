import history.HistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void addAndRetrieveHistory() {
        Task task1 = new Task("model.Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("model.Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task("model.Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История просмотров неверная.");
        assertEquals(task1, history.get(0), "История просмотров неверная.");
        assertEquals(task2, history.get(1), "История просмотров неверная.");
        assertEquals(task3, history.get(2), "История просмотров неверная.");
    }

    @Test
    public void doNotHaveDuplicatesInHistory() {
        Task task = new Task("model.Task", "Description");
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не должна содержать дубликаты.");
    }

    @Test
    public void removeTaskFromHistory() {
        Task task1 = new Task("model.Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("model.Task 2", "Description 2");
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу после удаления.");
        assertEquals(task2, history.get(0), "Неверная задача в истории после удаления.");
    }
}