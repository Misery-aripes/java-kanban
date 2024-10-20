import exeption.NotFoundException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import service.FileBackedTaskManager;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static final String FILE_PATH = "tasks.csv";

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File(FILE_PATH));
    }

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = createTaskManager();
        epic = new Epic("Тестовый Эпик", "Описание тестового Эпика");
        taskManager.createEpic(epic);

        subTask = new Subtask("Тестовая Подзадача", "Описание тестовой подзадачи", TaskStatus.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 10, 10, 12, 0), epic);
        taskManager.createSubtask(subTask);

        task = new Task("Тестовая Задача", "Описание тестовой задачи",
                TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 12, 40));
        taskManager.createTask(task);
    }

    @AfterEach
    public void tearDown() throws IOException {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    @Test
    public void shouldCreateTask() throws IOException {
        Task loadedTask = taskManager.getTask(task.getId());
        assertNotNull(loadedTask, "Задача не была создана");
        assertEquals(task, loadedTask, "Задача не совпадает с созданной");
    }

    @Override
    @Test
    public void shouldCreateEpic() throws IOException {
        Epic loadedEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(loadedEpic, "Эпик не был создан");
        assertEquals(epic, loadedEpic, "Эпик не совпадает с созданным");
    }

    @Override
    @Test
    public void shouldCreateSubTask() throws IOException {
        Subtask loadedSubTask = taskManager.getSubtask(subTask.getId());
        assertNotNull(loadedSubTask, "Подзадача не была создана");
        assertEquals(subTask, loadedSubTask, "Подзадача не совпадает с созданной");
    }

    @Override
    @Test
    public void shouldDeleteTask() {
        taskManager.deleteTaskById(task.getId());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            taskManager.getTask(task.getId());
        });
        assertEquals("Задача с ID " + task.getId() + " не найдена.", exception.getMessage());
    }

    @Override
    @Test
    public void shouldDeleteEpic() {
        taskManager.deleteEpicById(epic.getId());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            taskManager.getEpicById(epic.getId());
        });
        assertEquals("Эпик с ID " + epic.getId() + " не найден.", exception.getMessage());

        assertTrue(taskManager.getSubtasks().isEmpty(), "Связанные подзадачи не были удалены");
    }


    @Override
    @Test
    public void shouldDeleteSubTask() {
        taskManager.deleteSubtaskById(subTask.getId());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            taskManager.getSubtask(subTask.getId());
        });
        assertEquals("Подзадача с ID " + subTask.getId() + " не найдена.", exception.getMessage());
    }
}