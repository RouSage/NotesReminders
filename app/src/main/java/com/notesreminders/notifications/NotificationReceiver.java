package com.notesreminders.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.notesreminders.utils.Constants;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constants.ALARM_ACTION_NAME)) {
            int requestCode = intent.getIntExtra("requestCode", 0);
            String heading = intent.getStringExtra("heading");
            String text = intent.getStringExtra("text");

            NotificationCompat.Builder builder = NotificationHelper.createNotification(context, requestCode, heading, text);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(requestCode, builder.build());
        }
    }
}
