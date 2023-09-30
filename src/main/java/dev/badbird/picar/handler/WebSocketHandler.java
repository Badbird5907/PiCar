package dev.badbird.picar.handler;

import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class WebSocketHandler implements Consumer<WsConfig> {
    private final FrameGrabber grabber = new OpenCVFrameGrabber(0);
    private final List<WsContext> contexts = new ArrayList<>();
    private final ScheduledExecutorService executor;
    private static final Java2DFrameConverter paintConverter = new Java2DFrameConverter();

    @SneakyThrows
    public WebSocketHandler(ScheduledExecutorService executor) {
        this.executor = executor;
        grabber.start();
        new Thread(() -> {
            while (true) {
                //  System.out.println("Waiting for contexts");
                try {
                    Thread.sleep(1000 / 60); // some arbitrary number i pulled out of my ass
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (contexts.isEmpty()) {
                    // System.out.println("No contexts, sleeping");
                    continue;
                }
                Frame frame = null;
                try {
                    frame = grabber.grab();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                    continue;
                    // throw new RuntimeException(e);
                }
                BufferedImage image = frameToBufferedImage(frame);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "jpg", baos);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                    // throw new RuntimeException(e);
                }
                byte[] bytes = baos.toByteArray();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                for (WsContext context : contexts) {
                    executor.execute(() -> { // TODO use virtual threads when they're out
                        context.send(base64); // synchronous
                    });
                }
            }
        }).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                grabber.close();
            } catch (FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private BufferedImage frameToBufferedImage(Frame frame) {
        return paintConverter.getBufferedImage(frame, 1);
    }

    @Override
    public void accept(WsConfig ws) {
        ws.onConnect(ctx -> {
            System.out.println("Client connected");
            contexts.add(ctx);
        });
        ws.onClose(ctx -> {
            System.out.println("Client disconnected");
            // scheduledFuture.cancel(true);
            contexts.remove(ctx);
        });
        ws.onError(ctx -> {
            System.out.println("Client errored");
            ctx.error().printStackTrace();
            ctx.closeSession();
            contexts.remove(ctx);
        });
    }
}
