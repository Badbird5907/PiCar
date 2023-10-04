package dev.badbird.picar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.badbird.picar.handler.*;
import dev.badbird.picar.handler.ws.WSHandler;
import dev.badbird.picar.handler.ws.StreamWSHandler;
import dev.badbird.picar.object.Configuration;
import dev.badbird.picar.system.Platform;
import dev.badbird.picar.ws.WSPacketRegistry;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.plugin.bundled.CorsPluginConfig;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ScheduledExecutorService;

@Data
public class PiCar {
    private static final Logger logger = LoggerFactory.getLogger(PiCar.class);
    private static final ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(5);
    @Getter
    private static final PiCar instance = new PiCar();
    @Getter
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        // System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
        instance.init();
    }

    private Configuration configuration;
    private WSPacketRegistry packetRegistry;

    @SneakyThrows
    public void init() {
        File config = new File("config.json");
        if (!config.exists()) {
            configuration = new Configuration();
            configuration.save();
        } else {
            configuration = gson.fromJson(Files.readString(config.toPath()), Configuration.class);
        }
        logger.info("Initializing platform...");
        Platform.getPlatform().init();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Platform.getPlatform().cleanup();
                Platform.getPlatform().getMotorController().cleanup();
            }
        });
        logger.info("Platform initialized!");
        logger.info("Initializing motors...");
        Platform.getPlatform().getMotorController().init();
        logger.info("Motors initialized!");
        logger.info("Starting server on port {}", configuration.getPort());
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.plugins.enableCors(cors -> {
                cors.add(CorsPluginConfig::anyHost);
            });
        }).start(configuration.getPort());
        Handler[] routes = {
                new FrontendHandler()
        };
        LedHandler.init(app);
        app.ws("/api/ws/stream", new StreamWSHandler(executor));
        packetRegistry = new WSPacketRegistry(executor);
        packetRegistry.init();
        app.ws("/api/ws/", new WSHandler());
        for (Handler route : routes) {
            APIRoute annotation = route.getClass().getAnnotation(APIRoute.class);
            if (annotation == null) continue;
            app.addHandler(annotation.type(), annotation.value(), route);
        }
    }
}