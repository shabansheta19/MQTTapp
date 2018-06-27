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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shaban.mqttapp.CallbackBundle;
import com.example.shaban.mqttapp.R;
import com.example.shaban.mqttapp.control.MqttHelper;
import com.example.shaban.mqttapp.modle.Client;
import com.example.shaban.mqttapp.utils.ActivityConstants;
import com.example.shaban.mqttapp.utils.Clients;
import com.example.shaban.mqttapp.utils.Notify;
import com.example.shaban.mqttapp.utils.OpenFileDialog;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * Handles collection of user information to create a new MQTT Client
 *
 */
public class NewConnection extends Activity {

  private int openfileDialogId = 0;
  //new connection section widget views
  private Button connectBtn;
  private EditText clientIdEditText;
  private AutoCompleteTextView serverUriEditText;
  private EditText portEditText;
  private CheckBox cleanSessionCheckBox;

  //advanced settings section widget views
  private LinearLayout advancedSettingsSection;
  private EditText userNameEditText;
  private EditText passwordEditText;
  private CheckBox sslCheckBox;
  private EditText sslKeyLocationEditText;
  private Button sslKeyBtn;
  private EditText timeOutEditText;
  private EditText keepAliveEditText;

  //last will section widget views
  private RelativeLayout lastWillSection;
  private EditText topicEditText;
  private EditText messageEditText;
  private RadioGroup qosRadioGroup;
  private CheckBox retainedCheckBox;

  //check box controls hide/show advanced settings section
  private CheckBox advancedSettingsCheckBox;
  //check box controls hide/show last will section
  private CheckBox lastWillCheckBox;


