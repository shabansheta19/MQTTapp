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

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.shaban.mqttapp.clientConnectionStates.Connected;
import com.example.shaban.mqttapp.clientConnectionStates.Connecting;
import com.example.shaban.mqttapp.modle.Client;
import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.utils.Clients;

/**
 * ClientConnections is the main activity for the sample application, it
 * displays all the clients.
 * 
 */
public class ClientConnections extends ListActivity {

  /**
   * Token to pass to the MQTT Service
   */
  final static String TOKEN = "com.example.shaban.mqttapp.activities.NewConnection";
  // adapter to manage the listView.
  public static ArrayAdapter<Client> arrayAdapter = null;
  // instance object of the class.
  private ClientConnections clientConnections = this;
  //contextualActionBarActive contains delete button.
  private boolean contextualActionBarActive = false;
  private boolean logging;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Prefs.getInstance(this).getClients();
    //get the listView to show all clients.
    ListView connectionList = getListView();
    //set the ItemLongClickListener to show contextualActionBarActive;
    connectionList.setOnItemLongClickListener(new LongClickItemListener());
    connectionList.setTextFilterEnabled(true);
    arrayAdapter = new ArrayAdapter<Client>(this, R.layout.connection_text_view);
    setListAdapter(arrayAdapter);

    // get all the available connections
    List<Client> clients = Clients.getInstance().getClients();
    for (Client c : clients)
      arrayAdapter.add(c);

    arrayAdapter.notifyDataSetChanged();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    //load the correct menu depending on the status of logging
    if (logging) {
      getMenuInflater().inflate(R.menu.activity_connections_logging, menu);
    }
    else {
      getMenuInflater().inflate(R.menu.activity_connections, menu);
    }
    return true;
  }


  @Override
  protected void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);

    if (!contextualActionBarActive) {
      Client c = arrayAdapter.getItem(position);
      Clients.getInstance().setActiveClient(c);
      // start the connectionDetails activity to display the details about the
      // selected connection
      startActivity(new Intent(this,ConnectionDetails.class));
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == R.id.newConnection) {
      startActivity(new Intent(this,NewConnection.class));
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    Clients.getInstance().getActiveClient().getMqttAndroidClient().unregisterResources();
    Clients.getInstance().getActiveClient().getMqttAndroidClient().close();
    super.onDestroy();
  }

  private class LongClickItemListener implements OnItemLongClickListener, ActionMode.Callback, OnClickListener {

    /** The index of the item selected, or -1 if an item is not selected **/
    private int selected = -1;
    /** The view of the item selected **/
    private View selectedView = null;
    /** The connection the view is representing **/
    private Client client = null;

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      clientConnections.startActionMode(this);
      selected = position;
      selectedView = view;
      clientConnections.getListView().setSelection(position);
      view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
      return true;
    }

    /* (non-Javadoc)
     * @see android.view.ActionMode.Callback#onActionItemClicked(android.view.ActionMode, android.view.MenuItem)
     */
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
      selectedView.setBackgroundColor(getResources().getColor(android.R.color.white));
      switch (item.getItemId()) {
        case R.id.delete :
          delete();
          mode.finish();
          return true;
        default :
          return false;
      }
    }

    /* (non-Javadoc)
     * @see android.view.ActionMode.Callback#onCreateActionMode(android.view.ActionMode, android.view.Menu)
     */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      MenuInflater inflater = mode.getMenuInflater();
      inflater.inflate(R.menu.activity_client_connections_contextual, menu);
      clientConnections.contextualActionBarActive = true;
      return true;
    }

    /* (non-Javadoc)
     * @see android.view.ActionMode.Callback#onDestroyActionMode(android.view.ActionMode)
     */
    @Override
    public void onDestroyActionMode(ActionMode mode) {
      selected = -1;
      selectedView = null;

    }

    /* (non-Javadoc)
     * @see android.view.ActionMode.Callback#onPrepareActionMode(android.view.ActionMode, android.view.Menu)
     */
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return false;
    }

    /**
     * Deletes the connection, disconnecting if required.
     */
    private void delete()
    {
      client = arrayAdapter.getItem(selected);
      if (client.getState() == new Connecting() || client.getState() == new Connected()) {
        //display a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(clientConnections);
        builder.setTitle(R.string.disconnectClient)
            .setMessage(getString(R.string.deleteDialog))
            .setNegativeButton(R.string.cancelBtn, new OnClickListener() {

              @Override
              public void onClick(DialogInterface arg0, int arg1) {
                //do nothing user cancelled action
              }
            })
            .setPositiveButton(R.string.continueBtn, this)
            .show();
      }
      else {
        arrayAdapter.remove(client);
        Clients.getInstance().removeClient(client);
      }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      //user pressed continue disconnect client and delete
      try {
        client.getMqttAndroidClient().disconnect();
      }
      catch (MqttException e) {
        e.printStackTrace();
      }
      arrayAdapter.remove(client);
      Clients.getInstance().removeClient(client);

    }
  }
}

