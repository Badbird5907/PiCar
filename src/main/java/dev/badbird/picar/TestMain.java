package dev.badbird.picar;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import dev.badbird.picar.motor.impl.l298n.L298nMotorController;
import dev.badbird.picar.platform.impl.pi.BCM;

import java.util.Scanner;

public class TestMain {
    private static GpioPinDigitalOutput in1 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_25, "IN1", PinState.LOW);
    private static GpioPinDigitalOutput in2 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_23, "IN2", PinState.LOW);
    private static GpioPinDigitalOutput in3 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_24, "IN3", PinState.LOW);
    private static GpioPinDigitalOutput in4 = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_22, "IN4", PinState.LOW);
    private static GpioPinPwmOutput ena = GpioFactory.getInstance().provisionPwmOutputPin(RaspiPin.GPIO_01, "ENA",  L298nMotorController.calculateSpeed(50));
    private static GpioPinPwmOutput enb = GpioFactory.getInstance().provisionPwmOutputPin(RaspiPin.GPIO_26, "ENB",  L298nMotorController.calculateSpeed(50));

    public static void main(String[] args) {
        int i = Gpio.wiringPiSetup();
        if (i == -1) {
            System.out.println("GPIO SETUP FAILED");
            return;
        }
        // ena.setPwmRange(1000);
        ena.setPwm(0);
        // enb.setPwmRange(1000);
        enb.setPwm(0);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            clearConsole();
            System.out.println("Motor Control");
            System.out.println("1 - Forward Motor 1");
            System.out.println("2 - Backward Motor 1");
            System.out.println("3 - Stop Motor 1");
            System.out.println("4 - Forward Motor 2");
            System.out.println("5 - Backward Motor 2");
            System.out.println("6 - Stop Motor 2");
            System.out.println("7 - Stop All Motors");
            System.out.println("8 - Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter speed (250-600) for Motor 1: ");
                    int speed1 = scanner.nextInt();
                    controlMotor1(speed1, true);
                    break;
                case 2:
                    System.out.print("Enter speed (250-600) for Motor 1: ");
                    int speed2 = scanner.nextInt();
                    controlMotor1(speed2, false);
                    break;
                case 3:
                    controlMotor1(0, false, true);
                    break;
                case 4:
                    System.out.print("Enter speed (250-600) for Motor 2: ");
                    int speed3 = scanner.nextInt();
                    controlMotor2(speed3, true);
                    break;
                case 5:
                    System.out.print("Enter speed (250-600) for Motor 2: ");
                    int speed4 = scanner.nextInt();
                    controlMotor2(speed4, false);
                    break;
                case 6:
                    controlMotor2(0, false, true);
                    break;
                case 7:
                    controlMotor1(0, false, true);
                    controlMotor2(0, false, true);
                    break;
                case 8:
                    controlMotor1(0, false, true);
                    controlMotor2(0, false, true);
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void controlMotor1(int speed, boolean isForward, boolean... stop) {
        if (stop != null && stop.length > 0 && stop[0]) {
            in1.low();
            in2.low();
        } else {
            in1.setState(isForward);
            in2.setState(!isForward);
            ena.setPwm(L298nMotorController.calculateSpeed(speed));
        }
    }

    private static void controlMotor2(int speed, boolean isForward, boolean... stop) {
        if (stop != null && stop.length > 0 && stop[0]) {
            in3.low();
            in4.low();
        } else {
            in3.setState(isForward);
            in4.setState(!isForward);
            enb.setPwm(L298nMotorController.calculateSpeed(speed));
        }
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