  /**
   * create the UI of the activity
   * @param savedInstanceState
     */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_connection);
    initializeUI();
  }


  /**
   * bind the view widget objects with the view widgets at the layout.
   */
  private void initializeUI() {
    basicClientInformationUI();
    advancedSectionUI();
    lastWillSectionUI();
    controlCheckBoxes();
  }


  /**
   * bind the check boxes with the check boxes widget at the layout.
   * set checked change listener of check boxes.
   */
  private void controlCheckBoxes() {
    //control check boxes
    advancedSettingsCheckBox = (CheckBox) findViewById(R.id.advancedSettingsCheckBox);
    advancedSettingsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
          advancedSettingsSection.setVisibility(View.VISIBLE);
        } else {
          advancedSettingsSection.setVisibility(View.INVISIBLE);
        }
      }
    });

    lastWillCheckBox = (CheckBox) findViewById(R.id.lastWillCheckBox);
    lastWillCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
          lastWillSection.setVisibility(View.VISIBLE);
        } else {
          lastWillSection.setVisibility(View.INVISIBLE);
        }
      }
    });
  }


  /**
   * bind the view widget objects of the last will section
   * with the view widgets at the layout.
   */
  private void lastWillSectionUI() {
    //last will section UI
    lastWillSection = (RelativeLayout)findViewById(R.id.lastWillLayout);
    lastWillSection.setVisibility(View.INVISIBLE);
    topicEditText = (EditText) findViewById(R.id.lastWillTopicNewConnection);
    messageEditText = (EditText) findViewById(R.id.lastWillMessageNewConnection);
    qosRadioGroup = (RadioGroup) findViewById(R.id.qosRadioNewConnection);
    retainedCheckBox = (CheckBox) findViewById(R.id.retainedCheckBoxNewConnection);
  }


  /**
   * bind the view widget objects of the advanced settings
   * section with the view widgets at the layout.
   */
  private void advancedSectionUI() {
    //advanced section UI
    advancedSettingsSection = (LinearLayout) findViewById(R.id.advancedSettingsLayout);
    advancedSettingsSection.setVisibility(View.INVISIBLE);
    userNameEditText = (EditText) findViewById(R.id.unameNewConnection);
    passwordEditText = (EditText) findViewById(R.id.passwordNewConnection);
    sslCheckBox = (CheckBox) findViewById(R.id.sslCheckBoxNewConnection);
    sslCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          sslKeyBtn.setClickable(true);
        }else {
          sslKeyBtn.setClickable(false);
        }
      }
    });
    sslKeyLocationEditText = (EditText) findViewById(R.id.sslKeyLocationNewConnection);
    sslKeyBtn = (Button) findViewById(R.id.sslKeyButNewConnection);
    sslKeyBtn.setClickable(false);
    sslKeyBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //showFileChooser();
        showDialog(openfileDialogId);
      }
    });
    timeOutEditText = (EditText) findViewById(R.id.timeoutNewConnection);
    keepAliveEditText = (EditText) findViewById(R.id.keepaLiveNewConnection);
  }


  /**
   * bind the view widget objects of the basic client information section
   * with the view widgets at the layout.
   */
  private void basicClientInformationUI() {
    //basic information new connection section UI
    cleanSessionCheckBox = (CheckBox)findViewById(R.id.cleanSessionCheckBox);
    connectBtn = (Button)findViewById(R.id.connectButtonNewConnection);
    connectBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        connect();
      }
    });
    clientIdEditText = (EditText)findViewById(R.id.clientId);
    serverUriEditText = (AutoCompleteTextView)findViewById(R.id.serverURI);
    portEditText = (EditText)findViewById(R.id.port);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    adapter.addAll(readHosts());
    serverUriEditText.setAdapter(adapter);
  }


  /**
   * create the new client and establish the connection to mqtt.
   */
  private void connect() {
    //create new client object
    Client newClient = new Client(this);
    if (!setClientBasicInfo(newClient)) return;
    setClientAdvancedSettings(newClient);
    newClient.createMqttAndroidClient();
    setClientLastWill(newClient);
    // create a client tag
    String clientTag = newClient.getUri() + newClient.getClientId();
    newClient.setClientTag(clientTag);
    Clients.getInstance().addClient(newClient);
    Clients.getInstance().setActiveClient(newClient);
    MqttHelper.getInstance(this).connect();
    startActivity(new Intent(this,ClientConnections.class));
    finish();
  }


  /**
   * bind the widgets objects of last will section with the widgets views.
   * @param newClient
     */
  private void setClientLastWill(Client newClient) {
    // last will message
    String msg = messageEditText.getText().toString();
    String topic = topicEditText.getText().toString();
    Integer qos = getQos();
    Boolean retained = retainedCheckBox.isChecked();
    if ((!msg.equals(ActivityConstants.empty)) || (!topic.equals(ActivityConstants.empty))) {
      // need to make a message since last will is set
      try {
        newClient.getMqttConnectOptions().setWill(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
      }
      catch (Exception e) {
        Log.e(this.getClass().getCanonicalName(), "Exception Occured", e);
      }
    }
  }


  /**
   *
   * @param newClient
   * @return
     */
  private void setClientAdvancedSettings(Client newClient) {
    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    //the advanced settings information.
    boolean advancedSettings = advancedSettingsCheckBox.isChecked();
    boolean ssl = sslCheckBox.isChecked();
    String ssl_key = sslKeyLocationEditText.getText().toString();
    newClient.setSsl(ssl);
    newClient.setSsl_key(ssl_key);
    String uri = getUri(newClient.getServer(), newClient.getPort(), ssl);
    newClient.setUri(uri);
    if (ssl){
      try {
        if(advancedSettings && ssl_key != null && !ssl_key.equalsIgnoreCase("")) {
          FileInputStream key = new FileInputStream(ssl_key);
          mqttConnectOptions.setSocketFactory(newClient.getMqttAndroidClient().getSSLSocketFactory(key, "mqtttest"));
        }
      } catch (MqttSecurityException e) {
        Log.e(this.getClass().getCanonicalName(), "MqttException Occured: ", e);
      } catch (FileNotFoundException e) {
        Log.e(this.getClass().getCanonicalName(), "MqttException Occured: SSL Key file not found", e);
      }
    }
    // connection options
    String username = userNameEditText.getText().toString();
    String password = passwordEditText.getText().toString();

    int timeout = ActivityConstants.defaultTimeOut;
    int keepAlive = ActivityConstants.defaultKeepAlive;

    if(advancedSettings && !TextUtils.isEmpty(timeOutEditText.getText().toString()))
      timeout = Integer.parseInt(timeOutEditText.getText().toString());

    if(advancedSettings && !TextUtils.isEmpty(keepAliveEditText.getText().toString()))
      keepAlive = Integer.parseInt(keepAliveEditText.getText().toString());

    mqttConnectOptions.setCleanSession(newClient.isCleanSession());
    mqttConnectOptions.setConnectionTimeout(timeout);
    mqttConnectOptions.setKeepAliveInterval(keepAlive);

    if (advancedSettings && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
      mqttConnectOptions.setUserName(username);
      mqttConnectOptions.setPassword(password.toCharArray());
    }
    newClient.setMqttConnectOptions(mqttConnectOptions);
  }


  /**
   *
   * @param newClient
   * @return
     */
  private boolean setClientBasicInfo(Client newClient) {
    //extract client information
    String server = serverUriEditText.getText().toString();
    String port = portEditText.getText().toString();
    String clientId = clientIdEditText.getText().toString();

    if (server.equals(ActivityConstants.empty) || port.equals(ActivityConstants.empty) || clientId.equals(ActivityConstants.empty))
    {
      String notificationText = this.getString(R.string.missingOptions);
      Notify.toast(this, notificationText, Toast.LENGTH_LONG);
      return false;
    }

    boolean cleanSession = cleanSessionCheckBox.isChecked();
    //persist server
    persistServerURI(server);

    //set the new client with its attributes
    newClient.setServer(server);
    newClient.setPort(port);
    newClient.setClientId(clientId);
    newClient.setCleanSession(cleanSession);
    return true;
  }


  /**
   *
   * @param id
   * @return
     */
  @Override
  protected Dialog onCreateDialog(int id) {
    if (id == openfileDialogId) {
      Map<String, Integer> images = new HashMap<String, Integer>();
      images.put(OpenFileDialog.sRoot, R.drawable.ic_launcher);
      images.put(OpenFileDialog.sParent, R.drawable.ic_launcher);
      images.put(OpenFileDialog.sFolder, R.drawable.ic_launcher);
      images.put("bks", R.drawable.ic_launcher);
      images.put(OpenFileDialog.sEmpty, R.drawable.ic_launcher);
      Dialog dialog = OpenFileDialog.createDialog(id, this, "openfile",
              new CallbackBundle() {
                @Override
                public void callback(Bundle bundle) {
                  String filepath = bundle.getString("path");
                  // setTitle(filepath);
                  sslKeyLocationEditText.setText(filepath);
                }
              }, ".bks;", images);
      return dialog;
    }
    return null;
  }


  /**
   *
   * @param server
   * @param port
   * @param ssl
     * @return
     */
  @NonNull
  private String getUri(String server, String port, boolean ssl) {
    String uri;
    if (ssl) {
      Log.e("SSLConnection", "Doing an SSL Connect");
      uri = "ssl://";
    }
    else {
      uri = "tcp://";
    }
    uri = uri + server + ":" + port;
    return uri;
  }


  /**
   * the function return which radio button selected.
   * @return
     */
  private Integer getQos() {
    int checked = qosRadioGroup.getCheckedRadioButtonId();
    int qos = ActivityConstants.defaultQos;
    switch (checked) {
      case R.id.qos0NewConnection:
        qos = 0;
        break;
      case R.id.qos1NewConnection:
        qos = 1;
        break;
      case R.id.qos2NewConnection:
        qos = 2;
        break;
    }

    return qos;
  }


  /**
   * Add a server URI to the persisted file
   *
   * @param serverURI the uri to store
   */
  private void persistServerURI(String serverURI) {
    File fileDir = this.getFilesDir();
    File persisted = new File(fileDir, "hosts.txt");
    BufferedWriter bfw = null;
    try {
      bfw = new BufferedWriter(new FileWriter(persisted));
      bfw.write(serverURI);
      bfw.newLine();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (bfw != null) {
          bfw.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  /**
   * Read persisted hosts
   * @return The hosts contained in the persisted file
   */
  private String[] readHosts() {
    File fileDir = getFilesDir();
    File persisted = new File(fileDir, "hosts.txt");
    if (!persisted.exists()) {
      return new String[0];
    }
    ArrayList<String> hosts = new ArrayList<String>();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(persisted));
      String line = null;
      line = br.readLine();
      while (line != null) {
        hosts.add(line);
        line = br.readLine();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (br != null) {
          br.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    return hosts.toArray(new String[hosts.size()]);
  }
}
