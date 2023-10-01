package dev.badbird.picar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.badbird.picar.handler.APIRoute;
import dev.badbird.picar.handler.FrontendHandler;
import dev.badbird.picar.handler.LedHandler;
import dev.badbird.picar.handler.WebSocketHandler;
import dev.badbird.picar.object.Configuration;
import dev.badbird.picar.system.Platform;
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
        app.ws("/api/ws/stream", new WebSocketHandler(executor));
        for (Handler route : routes) {
            APIRoute annotation = route.getClass().getAnnotation(APIRoute.class);
            if (annotation == null) continue;
            app.addHandler(annotation.type(), annotation.value(), route);
        }
    }
}