package com.example.shaban.mqttapp.clientConnectionStates;

/**
 * Created by shaban on 6/25/2018.
 */

public class Connected extends ClientConnectionState {

    public Connected() {
        super();
    }

    @Override
    public String toString() {
        return "Connect to ";
    }
}
