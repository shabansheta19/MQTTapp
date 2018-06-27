package com.example.shaban.mqttapp.clientConnectionStates;

/**
 * Created by shaban on 6/25/2018.
 */

public class Error extends ClientConnectionState {

    public Error() {
        super();
    }

    @Override
    public String toString() {
        return "An error occurred connecting to ";
    }
}
