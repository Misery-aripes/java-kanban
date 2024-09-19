import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);

        Task savedTask = taskManager.getTask(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void addNewSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic);
        taskManager.createSubtask(subtask);

        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void history() {
        Task task1 = new Task("model.Task 1", "Description 1");
        taskManager.createTask(task1);
        Task task2 = new Task("model.Task 2", "Description 2");
        taskManager.createTask(task2);
        Task task3 = new Task("model.Task 3", "Description 3");
        taskManager.createTask(task3);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "История просмотров неверная.");
        assertEquals(task1, history.get(0), "История просмотров неверная.");
        assertEquals(task2, history.get(1), "История просмотров неверная.");
        assertEquals(task3, history.get(2), "История просмотров неверная.");
    }

    @Test
    public void removeSubtaskUpdatesEpic() {
        Epic epic = new Epic("Test model.Epic", "Test Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test model.Subtask", "Description", epic);
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtask.getId());

        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        assertTrue(updatedEpic.getSubtaskList().isEmpty(), "Список подзадач эпика должен быть пустым после удаления подзадачи.");
    }
}