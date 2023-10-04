package dev.badbird.picar.object;

import dev.badbird.picar.PiCar;
import dev.badbird.picar.system.Platform;
import dev.badbird.picar.ws.clientbound.ClientboundHeartbeatWSPacket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartbeatThread extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Sending heartbeat");
            ClientboundHeartbeatWSPacket packet = new ClientboundHeartbeatWSPacket(
                    System.currentTimeMillis(),
                    Platform.getPlatform().getMotorController().getMovementStates()
            );
            packet.send();
        }
    }
}
