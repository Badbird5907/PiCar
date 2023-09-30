package dev.badbird.picar.system.impl;

import dev.badbird.picar.system.IPlatform;

public class NoOpPlatform implements IPlatform {
    private boolean ledState = false;

    @Override
    public void init() {

    }

    @Override
    public void setLedState(boolean state) {
        System.out.println("LED state set to " + state);
        ledState = state;
    }

    @Override
    public boolean getLedState() {
        return ledState;
    }
}
