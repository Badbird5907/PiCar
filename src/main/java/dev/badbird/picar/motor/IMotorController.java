package dev.badbird.picar.motor;

import dev.badbird.picar.object.MotorMovementState;
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

    void speed(double speed);

    double getSpeed();

    default double speed() {
        return getSpeed();
    }

    default void setSpeed(double speed) {
        speed(getSpeed());
    }

    default Map<MotorSide, MotorMovementState> getMovementStates() {
        Map<MotorSide, MotorMovementState> map = new HashMap<>();
        getMotors().forEach((side, motor) -> map.put(side, motor.getState().getMovementState()));
        return map;
    }

    default MovementDirection getDirection() {
        return direction.get();
    }
}

