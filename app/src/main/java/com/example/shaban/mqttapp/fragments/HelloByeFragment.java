package com.example.shaban.mqttapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.control.MqttHelper;


public class HelloByeFragment extends Fragment {

    private Switch helloSwitch;
    private Switch byeSwitch;

    /**
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_hello_bye, null);
        helloSwitch = (Switch) view.findViewById(R.id.helloSwitch);
        byeSwitch = (Switch) view.findViewById(R.id.byeSwitch);

        helloSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    byeSwitch.setChecked(false);
                    MqttHelper.getInstance(getActivity()).publish("helloTopic","hello",0,true);
                }
            }
        });

        byeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    helloSwitch.setChecked(false);
                    MqttHelper.getInstance(getActivity()).publish("byeTopic","bye",0,true);
                }
            }
        });
        return view;
    }
}
