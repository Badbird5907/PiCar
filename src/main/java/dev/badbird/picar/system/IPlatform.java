package dev.badbird.picar.system;

public interface IPlatform {
    void init();
    void setLedState(boolean state);
    boolean getLedState();
}
