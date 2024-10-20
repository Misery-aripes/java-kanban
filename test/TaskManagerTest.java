import model.Epic;
import model.Task;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Epic epic;
    protected Subtask subTask;
    protected Task task;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = createTaskManager();
        epic = new Epic("Тестовый Эпик", "Описание тестового Эпика");
        taskManager.createEpic(epic);

        subTask = new Subtask("Тестовая Подзадача", "Описание тестовой подзадачи", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic);
        taskManager.createSubtask(subTask);

        task = new Task("Тестовая Задача", "Описание тестовой задачи",
                TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 12, 30));
        taskManager.createTask(task);
    }

    @Test
    abstract void shouldCreateTask() throws IOException;

    @Test
    abstract void shouldCreateEpic() throws IOException;

    @Test
    abstract void shouldCreateSubTask() throws IOException;

    @Test
    abstract void shouldDeleteTask();

    @Test
    abstract void shouldDeleteEpic();

    @Test
    abstract void shouldDeleteSubTask();
}