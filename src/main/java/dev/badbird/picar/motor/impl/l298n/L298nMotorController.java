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
            in1 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_25, "IN1", PinState.LOW);
            in2 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_23, "IN2", PinState.LOW);
            in3 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_24, "IN3", PinState.LOW);
            in4 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_22, "IN4", PinState.LOW);
            ena = GpioFactory.getInstance().provisionPwmOutputPin(RaspiPin.GPIO_01, "ENA",  L298nMotorController.calculateSpeed(50));
            enb = GpioFactory.getInstance().provisionPwmOutputPin(RaspiPin.GPIO_26, "ENB",  L298nMotorController.calculateSpeed(50));

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

    public static int calculateSpeed(int percentage) {
        if (percentage <= 0) {
            return 0;
        }
        int diff = speedBoundMax - speedBoundMin;
        return (int) (speedBoundMin + (diff * (percentage / 100.0)));
    }

    public static int pwmSpeedToPercentage(int pwm) {
        int diff = speedBoundMax - speedBoundMin;
        return (int) (((pwm - speedBoundMin) / (double) diff) * 100);
    }
    /*
    @Override
    public void startForward() {
        // IMotorController.super.startForward();
        in1.high();
        in2.low();
        in3.high();
        in4.low();
    }

    @Override
    public void startLeft() {
        in1.low();
        in2.high();
        in3.high();
        in4.low();
    }

    @Override
    public void startBackward() {
        // IMotorController.super.startBackward();
        in1.low();
        in2.high();
        in3.low();
        in4.high();
    }

    @Override
    public void startRight() {
        in1.high();
        in2.low();
        in3.low();
        in4.high();
    }

    @Override
    public void stop() {
        in1.low();
        in2.low();
        in3.low();
        in4.low();
    }
     */
}
