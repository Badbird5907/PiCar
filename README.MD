# PiCar

An over-engineered Raspberry Pi Powered car with a web ui using Java, React, and JavaScript (TS)

## Stack
- Java
- [React](https://react.dev/)
- [NextUI](https://nextui.org/)
- [Javalin](https://javalin.io/)
- [Pi4J 1.4](httpshttps://pi4j.com/1.4/) (v2 doesn't work for some reason)
- [Raspberry Pi 4b](https://www.raspberrypi.org/products/raspberry-pi-4-model-b/)
- L298N Motor Driver

## Setup

To build: `./gradlew build`:
This will build the React frontend in `src/main/frontend` and include it into a executable jar file.

Note: To save time, most dependencies aren't bundled with the jar.
Use [JarLoader](http://github.com/Badbird5907/JarLoader) to load the library jars at runtime.

## Usage

1. Execute the server with `java -javaagent:JarLoader.jar -jar PiCar.jar`
2. The server will bind to 0.0.0.0:1337, just go to [raspberrypi.local:1337](http://raspberrypi.local:1337/)
3. Change endpoint URLs as needed in settings (top right)
4. Use W/A/S/D or the buttons to control the car