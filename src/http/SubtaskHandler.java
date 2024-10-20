package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            handleGet(exchange);
        } else if (method.equals("POST")) {
            handlePost(exchange);
        } else if (method.equals("DELETE")) {
            handleDelete(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getSubtasks());
        sendText(exchange, response, 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        if (subtask.getId() == null) {
            taskManager.createSubtask(subtask);
            sendText(exchange, "Подзадача создана", 201);
        } else {
            taskManager.updateSubtask(subtask);
            sendText(exchange, "Подзадача обновлена", 200);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");
        if (segments.length == 3) {
            int subtaskId = Integer.parseInt(segments[2]);
            taskManager.deleteSubtaskById(subtaskId);
            sendText(exchange, "Подзадача удалена", 200);
        } else {
            sendNotFound(exchange);
        }
    }
}