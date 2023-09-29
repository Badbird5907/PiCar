package dev.badbird.picar;

import dev.badbird.picar.handler.WebSocketHandler;
import io.javalin.Javalin;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class Main {
    private static final ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(1);

    @SneakyThrows
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(1337);
        app.ws("/ws", new WebSocketHandler(executor));
        String data = Files.readAllLines(Paths.get("index.html")).stream().collect(Collectors.joining("\n"));
        app.get("/", ctx -> {
            // give the client the index.html file
            ctx.contentType("text/html");
            ctx.result(data);
        });
    }
}