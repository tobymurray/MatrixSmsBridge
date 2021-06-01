package ca.technicallyrural.matrixsmsbridge;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsService extends Service {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSmsReceiver, intentFilter);

        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()");

        unregisterReceiver(mSmsReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private final BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {
        public static final String SMS_BUNDLE = "pdus";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras == null) {
                Log.d(TAG, "Received with no extras: " + context + " " + intent);
                return;
            }

            Object[] smsMessages = (Object[]) intentExtras.get(SMS_BUNDLE);
            StringBuilder messagesString = new StringBuilder();
            for (Object sms : smsMessages) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms);

                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();

                messagesString.append("SMS From: ").append(address).append("\n");
                messagesString.append(smsBody).append("\n");
            }

            Log.d(TAG, messagesString.toString());

        }
    };

}