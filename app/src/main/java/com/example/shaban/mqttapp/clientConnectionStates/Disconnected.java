package com.example.shaban.mqttapp.clientConnectionStates;

/**
 * Created by shaban on 6/25/2018.
 */

public class Disconnected extends ClientConnectionState {

    public Disconnected() {
        super();
    }

    @Override
    public String toString() {
        return "Disconnect from ";
    }
}
