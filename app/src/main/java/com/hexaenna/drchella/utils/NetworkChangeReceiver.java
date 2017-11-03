package com.hexaenna.drchella.utils;

/**
 * Created by admin on 10/14/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String networkConnection = null;
        Log.e("action",action);
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()) {
                networkConnection = Constants.NETWORK_CONNECTED;
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            } else {
                networkConnection = Constants.NETWORK_NOT_CONNECTED;
                Toast.makeText(context, "Please Check your Network connection", Toast.LENGTH_SHORT).show();
            }

            Bundle extras = intent.getExtras();
            Intent i = new Intent(Constants.BROADCAST);
            // Data you need to pass to activity
            i.putExtra(Constants.MESSAGE, networkConnection);

            context.sendBroadcast(i);
        }

    }
}