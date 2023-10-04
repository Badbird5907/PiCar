package dev.badbird.picar.ws;

import com.google.gson.JsonObject;
import dev.badbird.picar.PiCar;
import dev.badbird.picar.handler.ws.WSHandler;
import io.javalin.websocket.WsContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class WSPacketRegistry {
    @Getter
    private static WSPacketRegistry instance;
    private final ExecutorService executorService;
    private Map<String, Class<? extends WebSocketPacket>> serverBoundPackets;

    public void init() {
        instance = this;
        serverBoundPackets = new HashMap<>();
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(Scanners.SubTypes.filterResultsBy(new FilterBuilder()))
                .addUrls(ClasspathHelper.forPackage(getClass().getPackageName()))
                .filterInputsBy(new FilterBuilder().add(s -> s.startsWith(getClass().getPackageName()))));
        for (Class<?> aClass : reflections.getSubTypesOf(Object.class)) {
            if (WebSocketPacket.class.isAssignableFrom(aClass)) {
                WSPacket annotation = aClass.getAnnotation(WSPacket.class);
                if (annotation == null)
                    continue;
                if (annotation.direction() == PacketDirection.SERVERBOUND)
                    serverBoundPackets.put(annotation.name(), (Class<? extends WebSocketPacket>) aClass);
            }
        }
    }

    public void onReceive(JsonObject jsonObject) {
        String name = jsonObject.get("name").getAsString();
        Class<? extends WebSocketPacket> aClass = serverBoundPackets.get(name);
        if (aClass == null)
            return;
        try {
            JsonObject data = jsonObject.get("data").getAsJsonObject();
            WebSocketPacket packet = PiCar.getGson().fromJson(data, aClass);
            packet.handle(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send(WebSocketPacket packet) {
        JsonObject data = packet.serialize();
        String json = PiCar.getGson().toJson(data);
        for (WsContext connection : WSHandler.getConnections()) {
            executorService.submit(() -> {
                connection.send(json);
            });
        }
    }
}
