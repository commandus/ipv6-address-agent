package com.commandus.ipv6addressagent.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.commandus.utilipv6.Address;

public class IPV6AddressAgentService extends Service {
    private static final String TAG = IPV6AddressAgentService.class.getSimpleName();
    private String ipv6Address;

    public IPV6AddressAgentService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void init() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    setIPv6Address(Address.getIPv6GlobalAddress());
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onAvailable(network);
                    setIPv6Address(Address.getIPv6GlobalAddress());
                }
            });
        } else {
            this.registerReceiver(new NetworkChangeReceiver(), null);
        }
    }

    private void setIPv6Address(String value) {
        ipv6Address = value;
        if (value.isEmpty()) {
            return;
        }

    }

    @Override
    public void onCreate() {
        Log.d(TAG, "IPV6AddressAgent service started.");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "IPV6AddressAgent service stopped.");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting command");
        setIPv6Address(Address.getIPv6GlobalAddress());
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
            }
        }
        return START_STICKY;
    }

}
