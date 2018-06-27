package com.example.shaban.mqttapp.clientConnectionStates;

/**
 * Created by shaban on 6/25/2018.
 */

public class None extends ClientConnectionState {

    public None() {
        super();
    }

    @Override
    public String toString() {
        return "Unknown connection status to ";
    }
}
