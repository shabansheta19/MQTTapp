package com.example.shaban.mqttapp.control;

import android.content.Context;
import android.widget.Toast;

import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.clientConnectionStates.Connected;
import com.example.shaban.mqttapp.clientConnectionStates.Connecting;
import com.example.shaban.mqttapp.clientConnectionStates.Disconnected;
import com.example.shaban.mqttapp.clientConnectionStates.Error;
import com.example.shaban.mqttapp.fragments.HistoryFragment;
import com.example.shaban.mqttapp.utils.Clients;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shaban on 6/26/2018.
 */

public class MqttHelper {

    private boolean process = true;
    private static Context context;
    private final static MqttHelper instance = new MqttHelper();

    //private constructor for singleton class.
    private MqttHelper(){
    }

    /**
     * function to return the only object of class.
     * @param cxt .
     * @return instance only object of class.
     */
    public static MqttHelper getInstance(Context cxt) {
        context = cxt;
        return instance;
    }


    /**
     * function to establish an MQTT-connection
     * @return true if connection success and false if connection failure.
     */
    public boolean connect() {
        //set the client state to connecting.
        Clients.getInstance().getActiveClient().setState(new Connecting());
        MqttAndroidClient mqttAndroidClient = Clients.getInstance().getActiveClient().getMqttAndroidClient();
        try {
            mqttAndroidClient.connect(Clients.getInstance().getActiveClient().getMqttConnectOptions(), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // client is connected.
                    Clients.getInstance().getActiveClient().setState(new Connected()); //change the state of client to connected
                    String str = "Client Connected" + getTimeStamp(); //history string of connected action.
                    Clients.getInstance().getActiveClient().getHistory().add(str);
                    HistoryFragment.refresh();
                    process = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Clients.getInstance().getActiveClient().setState(new Error());
                    process = false;
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            process = false;
        }
        return process;
    }


    /**
     * Disconnect the client
     */
    public boolean disconnect() {
        MqttAndroidClient mqttAndroidClient = Clients.getInstance().getActiveClient().getMqttAndroidClient();
        //if the client is not connected, process the disconnect
        if (Clients.getInstance().getActiveClient().getState() == new Disconnected()) {
            return true;
        }

        try {
            mqttAndroidClient.disconnect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    process = true;
                    String str = "Client Disconnected" + getTimeStamp();
                    Clients.getInstance().getActiveClient().getHistory().add(str);
                    HistoryFragment.refresh();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context,"Failed to disconnect the client",Toast.LENGTH_LONG).show();
                    process = false;
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(context,"Failed to disconnect the client",Toast.LENGTH_LONG).show();
            process = false;
        }
        return process;
    }



    /**
     * Subscribe to a topic that the user has specified
     */
    public boolean subscribe(final String topic, final int qos) {
        final MqttAndroidClient mqttAndroidClient = Clients.getInstance().getActiveClient().getMqttAndroidClient();
        IMqttToken token = null;
        try {
            token = mqttAndroidClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
            process = false;
        }
        token.setActionCallback(new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                // The message was published
                Toast.makeText(context,"Subscribe Successfully ", Toast.LENGTH_LONG);
                process = true;
                //history string for publish message action
                String str = "Subscribe: MQTT so fresh.topic:" + topic + ";qos:" + qos + getTimeStamp();
                Clients.getInstance().getActiveClient().getHistory().add(str);
                HistoryFragment.refresh();
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                Toast.makeText(context,"Subscribe Failed ", Toast.LENGTH_LONG);
                process = false;
            }
        });
        return true;
    }



    /**
     * Publish the message the user has specified
     */
    public boolean publish(final String topic , String message , final int qos , final boolean retained) {
        MqttAndroidClient mqttAndroidClient = Clients.getInstance().getActiveClient().getMqttAndroidClient();
        try {
            mqttAndroidClient.publish(topic, message.getBytes(), qos, retained, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //the message published successfully.
                    process = true;
                    //history string for publish message action
                    String str = "Published message: MQTT so fresh. to topic:" + topic + ";qos:" + qos + ";retained:" + retained + getTimeStamp();
                    Clients.getInstance().getActiveClient().getHistory().add(str);
                    HistoryFragment.refresh();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong.
                    Toast.makeText(context, "Failed to publish a message from the client",Toast.LENGTH_LONG).show();
                    process = false;
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to publish a message from the client",Toast.LENGTH_LONG).show();
            process = false;
        }
        return process;
    }


    /**
     * function to get the date and time of the process(publish
     *  ,subscribe,connect,disconnect).
     * @return string of date with time.
     */
    public String getTimeStamp() {
        Object[] args = new String[1];
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.dateFormat));
        args[0] = sdf.format(new Date());

        String timestamp = context.getString(R.string.timestamp, args);
        return "\n"+timestamp;
    }

}
