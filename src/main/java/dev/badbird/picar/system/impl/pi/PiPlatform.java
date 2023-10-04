package dev.badbird.picar.system.impl.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.motor.impl.l298n.L298nMotorController;
import dev.badbird.picar.system.IPlatform;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;

@Getter
public class PiPlatform implements IPlatform {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PiPlatform.class);
    private GpioController controller;
    private GpioPinDigitalOutput led;
    private IMotorController motorController;

    @SneakyThrows
    @Override
    public void init() {
        controller = GpioFactory.getInstance();
        led = controller.provisionDigitalOutputPin(BCM.GPIO_08, "LED", PinState.LOW);
        motorController = new L298nMotorController();
    }

    @Override
    public void cleanup() {
        controller.shutdown();
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
