package dev.badbird.picar.ws.clientbound;

import dev.badbird.picar.motor.MotorSide;
import dev.badbird.picar.object.MotorMovementState;
import dev.badbird.picar.object.MovementDirection;
import dev.badbird.picar.ws.PacketDirection;
import dev.badbird.picar.ws.WSPacket;
import dev.badbird.picar.ws.WebSocketPacket;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@WSPacket(direction = PacketDirection.CLIENTBOUND, name = "heartbeat")
public class ClientboundHeartbeatWSPacket implements WebSocketPacket {
    private long time = System.currentTimeMillis();
    private Map<MotorSide, MotorMovementState> motors;
    private MovementDirection movementDirection;
}
