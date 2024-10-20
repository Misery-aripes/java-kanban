import com.google.gson.Gson;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    private TaskManager manager;
    private HttpTaskServer taskServer;
    private HttpClient client;
    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        client = HttpClient.newHttpClient();
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description of test task",
                TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Некорректный статус код при создании задачи");

        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test Task", tasksFromManager.get(0).getName(), "Имя задачи не совпадает");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description of test task",
                TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        manager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус код при получении задач");

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.length, "Некорректное количество задач");
        assertEquals("Test Task", tasks[0].getName(), "Имя задачи не совпадает");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description of test task",
                TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        manager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус код при удалении задачи");

        List<Task> tasksFromManager = manager.getTasks();
        assertEquals(0, tasksFromManager.size(), "Задача не была удалена");
    }
}
