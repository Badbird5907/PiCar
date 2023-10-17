package dev.badbird.picar.ws.serverbound;

import com.google.gson.JsonObject;
import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.platform.Platform;
import dev.badbird.picar.ws.PacketDirection;
import dev.badbird.picar.ws.WSPacket;
import dev.badbird.picar.ws.WebSocketPacket;
import dev.badbird.picar.ws.clientbound.ClientboundHeartbeatWSPacket;

@WSPacket(name = "motor", direction = PacketDirection.SERVERBOUND)
public class ServerboundMotorWSPacket implements WebSocketPacket {
    private String state;

    @Override
    public void handle(JsonObject jsonObject) {
        System.out.println("Received motor packet: " + state);
        switch (state.toLowerCase()) {
            case "w": {
                Platform.getPlatform().getMotorController().startForward();
                break;
            }
            case "a": {
                Platform.getPlatform().getMotorController().startLeft();
                break;
            }
            case "s": {
                Platform.getPlatform().getMotorController().startBackward();
                break;
            }
            case "d": {
                Platform.getPlatform().getMotorController().startRight();
                break;
            }
            case "stop": {
                Platform.getPlatform().getMotorController().stop();
                break;
            }
            default: {
                System.out.println("Unknown motor state: " + state);
            }
        }
        IMotorController<?> mc = Platform.getPlatform().getMotorController();
        new ClientboundHeartbeatWSPacket(System.currentTimeMillis(),
                mc.getMotorStates(),
                mc.getDirection()
        ).send();

    }
}
