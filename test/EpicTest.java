import model.Epic;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    private final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldCreateEpic() {
        Epic epic = new Epic("Test model.Epic", "Test Description");
        Epic createdEpic = taskManager.createEpic(epic);
        assertNotNull(epic.getId(), "ID эпика не должен быть null");
        assertEquals("Test model.Epic", epic.getName(), "Имя эпика не совпадает");
        assertEquals("Test Description", epic.getDescription(), "Описание эпика не совпадает");
        assertTrue(epic.getSubtaskList().isEmpty(), "Список подзадач эпика должен быть пустым");
    }
}