package dev.badbird.picar.motor.impl.l298n;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import dev.badbird.picar.motor.IMotor;
import dev.badbird.picar.motor.MotorSide;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class L298nMotor implements IMotor {
    private GpioPinDigitalOutput pin1, pin2;
    private MotorSide side;

    @Override
    public void forward() {
        pin1.low();
        pin2.high();
    }

    @Override
    public void stop() {
        pin1.low();
        pin2.low();
    }

    @Override
    public void backward() {
        pin1.high();
        pin2.low();
    }
}
