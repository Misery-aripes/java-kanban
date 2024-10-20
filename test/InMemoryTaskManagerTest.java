import exeption.NotFoundException;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Override
    @Test
    void shouldCreateTask() throws IOException {
        assertNotNull(task, "Задача не была создана");
        assertEquals(task, taskManager.getTask(task.getId()), "Задача не совпадает с созданной");
    }

    @Override
    @Test
    void shouldCreateEpic() throws IOException {
        assertNotNull(epic, "Эпик не был создан");
        assertEquals(epic, taskManager.getEpicById(epic.getId()), "Эпик не совпадает с созданным");
    }

    @Override
    @Test
    void shouldCreateSubTask() throws IOException {
        assertNotNull(subTask, "Подзадача не была создана");
        assertEquals(subTask, taskManager.getSubtask(subTask.getId()), "Подзадача не совпадает с созданной");
    }

    @Override
    @Test
    void shouldDeleteTask() {
        taskManager.deleteTaskById(task.getId());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            taskManager.getTask(task.getId());
        });
        assertEquals("Задача с ID " + task.getId() + " не найдена.", exception.getMessage());
    }

    @Override
    @Test
    void shouldDeleteEpic() {
        taskManager.deleteEpicById(epic.getId());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            taskManager.getEpicById(epic.getId());
        });
        assertEquals("Эпик с ID " + epic.getId() + " не найден.", exception.getMessage());

        assertTrue(taskManager.getSubtasks().isEmpty(), "Связанные подзадачи не были удалены");
    }

    @Override
    @Test
    void shouldDeleteSubTask() {
        taskManager.deleteSubtaskById(subTask.getId());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            taskManager.getSubtask(subTask.getId());
        });
        assertEquals("Подзадача с ID " + subTask.getId() + " не найдена.", exception.getMessage());
    }

    @Test
    public void shouldEpicStatusBeNewWhenAllSubtasksAreNew() {
        Epic epic = new Epic("Тестовый Эпик", "Описание Эпика");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(1), epic);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(2), epic);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус эпика должен быть NEW, когда все подзадачи со статусом NEW");
    }

    @Test
    public void shouldEpicStatusBeDoneWhenAllSubtasksAreDone() {
        Epic epic = new Epic("Тестовый Эпик", "Описание Эпика");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", TaskStatus.DONE,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(1), epic);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", TaskStatus.DONE,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(2), epic);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.DONE, epic.getStatus(),
                "Статус эпика должен быть DONE, когда все подзадачи со статусом DONE");
    }

    @Test
    public void shouldEpicStatusBeInProgressWhenSubtasksAreNewAndDone() {
        Epic epic = new Epic("Тестовый Эпик", "Описание Эпика");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(1), epic);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", TaskStatus.DONE,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(2), epic);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус эпика должен быть IN_PROGRESS при подзадачах со статусами NEW и DONE");
    }

    @Test
    public void shouldEpicStatusBeInProgressWhenAllSubtasksAreInProgress() {
        Epic epic = new Epic("Тестовый Эпик", "Описание Эпика");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", TaskStatus.IN_PROGRESS,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(1), epic);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", TaskStatus.IN_PROGRESS,
                Duration.ofMinutes(15), LocalDateTime.now().plusHours(2), epic);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус эпика должен быть IN_PROGRESS, когда все подзадачи со статусом IN_PROGRESS");
    }
}