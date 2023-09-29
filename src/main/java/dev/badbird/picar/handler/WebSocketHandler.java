package dev.badbird.picar.handler;

import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;
import lombok.SneakyThrows;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class WebSocketHandler implements Consumer<WsConfig> {
    private final ScheduledExecutorService executor;
    // private final Webcam webcam;
    private final FrameGrabber grabber = new OpenCVFrameGrabber(0);
    private boolean run = false;
    private final List<WsContext> contexts = new ArrayList<>();
    private ScheduledFuture<?> scheduledFuture = null;

    @SneakyThrows
    public WebSocketHandler(ScheduledExecutorService executor) {
        this.executor = executor;
        // webcam = Webcam.getDefault();
        scheduledFuture = executor.scheduleAtFixedRate(() -> {
            if (!run || contexts.isEmpty()) return;
            // if (!webcam.isOpen() || contexts.isEmpty()) return;
            // BufferedImage image = webcam.getImage();
            System.out.println("Grabbing frame");
            Frame frame = null;
            try {
                frame = grabber.grab();
            } catch (FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("Got frame");
            BufferedImage image = frameToBufferedImage(frame);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "jpg", baos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] bytes = baos.toByteArray();
            System.out.println("Sending frame");
            String base64 = Base64.getEncoder().encodeToString(bytes);
            for (WsContext context : contexts) {
                context.send(base64);
            }
        }, 20, 20, TimeUnit.MILLISECONDS);
    }

    private BufferedImage frameToBufferedImage(Frame frame) {
        System.out.println("Converting frame");
        Java2DFrameConverter paintConverter = new Java2DFrameConverter();
        return paintConverter.getBufferedImage(frame, 1);
    }

    @Override
    public void accept(WsConfig ws) {
        ws.onConnect(ctx -> {
            System.out.println("Client connected");
            // webcam.open();
            grabber.start();
            contexts.add(ctx);
            run = true;
        });
        ws.onClose(ctx -> {
            System.out.println("Client disconnected");
            scheduledFuture.cancel(true);
            // webcam.close(); // TODO check if ctx list is empty
            grabber.close();
            run = false;
            contexts.remove(ctx);
        });
    }
}
