package com.curzar.androidkiosksample;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    private Button btn_call_second_activity;
    private Button btn_start_websocket_server;
    AsyncHttpServer httpServer = new AsyncHttpServer();
    List<WebSocket> _sockets = new ArrayList<WebSocket>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        //DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        //devicePolicyManager.clearDeviceOwnerApp(this.getPackageName());

        btn_start_websocket_server=(Button) findViewById(R.id.btn_start_websocket_server);

        btn_call_second_activity=(Button) findViewById(R.id.btn_call_second_activity);
        btn_call_second_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FirstActivity.this,SecondAcitivity.class);
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