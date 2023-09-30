package dev.badbird.picar.handler;

import dev.badbird.picar.PiCar;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@APIRoute("/*")
public class FrontendHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) {
        String path = ctx.path().substring(1);
        if (path.isEmpty()) path = "index.html";
        Map.Entry<String, InputStream> file = resolveFile(path);
        if (file == null) {
            ctx.status(404);
            return;
        }
        ctx.contentType(file.getKey());
        ctx.result(file.getValue());
    }

    @SneakyThrows
    private static Map.Entry<String, InputStream> resolveFile(String path) {
        InputStream resource = PiCar.class.getResourceAsStream("/frontend/" + path);
        if (resource == null && path.equals("index.html")) return null; // avoid stack overflow
        if (resource == null) return resolveFile("index.html");
        return Map.entry(Files.probeContentType(Paths.get(path)), resource);
    }
}
