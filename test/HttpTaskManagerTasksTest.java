import com.google.gson.Gson;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
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

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    private TaskManager taskManager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        gson = GsonUtilLTA.createGson();  // Используем кастомный Gson
        client = HttpClient.newHttpClient();
        taskServer.start();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task to Delete", "Test Task Description",
                TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Некорректный статус при удалении задачи");

        assertTrue(taskManager.getTasks().isEmpty(), "Задача не была удалена");
    }
}