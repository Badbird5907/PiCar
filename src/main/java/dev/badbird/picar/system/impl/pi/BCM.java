package dev.badbird.picar.system.impl.pi;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class BCM { // For the pi 4b - https://pi4j.com/1.4/pins/rpi-4b.html
    public static Pin GPIO_03 = RaspiPin.GPIO_08,
            GPIO_05 = RaspiPin.GPIO_09, GPIO_07 = RaspiPin.GPIO_07,
            GPIO_08 = RaspiPin.GPIO_15, GPIO_10 = RaspiPin.GPIO_16,
            GPIO_11 = RaspiPin.GPIO_00, GPIO_12 = RaspiPin.GPIO_01,
            GPIO_13 = RaspiPin.GPIO_02, GPIO_15 = RaspiPin.GPIO_03,
            GPIO_16 = RaspiPin.GPIO_04, GPIO_18 = RaspiPin.GPIO_05,
            GPIO_19 = RaspiPin.GPIO_12, GPIO_21 = RaspiPin.GPIO_13,
            GPIO_22 = RaspiPin.GPIO_06, GPIO_23 = RaspiPin.GPIO_14,
            GPIO_24 = RaspiPin.GPIO_10, GPIO_26 = RaspiPin.GPIO_11,
            GPIO_27 = RaspiPin.GPIO_30, GPIO_28 = RaspiPin.GPIO_31,
            GPIO_29 = RaspiPin.GPIO_21, GPIO_31 = RaspiPin.GPIO_22,
            GPIO_32 = RaspiPin.GPIO_26, GPIO_33 = RaspiPin.GPIO_23,
            GPIO_35 = RaspiPin.GPIO_24, GPIO_36 = RaspiPin.GPIO_27,
            GPIO_37 = RaspiPin.GPIO_25, GPIO_38 = RaspiPin.GPIO_28,
            GPIO_40 = RaspiPin.GPIO_29;
}
