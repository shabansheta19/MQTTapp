package com.example.shaban.mqttapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shaban on 6/27/2018.
 */

public class Prefs {

    private static Prefs instance;
    private final static String KEY = "CLIENTS";
    private static Context context;
    private SharedPreferences prefs;

    private Prefs() {
    }


    public static Prefs getInstance(Context cxt) {
        context = cxt;
        if (instance == null)
            instance = new Prefs();
        return instance;
    }


    public void saveClients() {
    /*
        prefs = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        //save the client list to preference
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Clients.getInstance().getClients());
        Log.d("json = ",json);
        prefsEditor.putString(KEY, json);
        prefsEditor.commit();
    */
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "mqtt", MODE_PRIVATE);;
        complexPreferences.putObject(KEY, Clients.getInstance());
        complexPreferences.commit();
    }


    public void getClients() {
    /*
        prefs = context.getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY, "");
        List<Client> clients ;
        clients = gson.fromJson(json, new TypeToken<List<Client>>(){}.getType());
        if (clients == null)
            clients = new ArrayList<>();
        for (int i = 0 ; i < clients.size() ; i++)
        Clients.getInstance().addClient(clients.get(i));
    */
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "mqtt", MODE_PRIVATE);
        Clients clients = complexPreferences.getObject(KEY, Clients.class);
        Clients.setInstance(clients);
    }
}
