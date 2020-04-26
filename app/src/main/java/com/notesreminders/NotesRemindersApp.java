package com.notesreminders;

import android.app.Application;

import androidx.room.Room;

import com.notesreminders.data.AppDatabase;
import com.notesreminders.notifications.NotificationHelper;
import com.notesreminders.utils.Constants;

public class NotesRemindersApp extends Application {

    public AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.DB_NAME).build();

        NotificationHelper.createNotificationChannel(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        db.close();

        super.onTerminate();
    }
}
