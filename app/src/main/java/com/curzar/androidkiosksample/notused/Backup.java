package com.curzar.androidkiosksample.notused;

import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import java.util.ArrayList;
import java.util.List;

public class Backup {
    AsyncHttpServer httpServer = new AsyncHttpServer();
    List<WebSocket> _sockets = new ArrayList<WebSocket>();

    /*
     httpServer.listen(AsyncServer.getDefault(), 1414);
                Log.d("titulo", Utils.getIPAddress(true));
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

     */
}
