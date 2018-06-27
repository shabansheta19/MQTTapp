package com.example.shaban.mqttapp.clientConnectionStates;

/**
 * Created by shaban on 6/25/2018.
 */

public class Disconnecting extends ClientConnectionState {

    public Disconnecting() {
        super();
    }

    @Override
    public String toString() {
        return "Disconnecting from ";
    }
}
