import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    private final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldCreateSubtask() {
        Epic epic = new Epic("Test model.Epic", "Test Description");
        Subtask subtask = new Subtask("Test model.Subtask", "Test model.Subtask Description", epic);
        Subtask createdSub = taskManager.createSubtask(subtask);
        assertNotNull(subtask.getId(), "ID подзадачи не должен быть null");
        assertEquals("Test model.Subtask", subtask.getName(), "Имя подзадачи не совпадает");
        assertEquals("Test model.Subtask Description", subtask.getDescription(), "Описание подзадачи не совпадает");
        assertEquals(epic, subtask.getEpic(), "Эпик подзадачи не совпадает");
    }
}