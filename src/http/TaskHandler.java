package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = GsonUtilLTA.createGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        switch (method) {
            case "GET" -> handleGet(exchange);
            case "POST" -> handlePost(exchange);
            case "DELETE" -> handleDeleteTask(exchange, path);
            default -> sendNotFound(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getTasks()), 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            Task task = gson.fromJson(isr, Task.class);

            if (task == null || task.getName() == null || task.getDescription() == null) {
                sendInternalError(exchange, "Некорректный формат задачи или отсутствуют обязательные поля");
                return;
            }

            taskManager.createTask(task);
            exchange.sendResponseHeaders(201, -1);
        } catch (Exception e) {
            sendInternalError(exchange, "Ошибка при обработке создания задачи: " + e.getMessage());
        }
    }

    private void handleDeleteTask(HttpExchange exchange, String path) throws IOException {
        handleDelete(exchange, path,
                id -> Optional.ofNullable(taskManager.getTask(id)),
                taskManager::deleteTaskById);
    }

    private void handleDelete(HttpExchange exchange, String path,
                              Function<Integer, Optional<Task>> getTaskById,
                              Consumer<Integer> deleteTask) throws IOException {
        String[] pathParts = path.split("/");

        if (pathParts.length != 3 || !pathParts[2].matches("\\d+")) {
            sendNotFound(exchange);
            return;
        }

        int taskId = Integer.parseInt(pathParts[2]);

        try {
            getTaskById.apply(taskId)
                    .orElseThrow(() -> new NoSuchElementException("Задача с ID " + taskId + " не найдена"));

            deleteTask.accept(taskId);
            exchange.sendResponseHeaders(200, -1);
        } catch (NoSuchElementException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            sendInternalError(exchange, e.getMessage());
        } finally {
            exchange.close();
        }
    }
}