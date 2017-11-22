package com.hexaenna.drchella.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

/**
 * Created by admin on 11/14/2017.
 */

public class SMSBroadCastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    String[] separated = senderNum.split("-");
                    try {
                        if (separated[1].equals("URCmed")) {
                            Intent smsIntent = new Intent("otp");
                            smsIntent.putExtra("message",message);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                        }
                    } catch(Exception e){}
                }
            }
        } catch (Exception e) {}
    }
}
