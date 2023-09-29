package dev.badbird.picar.handler;

import com.github.sarxos.webcam.Webcam;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;

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
    private final Webcam webcam;
    private final List<WsContext> contexts = new ArrayList<>();
    private ScheduledFuture<?> scheduledFuture = null;

    public WebSocketHandler(ScheduledExecutorService executor) {
        this.executor = executor;
        webcam = Webcam.getDefault();
        scheduledFuture = executor.scheduleAtFixedRate(() -> {
            if (!webcam.isOpen() || contexts.isEmpty()) return;
            BufferedImage image = webcam.getImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "jpg", baos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] bytes = baos.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            for (WsContext context : contexts) {
                context.send(base64);
            }
        }, 20, 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public void accept(WsConfig ws) {
        ws.onConnect(ctx -> {
            webcam.open();
            contexts.add(ctx);
        });
        ws.onClose(ctx -> {
            scheduledFuture.cancel(true);
            webcam.close();
            contexts.remove(ctx);
        });
    }
}
