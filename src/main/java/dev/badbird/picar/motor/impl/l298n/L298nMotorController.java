package dev.badbird.picar.motor.impl.l298n;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.motor.MotorSide;
import dev.badbird.picar.system.IPlatform;
import dev.badbird.picar.system.Platform;
import dev.badbird.picar.system.impl.pi.BCM;
import dev.badbird.picar.system.impl.pi.PiPlatform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class L298nMotorController implements IMotorController<L298nMotor> {
    private GpioController controller;
    private GpioPinAnalogOutput ena;
    private GpioPinDigitalOutput in1, in2, in3, in4;
    private Map<MotorSide, L298nMotor> motors;

    private final IPlatform platform;

    @Override
    public IPlatform getPlatform() {
        return platform;
    }

    @Override
    public void init() {
        if (platform instanceof PiPlatform pi) {
            controller = pi.getController();
            in1 = controller.provisionDigitalOutputPin(BCM.GPIO_03, "IN1");
            in2 = controller.provisionDigitalOutputPin(BCM.GPIO_05, "IN2");
            in3 = controller.provisionDigitalOutputPin(BCM.GPIO_11, "IN3");
            in4 = controller.provisionDigitalOutputPin(BCM.GPIO_13, "IN4");
            ena = controller.provisionAnalogOutputPin(BCM.GPIO_07, "ENA");

            in1.setShutdownOptions(true, PinState.LOW);
            in2.setShutdownOptions(true, PinState.LOW);
            in3.setShutdownOptions(true, PinState.LOW);
            in4.setShutdownOptions(true, PinState.LOW);

            motors = new HashMap<>();
            motors.put(MotorSide.TOP_LEFT, new L298nMotor(in1, in2, MotorSide.TOP_LEFT));
            motors.put(MotorSide.TOP_RIGHT, new L298nMotor(in3, in4, MotorSide.TOP_RIGHT));

            // initial state - turn off motors
            in1.low();
            in2.low();
            in3.low();
            in4.low();
        } else {
            throw new IllegalStateException("Platform is not a PiPlatform!");
        }
    }

    @Override
    public void cleanup() {

    }
    @Override
    public int getMotorCount() {
        return motors.size();
    }

    @Override
    public Optional<L298nMotor> getMotor(MotorSide side) {
        L298nMotor l298nMotor = motors.get(side);
        return Optional.ofNullable(l298nMotor);
    }

    @Override
    public void speed(double speed) {
        ena.setValue(speed);
    }

    @Override
    public double getSpeed() {
        return ena.getValue();
    }
}
