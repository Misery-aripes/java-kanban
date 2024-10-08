import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;


public class TaskTest {

    private final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldCreateTask() {
        Task task = new Task("Test model.Task", "Test Description");
        Task createdTask = taskManager.createTask(task);
        assertNotNull(task.getId(), "ID задачи не должен быть null");
        assertEquals("Test model.Task", task.getName(), "Имя задачи не совпадает");
        assertEquals("Test Description", task.getDescription(), "Описание задачи не совпадает");
    }

    @Test
    public void shouldUpdateTask() {
        Task task = new Task(1, "Test model.Task", "Test Description", TaskStatus.IN_PROGRESS);
        task.setName("Updated model.Task");
        task.setDescription("Updated Description");
        task.setStatus(TaskStatus.DONE);
        assertEquals("Updated model.Task", task.getName(), "Имя задачи не обновлено");
        assertEquals("Updated Description", task.getDescription(), "Описание задачи не обновлено");
        assertEquals(TaskStatus.DONE, task.getStatus(), "Статус задачи не обновлен");
    }
}