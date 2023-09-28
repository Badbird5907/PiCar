package dev.badbird.picar;

import com.github.sarxos.webcam.Webcam;
import io.javalin.Javalin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(1337);
        app.ws("/ws", ws -> { // TESTING CODE
            Webcam webcam = Webcam.getDefault();
            ws.onConnect(ctx -> {
                System.out.println("Connected");
                webcam.open();
                System.out.println("Started grabber");
                executor.scheduleAtFixedRate(() -> {
                    System.out.println("Scheduler called");
                    if (!webcam.isOpen()) return;
                    System.out.println("Getting image");
                    BufferedImage image = webcam.getImage();
                    System.out.println("Converting to byte array");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(image, "jpg", baos);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    byte[] bytes = baos.toByteArray();
                    System.out.println("Converting to base64 (" + bytes.length + " bytes)");
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    System.out.println("Sending " + base64);
                    ctx.send(base64);
                }, 20, 20, TimeUnit.MILLISECONDS);
            });
            ws.onClose(ctx -> {
                webcam.close();
            });
        });
    }
}