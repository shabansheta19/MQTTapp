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
package com.example.shaban.mqttapp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shaban.mqttapp.adapters.ViewPagerAdapter;
import com.example.shaban.mqttapp.control.MqttHelper;
import com.example.shaban.mqttapp.fragments.ComingMessageFragment;
import com.example.shaban.mqttapp.fragments.HelloByeFragment;
import com.example.shaban.mqttapp.fragments.HistoryFragment;
import com.example.shaban.mqttapp.fragments.PublishFragment;
import com.example.shaban.mqttapp.fragments.SubscribeFragment;
import com.example.shaban.mqttapp.modle.Client;
import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.utils.Clients;

public class ConnectionDetails extends AppCompatActivity {

  //instance object of class.
  private final ConnectionDetails connectionDetails = this;
  //active client.
  private Client client = null;
  //layout contains tabs.
  private TabLayout tabLayout;
  //container of tabs fragments.
  private ViewPager viewPager;
  //button to connect the active client to mqtt.
  private Button disconnectBtn;
  //button to disconnect the active client.
  private Button connectBtn;
  //textView to show the active client id.
  private TextView clientTxtView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_connection_details);
    //assign the client object with the active client.
    client = Clients.getInstance().getActiveClient();
    //toolbar contain the active client id and connect/disconnect button.
    Toolbar toolbar = (Toolbar)findViewById(R.id.connection_details_toolbar);
    setSupportActionBar(toolbar);
    //bind the widgets objects with the widgets views at the layout.
    clientTxtView = (TextView)findViewById(R.id.connection_details_client);
    clientTxtView.setText(client.getClientId());
    connectBtn = (Button)findViewById(R.id.connection_details_connect_btn);
    connectBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (MqttHelper.getInstance(connectionDetails).connect()) {
            disconnectBtn.setVisibility(View.VISIBLE);
            connectBtn.setVisibility(View.INVISIBLE);
          }
        }
    });
    disconnectBtn = (Button) findViewById(R.id.connection_details_disconnect_btn);
    disconnectBtn.setVisibility(View.INVISIBLE);
    disconnectBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (MqttHelper.getInstance(connectionDetails).disconnect()) {
          connectBtn.setVisibility(View.VISIBLE);
          disconnectBtn.setVisibility(View.INVISIBLE);
        }
      }
    });
    viewPager = (ViewPager) findViewById(R.id.connection_details_viewpager);
    setupViewPager(viewPager);
    tabLayout = (TabLayout) findViewById(R.id.connection_details_tabs);
    tabLayout.setupWithViewPager(viewPager);
  }

  /**
   * add tabs fragments and its titles to view pager adapter.
   * @param viewPager .
     */
  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(new HistoryFragment(),"History");
    adapter.addFragment(new PublishFragment(),"Publish");
    adapter.addFragment(new SubscribeFragment(),"Subscribe");
    adapter.addFragment(new HelloByeFragment(),"Hello & Bye");
    adapter.addFragment(new ComingMessageFragment(),"Arrived Message");
    viewPager.setAdapter(adapter);
  }

  @Override
  protected void onDestroy() {
    Clients.getInstance().getActiveClient().getMqttAndroidClient().unregisterResources();
    Clients.getInstance().getActiveClient().getMqttAndroidClient().close();
    super.onDestroy();
  }
}
