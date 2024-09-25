import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import service.FileBackedTaskManager;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager taskManager;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        testFile = new File("test_tasks.csv");
        taskManager = new FileBackedTaskManager(testFile);

        if (!testFile.exists()) {
            testFile.createNewFile();
        }
    }

    @AfterEach
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void shouldSaveAndLoadTasks() {
        Task task = new Task("Test Task", "Test Description", TaskStatus.NEW);
        taskManager.createTask(task);

        Epic epic = new Epic("Test Epic", "Test Epic Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Test Subtask Description", TaskStatus.IN_PROGRESS, epic);
        taskManager.createSubtask(subtask);

        try {
            List<String> lines = Files.readAllLines(testFile.toPath());
            assertEquals(4, lines.size(), "Неверное количество строк в файле после сохранения");
        } catch (IOException e) {
            fail("Ошибка при чтении файла: " + e.getMessage());
        }

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(testFile);
        List<Task> tasks = loadedTaskManager.getTasks();
        List<Epic> epics = loadedTaskManager.getEpics();
        List<Subtask> subtasks = loadedTaskManager.getSubtasks();

        assertEquals(1, tasks.size(), "Неверное количество задач после загрузки");
        assertEquals(task, tasks.get(0), "Задача загружена некорректно");

        assertEquals(1, epics.size(), "Неверное количество эпиков после загрузки");
        assertEquals(epic, epics.get(0), "Эпик загружен некорректно");

        assertEquals(1, subtasks.size(), "Неверное количество подзадач после загрузки");
        assertEquals(subtask, subtasks.get(0), "Подзадача загружена некорректно");
    }

    @Test
    public void shouldCreateAndLoadEmptyFile() {
        try {
            List<String> lines = Files.readAllLines(testFile.toPath());
            assertTrue(lines.isEmpty(), "Файл должен быть пустым при создании");
        } catch (IOException e) {
            fail("Ошибка при чтении файла: " + e.getMessage());
        }

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(testFile);
        List<Task> tasks = loadedTaskManager.getTasks();
        List<Epic> epics = loadedTaskManager.getEpics();
        List<Subtask> subtasks = loadedTaskManager.getSubtasks();

        assertTrue(tasks.isEmpty(), "Задачи должны отсутствовать в пустом файле");
        assertTrue(epics.isEmpty(), "Эпики должны отсутствовать в пустом файле");
        assertTrue(subtasks.isEmpty(), "Подзадачи должны отсутствовать в пустом файле");
    }
}