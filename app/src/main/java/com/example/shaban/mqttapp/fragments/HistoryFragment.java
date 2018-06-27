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
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.modle.Client;
import com.example.shaban.mqttapp.utils.Clients;

/**
 * This fragment displays the history information for a client
 *
 */
public class HistoryFragment extends ListFragment {

  /**
   * {@link ArrayAdapter} to display the formatted text
   **/
  static ArrayAdapter<String> arrayAdapter = null;

  /**
   * @see ListFragment#onCreate(Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Client client = Clients.getInstance().getActiveClient();
    //Initialise the arrayAdapter, view and add data
    arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view_text_view);
    arrayAdapter.addAll(client.getHistory());
    setListAdapter(arrayAdapter);
    arrayAdapter.notifyDataSetChanged();
  }

  /**
   * Updates the data displayed to match the current history
   */
  public static void refresh() {
    if (arrayAdapter != null) {
      Client client = Clients.getInstance().getActiveClient();
      arrayAdapter.clear();
      arrayAdapter.addAll(client.getHistory());
      arrayAdapter.notifyDataSetChanged();
    }
  }
}
