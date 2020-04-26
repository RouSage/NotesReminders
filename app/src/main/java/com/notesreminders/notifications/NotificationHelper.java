package com.notesreminders.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.notesreminders.R;
import com.notesreminders.activities.NoteDetail.NoteDetailActivity;
import com.notesreminders.utils.Constants;

public class NotificationHelper {
    public static void setAlarm(Context context, int requestCode, String notificationHeading, String notificationText, long triggerAtMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        NotificationReceiver receiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter(Constants.ALARM_ACTION_NAME);
        context.registerReceiver(receiver, filter);

        Intent intent = new Intent(Constants.ALARM_ACTION_NAME);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("heading", notificationHeading);
        intent.putExtra("text", notificationText);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    public static void cancelAlarm(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        NotificationReceiver receiver = new NotificationReceiver();
//        IntentFilter filter = new IntentFilter(Constants.ALARM_ACTION_NAME);
//        context.registerReceiver(receiver, filter);
        Intent intent = new Intent(Constants.ALARM_ACTION_NAME);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public static NotificationCompat.Builder createNotification(Context context, int requestCode, String heading, String text) {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra("noteId", requestCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, 0);

        return new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(heading)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    public static void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,
                    Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
