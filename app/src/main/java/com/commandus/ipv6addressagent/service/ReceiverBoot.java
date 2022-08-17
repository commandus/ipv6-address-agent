package com.commandus.ipv6addressagent.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receive android.intent.action.BOOT_COMPLETED broadcast and start up
 */
public class ReceiverBoot extends BroadcastReceiver {
    private static final String TAG = ReceiverBoot.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.e(TAG, "Intent is null");
            return;
        }

        String action = intent.getAction();
        if (action == null) {
            Log.e(TAG, "Action os null");
            return;
        }
        if (!action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.e(TAG, "Wrong action:" + action);
            return;
        }

        context.startService(new Intent(context, IPV6AddressAgentService.class));
        Log.d(TAG, "Listener started on reboot");
    }
}
