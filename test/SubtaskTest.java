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
        Epic epic = taskManager.createEpic(new Epic("Test model.Epic", "Test Description"));

        Subtask subtask = new Subtask("Test model.Subtask", "Test model.Subtask Description", epic);
        Subtask createdSub = taskManager.createSubtask(subtask);

        assertNotNull(createdSub.getId(), "ID подзадачи не должен быть null");

        Subtask savedSubtask = taskManager.getSubtask(createdSub.getId());
        assertNotNull(savedSubtask, "Подзадача не была сохранена в менеджере");

        assertEquals("Test model.Subtask", savedSubtask.getName(), "Имя подзадачи не совпадает");
        assertEquals("Test model.Subtask Description", savedSubtask.getDescription(),
                "Описание подзадачи не совпадает");
        assertEquals(epic, savedSubtask.getEpic(), "Эпик подзадачи не совпадает");
    }
}
