package com.hexaenna.drchella.service;

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

import com.hexaenna.drchella.utils.Constants;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static boolean firstConnect ;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String networkConnection = null;

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

                // do subroutines here

                if (currentNetworkInfo.isConnected()) {
                    networkConnection = Constants.NETWORK_CONNECTED;
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();

                } else {
                    networkConnection = Constants.NETWORK_NOT_CONNECTED;
                    Toast.makeText(context, "Please Check your Network connection", Toast.LENGTH_SHORT).show();

                }


            Intent i = new Intent(Constants.BROADCAST);
            // Data you need to pass to activity
            i.putExtra(Constants.MESSAGE, networkConnection);
            Log.e("action", networkConnection);
            context.sendBroadcast(i);
        }

    }
}