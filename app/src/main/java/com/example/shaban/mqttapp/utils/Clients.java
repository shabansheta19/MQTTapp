package com.example.shaban.mqttapp.utils;

import com.example.shaban.mqttapp.modle.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shaban on 6/25/2018.
 */

public class Clients {

    private HashMap<String,Client> clients = new HashMap<>();
    private static Clients instance = new Clients();
    private String activeClientTag;

    //private constructor for singleton class
    private Clients() {
    }


    /**
     * function to create object of class.
     * @return instance which is the only one object of Clients class.
     */
    public static synchronized Clients getInstance() {
        if (instance == null)
            instance = new Clients();
        return instance;
    }


    public static void setInstance(Clients clients) {
        instance = clients;
    }


    /**
     * function to add new client.
     * @param client the client object that you want to add.
     */
    public void addClient(Client client) {
        clients.put(client.getClientTag(),client);
    }


    /**
     * function to return all clients
     * @return list of clients.
     */
    public List<Client> getClients() {
        return new ArrayList<Client>(clients.values());
    }


    /**
     * function to return specific client
     * @param clientTag unique attribute of client.
     * @return the client which match with tag attribute.
     */
    public Client getClient(String clientTag) {
        return clients.get(clientTag);
    }


    /**
     * function to remove client
     * @param client the client object that you want to remove.
     */
    public void removeClient(Client client) {
        clients.remove(client.getClientTag());
    }


    /**
     * function to return the client which show its details.
     * @return the client object.
     */
    public Client getActiveClient() {
        return clients.get(activeClientTag);
    }


    /**
     * function to set the active client object which show its details.
     * @param activeClient .
     */
    public void setActiveClient(Client activeClient) {
        this.activeClientTag = activeClient.getClientTag();
    }
}
