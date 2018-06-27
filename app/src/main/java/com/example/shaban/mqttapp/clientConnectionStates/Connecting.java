package com.example.shaban.mqttapp.clientConnectionStates;

/**
 * Created by shaban on 6/25/2018.
 */

public class Connecting extends ClientConnectionState {

    public Connecting() {
        super();
    }

    @Override
    public String toString() {
        return "Connecting to ";
    }
}
