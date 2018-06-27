package com.example.shaban.mqttapp.clientConnectionStates;

import com.example.shaban.mqttapp.activities.ClientConnections;

/**
 * Created by shaban on 6/25/2018.
 */

public abstract class ClientConnectionState {

    public ClientConnectionState() {
        //to reset the the information presented by the listView at ClientConnections activity.
        //ClientConnections.arrayAdapter.notifyDataSetChanged();
    }

    public String toString() {
        return "state ";
    }
}
