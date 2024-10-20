import history.HistoryManager;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    @Test
    public void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("model.Task", "Description");
        task.setId(1);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть пустой.");
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
    }

    @Test
    public void remove() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("model.Task", "Description");
        task.setId(1);
        historyManager.add(task);
        historyManager.remove(task.getId());
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления задачи.");
    }

    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    public void shouldReturnEmptyHistoryWhenNoTasksViewed() {
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой, если не было задач");
    }

    @Test
    public void shouldHandleDuplicateTasksInHistory() {
        Task task = new Task("Task", "Description", Duration.ofMinutes(30), LocalDateTime.now());
        historyManager.add(task);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "История не должна содержать дубликаты");
    }

    @Test
    public void shouldRemoveTaskFromHistoryCorrectly() {
        Task task1 = new Task("Task 1", "Description", Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", Duration.ofMinutes(30), LocalDateTime.now());

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        assertEquals(1, historyManager.getHistory().size(),
                "История должна содержать одну задачу после удаления");
        assertEquals(task2, historyManager.getHistory().get(0), "Оставшаяся задача должна быть task2");
    }
}

