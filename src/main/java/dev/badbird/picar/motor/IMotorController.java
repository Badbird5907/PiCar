package dev.badbird.picar.motor;

import dev.badbird.picar.object.MotorState;
import dev.badbird.picar.object.MovementDirection;
import dev.badbird.picar.platform.IPlatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public interface IMotorController<T extends IMotor> {
    AtomicReference<MovementDirection> direction = new AtomicReference<>(); // maybe we should make this abstract
    IPlatform getPlatform();
    void init();

    void cleanup();

    default void startForward() {
        getMotors().values().forEach(IMotor::forward);
        direction.set(MovementDirection.FORWARD);
    }

    default void startBackward() {
        getMotors().values().forEach(IMotor::backward);
        direction.set(MovementDirection.BACKWARD);
    }

    default void stop() {
        getMotors().values().forEach(IMotor::stop);
        direction.set(MovementDirection.STOPPED);
    }

    int getMotorCount();

    Optional<T> getMotor(MotorSide side);
    Map<MotorSide, T> getMotors();
    default List<T> getHalf(MotorSide.Half half) {
        return getMotors().values().stream().filter(motor -> motor.getSide().getHalf() == half).toList();
    }

    default void startLeft() {
        direction.set(MovementDirection.LEFT);
        getHalf(MotorSide.Half.LEFT).forEach(IMotor::backward);
        getHalf(MotorSide.Half.RIGHT).forEach(IMotor::forward);
    }

    default void startRight() {
        direction.set(MovementDirection.RIGHT);
        getHalf(MotorSide.Half.LEFT).forEach(IMotor::forward);
        getHalf(MotorSide.Half.RIGHT).forEach(IMotor::backward);
    }

    default void setSpeed(int speed) {
        getMotors().values().forEach(motor -> motor.setSpeed(speed));
    }

    default void setSpeed(MotorSide side, int speed) {
        getMotor(side).ifPresent(motor -> motor.setSpeed(speed));
    }


    default Map<MotorSide, MotorState> getMotorStates() {
        Map<MotorSide, MotorState> map = new HashMap<>();
        getMotors().forEach((side, motor) -> map.put(side, motor.getState()));
        return map;
    }

    default MovementDirection getDirection() {
        return direction.get();
    }
}

