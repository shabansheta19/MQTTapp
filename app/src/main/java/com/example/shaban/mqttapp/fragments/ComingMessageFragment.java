package com.example.shaban.mqttapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.modle.Client;
import com.example.shaban.mqttapp.utils.Clients;

public class ComingMessageFragment extends Fragment {

    private ListView listView;
    private static ArrayAdapter<String> arrayAdapter;
    private Client client;

    /**
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        client = Clients.getInstance().getActiveClient();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_coming_message, null);
        listView = (ListView) view.findViewById(R.id.messagesListView);
        arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_view_text_view);
        arrayAdapter.addAll(client.getArrivedMessages());
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        return view;
    }


    /**
     * Updates the data displayed to match the current history
     */
    public static void refresh() {
        if (arrayAdapter != null) {
            Client client = Clients.getInstance().getActiveClient();
            arrayAdapter.clear();
            arrayAdapter.addAll(client.getArrivedMessages());
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
