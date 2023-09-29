package dev.badbird.picar;

import dev.badbird.picar.handler.WebSocketHandler;
import io.javalin.Javalin;

import java.util.concurrent.ScheduledExecutorService;

public class Main {
    private static final ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(1337);
        app.ws("/ws", new WebSocketHandler(executor));
    }
}