/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.example.shaban.mqttapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.control.MqttHelper;
import com.example.shaban.mqttapp.utils.ActivityConstants;

/**
 * Fragment for the subscribe pane for the client
 *
 */
public class SubscribeFragment extends Fragment {

  private EditText topicEditText;
  private RadioGroup qosRadioGroup;
  private Button subscribeButton;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_subscribe, null);

    topicEditText = (EditText) view.findViewById(R.id.topicSubscribe);
    qosRadioGroup = (RadioGroup) view.findViewById(R.id.qosSubscribeRadioGroup);
    subscribeButton = (Button) view.findViewById(R.id.subscribeButton);
    subscribeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        subscribe();
      }
    });
    return view;
  }

  private void subscribe() {
    String topic = topicEditText.getText().toString();
    topicEditText.setText("");
    int qos = getQos();
    MqttHelper.getInstance(getActivity()).subscribe(topic,qos);
  }

  /**
   * the function return which radio button selected.
   * @return
   */
  private Integer getQos() {
    int checked = qosRadioGroup.getCheckedRadioButtonId();
    int qos = ActivityConstants.defaultQos;
    switch (checked) {
      case R.id.qos0Subscribe:
        qos = 0;
        break;
      case R.id.qos1Subscribe:
        qos = 1;
        break;
      case R.id.qos2Subscribe:
        qos = 2;
        break;
    }

    return qos;
  }

}
