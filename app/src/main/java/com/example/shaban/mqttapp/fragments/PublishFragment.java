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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.control.MqttHelper;
import com.example.shaban.mqttapp.utils.ActivityConstants;

/**
 * Fragment for the publish message pane.
 *
 */
public class PublishFragment extends Fragment {

  private EditText topicEditText;
  private EditText messageEditText;
  private RadioGroup qosRadioGroup;
  private CheckBox retainedCheckBox;
  private Button publishBtn;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_publish, null);
    topicEditText = (EditText) view.findViewById(R.id.topicPublish);
    messageEditText = (EditText)view.findViewById(R.id.messagePublish);
    qosRadioGroup = (RadioGroup) view.findViewById(R.id.qosPublishRadio);
    retainedCheckBox = (CheckBox) view.findViewById(R.id.retainedPublish);
    publishBtn = (Button) view.findViewById(R.id.publishBtn);
    publishBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        publish();
      }
    });
    return view;
  }

  private void publish() {
    String topic = topicEditText.getText().toString();
    topicEditText.setText("");
    String message = messageEditText.getText().toString();
    messageEditText.setText("");
    int qos = getQos();
    boolean retained = retainedCheckBox.isChecked();
    MqttHelper.getInstance(getActivity()).publish(topic,message,qos,retained);
  }

  /**
   * the function return which radio button selected.
   * @return
   */
  private Integer getQos() {
    int checked = qosRadioGroup.getCheckedRadioButtonId();
    int qos = ActivityConstants.defaultQos;
    switch (checked) {
      case R.id.qos0Publish:
        qos = 0;
        break;
      case R.id.qos1Publish:
        qos = 1;
        break;
      case R.id.qos2Publish:
        qos = 2;
        break;
    }
    return qos;
  }

}
