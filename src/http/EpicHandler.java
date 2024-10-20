package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public EpicHandler(TaskManager taskManager) {
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
        String response = gson.toJson(taskManager.getEpics());
        sendText(exchange, response, 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == null) {
            taskManager.createEpic(epic);
            sendText(exchange, "Эпик создан", 201);
        } else {
            taskManager.updateEpic(epic);
            sendText(exchange, "Эпик обновлен", 200);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");
        if (segments.length == 3) {
            int epicId = Integer.parseInt(segments[2]);
            taskManager.deleteEpicById(epicId);
            sendText(exchange, "Эпик удален", 200);
        } else {
            sendNotFound(exchange);
        }
    }
}