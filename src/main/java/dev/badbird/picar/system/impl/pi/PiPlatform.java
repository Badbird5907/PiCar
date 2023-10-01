package dev.badbird.picar.system.impl.pi;

import com.pi4j.io.gpio.*;
import dev.badbird.picar.system.IPlatform;
import org.slf4j.Logger;

public class PiPlatform implements IPlatform {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PiPlatform.class);
    private GpioController controller;
    private GpioPinDigitalOutput led;
    @Override
    public void init() {
        controller = GpioFactory.getInstance();
        led = controller.provisionDigitalOutputPin(BCM.GPIO_08, "LED", PinState.LOW);
    }

    @Override
    public void setLedState(boolean state) {
        logger.info("Setting LED state to {}", state);
        led.setState(state);
    }

    @Override
    public boolean getLedState() {
        logger.info("LED State is {}", led.getState());
        return led.isState(PinState.HIGH);
    }
}
