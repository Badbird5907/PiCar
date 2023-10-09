package dev.badbird.picar.object;

import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.platform.Platform;
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
            IMotorController<?> mc = Platform.getPlatform().getMotorController();
            ClientboundHeartbeatWSPacket packet = new ClientboundHeartbeatWSPacket(
                    System.currentTimeMillis(),
                    mc.getMovementStates(),
                    mc.getDirection()
            );
            packet.send();
        }
    }
}
