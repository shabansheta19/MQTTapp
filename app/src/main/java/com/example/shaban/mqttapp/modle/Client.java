package com.example.shaban.mqttapp.modle;

import android.content.Context;

import com.example.shaban.mqttapp.clientConnectionStates.ClientConnectionState;
import com.example.shaban.mqttapp.clientConnectionStates.Disconnected;
import com.example.shaban.mqttapp.control.MqttHelper;
import com.example.shaban.mqttapp.fragments.ComingMessageFragment;
import com.example.shaban.mqttapp.fragments.HistoryFragment;
import com.example.shaban.mqttapp.utils.Clients;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaban on 6/25/2018.
 */

public class Client {

    private Context context;
    /*
     *The basic client information
     */
    private String clientTag; //unique attribute to specify the client.
    private String clientId;
    private String server;
    private String port;
    private boolean cleanSession;
    private boolean ssl;
    private String ssl_key;
    private String uri;
    private ClientConnectionState state;
    private MqttAndroidClient mqttAndroidClient = null;
    private MqttConnectOptions mqttConnectOptions;
    private String lastWillMessage;
    private List<String> history;
    private List<String> arrivedMessages;

    //constructor
    public Client(Context context) {
        this.context = context;
        history = new ArrayList<>();
        arrivedMessages = new ArrayList<>();
    }


    /**
     * A string representing the state of the client this connection
     * object represents.
     *
     * @return A string representing the state of the client
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(clientId);
        sb.append("\n ");
        sb.append(state.toString());
        sb.append(" ");
        sb.append(server);

        return sb.toString();
    }


    /**
     * Creates an instance of an Android MQTT client,
     * that will bind to the Paho Android Service
     */
    public void createMqttAndroidClient() {
        if (mqttAndroidClient == null) {
            mqttAndroidClient = new MqttAndroidClient(context, uri, clientId);
            mqttAndroidClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Clients.getInstance().getActiveClient().setState(new Disconnected());
                    String str = "Connection Lost" + MqttHelper.getInstance(context).getTimeStamp();
                    history.add(str);
                    HistoryFragment.refresh();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String str = "Message Arrived: MQTT so fresh. ";
                    String c = "topic:" + topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
                    String t = MqttHelper.getInstance(context).getTimeStamp();
                    str += c + t;
                    history.add(str);
                    arrivedMessages.add("message:" + message.toString() + c + t);
                    ComingMessageFragment.refresh();
                    HistoryFragment.refresh();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }
    }


    //getter and setter for the basic client information
    public String getClientTag() {
        return clientTag;
    }

    public void setClientTag(String clientTag) {
        this.clientTag = clientTag;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getSsl_key() {
        return ssl_key;
    }

    public void setSsl_key(String ssl_key) {
        this.ssl_key = ssl_key;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ClientConnectionState getState() {
        return state;
    }

    public void setState(ClientConnectionState state) {
        this.state = state;
    }

    public MqttAndroidClient getMqttAndroidClient() {
        return mqttAndroidClient;
    }

    public void setMqttAndroidClient(MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient = mqttAndroidClient;
    }

    public MqttConnectOptions getMqttConnectOptions() {
        return mqttConnectOptions;
    }

    public void setMqttConnectOptions(MqttConnectOptions mqttConnectOptions) {
        this.mqttConnectOptions = mqttConnectOptions;
    }

    public String getLastWillMessage() {
        return lastWillMessage;
    }

    public void setLastWillMessage(String lastWillMessage) {
        this.lastWillMessage = lastWillMessage;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public List<String> getArrivedMessages() {
        return arrivedMessages;
    }

    public void setArrivedMessages(List<String> arrivedMessages) {
        this.arrivedMessages = arrivedMessages;
    }
}
