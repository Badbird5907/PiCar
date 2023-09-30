package dev.badbird.picar;

import dev.badbird.picar.handler.WebSocketHandler;
import io.javalin.Javalin;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class Main {
    private static final ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(5);

    @SneakyThrows
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(1337);
        app.ws("/api/ws/stream", new WebSocketHandler(executor));
        String data = Files.readAllLines(Paths.get("index.html")).stream().collect(Collectors.joining("\n"));
        app.get("/old", ctx -> {
            // give the client the index.html file
            ctx.contentType("text/html");
            ctx.result(data);
        });
        // serve anything in frontend/* in the jar
        app.get("/*", ctx -> {
            String path = ctx.path().substring(1);
            if (path.isEmpty()) path = "index.html";
            Map.Entry<String, InputStream> file = resolveFile(path);
            if (file == null) {
                ctx.status(404);
                return;
            }
            ctx.contentType(file.getKey());
            ctx.result(file.getValue());
        });
    }

    @SneakyThrows
    private static Map.Entry<String, InputStream> resolveFile(String path) {
        InputStream resource = Main.class.getResourceAsStream("/frontend/" + path);
        if (resource == null && path.equals("index.html")) return null; // avoid stack overflow
        if (resource == null) return resolveFile("index.html");
        return Map.entry(Files.probeContentType(Paths.get(path)), resource);
    }
}