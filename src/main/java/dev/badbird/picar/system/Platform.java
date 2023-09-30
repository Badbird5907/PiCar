package dev.badbird.picar.system;

import dev.badbird.picar.system.impl.NoOpPlatform;
import dev.badbird.picar.system.impl.pi.PiPlatform;

public class Platform {

    private static IPlatform system = null;

    public static IPlatform getPlatform() {
        if (system != null)
            return system;
        // if com.pi4j.Pi4J exists, return a PiSystem
        try {
            Class.forName("com.pi4j.io.gpio.GpioFactory");
            return system = new PiPlatform();
        } catch (ClassNotFoundException ignored) {
        }
        return system = new NoOpPlatform();
    }
}
