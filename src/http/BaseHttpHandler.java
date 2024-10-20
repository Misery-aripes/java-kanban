package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Not Found\"}", 404);
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Task has interactions with existing tasks\"}", 406);
    }

    protected void sendInternalError(HttpExchange exchange, String errorMessage) throws IOException {
        byte[] response = errorMessage.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(500, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}