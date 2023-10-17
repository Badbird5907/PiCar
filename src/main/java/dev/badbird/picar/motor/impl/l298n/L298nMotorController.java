package dev.badbird.picar.motor.impl.l298n;

import com.pi4j.io.gpio.*;
import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.motor.MotorSide;
import dev.badbird.picar.platform.IPlatform;
import dev.badbird.picar.platform.impl.pi.BCM;
import dev.badbird.picar.platform.impl.pi.PiPlatform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
public class L298nMotorController implements IMotorController<L298nMotor> {
    private static final int speedBoundMin = 250, speedBoundMax = 600;
    private GpioController controller;
    private GpioPinDigitalOutput in1, in2, in3, in4;
    private GpioPinPwmOutput ena, enb;
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
            in1 = controller.provisionDigitalOutputPin(BCM.GPIO_26, "IN1", PinState.LOW);
            in2 = controller.provisionDigitalOutputPin(BCM.GPIO_13, "IN2", PinState.LOW);
            in3 = controller.provisionDigitalOutputPin(BCM.GPIO_19, "IN3", PinState.LOW);
            in4 = controller.provisionDigitalOutputPin(BCM.GPIO_06, "IN4", PinState.LOW);

            ena = controller.provisionPwmOutputPin(RaspiPin.GPIO_01, "ENA", calculateSpeed(50));
            enb = controller.provisionPwmOutputPin(RaspiPin.GPIO_26, "ENB", calculateSpeed(50));

            in1.setShutdownOptions(true, PinState.LOW);
            in2.setShutdownOptions(true, PinState.LOW);
            in3.setShutdownOptions(true, PinState.LOW);
            in4.setShutdownOptions(true, PinState.LOW);

            motors = new HashMap<>();
            motors.put(MotorSide.TOP_LEFT, new L298nMotor(in1, in2, ena, MotorSide.TOP_LEFT));
            motors.put(MotorSide.TOP_RIGHT, new L298nMotor(in3, in4, enb, MotorSide.TOP_RIGHT));

            // initial state - turn off motors
            in1.low();
            in2.low();
            in3.low();
            in4.low();
        } else {
            throw new IllegalStateException("Platform is not a PiPlatform!");
        }

        log.info("[DEBUG] 100: {}", calculateSpeed(100));
        log.info("[DEBUG] 75: {}", calculateSpeed(75));
        log.info("[DEBUG] 50: {}", calculateSpeed(50));
        log.info("[DEBUG] 25: {}", calculateSpeed(25));
        log.info("[DEBUG] 1: {}", calculateSpeed(1));
        log.info("[DEBUG] 0: {}", calculateSpeed(0));
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

    protected static int calculateSpeed(int percentage) {
        if (percentage <= 0) {
            return 0;
        }
        int diff = speedBoundMax - speedBoundMin;
        return (int) (speedBoundMin + (diff * (percentage / 100.0)));
    }

    protected static int pwmSpeedToPercentage(int pwm) {
        int diff = speedBoundMax - speedBoundMin;
        return (int) (((pwm - speedBoundMin) / (double) diff) * 100);
    }

    @Override
    public void startForward() {
        IMotorController.super.startForward();
    }

    @Override
    public void startLeft() {
        IMotorController.super.startLeft();
    }

    @Override
    public void startBackward() {
        IMotorController.super.startBackward();
    }

    @Override
    public void startRight() {
        IMotorController.super.startRight();
    }

    @Override
    public void stop() {
        IMotorController.super.stop();
    }
}
