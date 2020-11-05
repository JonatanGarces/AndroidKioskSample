package com.curzar.androidkiosksample;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.curzar.androidkiosksample.ble.DeviceControlActivity;
import com.curzar.androidkiosksample.ble.DeviceScanActivity;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import java.util.ArrayList;
import java.util.List;

public class Activity2Settings extends AppCompatActivity {

    private Button btn_call_second_activity;
    private Button btn_start_websocket_server;
    AsyncHttpServer httpServer = new AsyncHttpServer();
    List<WebSocket> _sockets = new ArrayList<WebSocket>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2settings);


        Spinner spinner = (Spinner) findViewById(R.id.spinner_typeofchange);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        //devicePolicyManager.clearDeviceOwnerApp(this.getPackageName());
        btn_start_websocket_server=(Button) findViewById(R.id.btn_start_websocket_server);
        btn_call_second_activity=(Button) findViewById(R.id.btn_call_second_activity);
        btn_call_second_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Activity2Settings.this, DeviceScanActivity.class);
                startActivity(intent);
            }
        });


        btn_start_websocket_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                httpServer.listen(AsyncServer.getDefault(), 1414);
                Log.d("titulo", Utils.getIPAddress(true));

               //// Utils.getMACAddress("wlan0");
                //Utils.getMACAddress("eth0");
               // Utils.getIPAddress(false); // IPv6

                httpServer.websocket("/live", new AsyncHttpServer.WebSocketRequestCallback() {
                    @Override
                    public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                        _sockets.add(webSocket);
                        //Use this to clean up any references to your websocket
                        webSocket.setClosedCallback(new CompletedCallback() {
                            @Override
                            public void onCompleted(Exception ex) {
                                try {
                                    if (ex != null)
                                        Log.e("WebSocket", "An error occurred", ex);
                                } finally {
                                    _sockets.remove(webSocket);
                                }
                            }
                        });

                        webSocket.setStringCallback(new WebSocket.StringCallback() {
                            @Override
                            public void onStringAvailable(String s) {
                                if ("Hello Server".equals(s))
                                    webSocket.send("Welcome Client!");
                            }
                        });

                    }
                });


            }
        });
    }
}