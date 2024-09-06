import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    private final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldCreateSubtask() {
        Epic epic = new Epic("Test Epic", "Test Description");
        Subtask subtask = new Subtask("Test Subtask", "Test Subtask Description", epic);
        Subtask createdSub = taskManager.createSubtask(subtask);
        assertNotNull(subtask.getId(), "ID подзадачи не должен быть null");
        assertEquals("Test Subtask", subtask.getName(), "Имя подзадачи не совпадает");
        assertEquals("Test Subtask Description", subtask.getDescription(), "Описание подзадачи не совпадает");
        assertEquals(epic, subtask.getEpic(), "Эпик подзадачи не совпадает");
    }
}