package dev.badbird.picar.handler.ws;

import com.google.gson.JsonObject;
import dev.badbird.picar.PiCar;
import dev.badbird.picar.platform.Platform;
import dev.badbird.picar.ws.WSPacketRegistry;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class WSHandler implements Consumer<WsConfig> {
    public WSHandler() {
    }
    @Getter
    private static final List<WsContext> connections = new ArrayList<>();
    @Override
    public void accept(WsConfig wsConfig) {
        wsConfig.onConnect(ctx -> {
            log.info("WS Connected");
            connections.add(ctx);
        });
        wsConfig.onClose(ctx -> {
            log.info("WS Closed");
            connections.remove(ctx);
            // stop motors just in case
            Platform.getPlatform().getMotorController().stop();
        });
        wsConfig.onError(ctx -> {
            log.error("WS Error", ctx.error());
            connections.remove(ctx);
            // stop motors just in case
            Platform.getPlatform().getMotorController().stop();
        });
        wsConfig.onMessage(ctx -> {
            log.info("WS Message: {}", ctx.message());
            String message = ctx.message();
            JsonObject json = PiCar.getGson().fromJson(message, JsonObject.class);
            WSPacketRegistry.getInstance().onReceive(json);
        });
    }
}
