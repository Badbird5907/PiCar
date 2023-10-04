package dev.badbird.picar.ws;

import com.google.gson.JsonObject;
import dev.badbird.picar.PiCar;

public interface WebSocketPacket {
    default JsonObject serialize() {
        JsonObject jsonObject = PiCar.getGson().toJsonTree(this).getAsJsonObject();
        JsonObject packet = new JsonObject();
        packet.addProperty("name", getPacketName());
        packet.add("data", jsonObject);
        return packet;
    }

    default String getPacketName() {
        WSPacket annotation = this.getClass().getAnnotation(WSPacket.class);
        if (annotation == null)
            return null;
        return annotation.name();
    }

    default void handle(JsonObject jsonObject) {

    }

    default void send() {
        WSPacketRegistry.getInstance().send(this);
    }
}
