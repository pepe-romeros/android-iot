package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.app.web.NamesHandler;
import com.example.app.web.RootHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName() + "_TAG"; // Logging TAG
    private static final int SERVER_PORT = 5000; // HTTP Service's port.

    private HttpServer httpServer; // Holds our Http Service instance.
    private NamesHandler namesHandler; // Variable needed to release used resources when done.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // 1 create the Http Service
            httpServer = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
            httpServer.setExecutor(Executors.newCachedThreadPool());
            // 2 define the endpoints
            httpServer.createContext(RootHandler.ENDPOINT, new RootHandler(getAssets()));
            namesHandler = new NamesHandler(getApplicationContext());
            httpServer.createContext(NamesHandler.ENDPOINT, namesHandler);
            // 3 start service
            httpServer.start();
            Log.d(TAG, "onCreate: Server started at " + httpServer.getAddress().toString());
        } catch (IOException ioe) {
            Log.e(TAG, "onCreate: ", ioe);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (httpServer != null) {
            httpServer.stop(0);
            namesHandler.tearDown();
        }
    }
}
