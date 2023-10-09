package dev.badbird.picar.platform.impl.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.motor.impl.l298n.L298nMotorController;
import dev.badbird.picar.platform.IPlatform;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;

@Getter
public class PiPlatform implements IPlatform {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PiPlatform.class);
    private GpioController controller;
    private IMotorController motorController;

    @SneakyThrows
    @Override
    public void init() {
        controller = GpioFactory.getInstance();
        motorController = new L298nMotorController(this);
    }

    @Override
    public void cleanup() {
        controller.shutdown();
    }

}
