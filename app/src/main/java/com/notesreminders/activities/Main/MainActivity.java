package com.notesreminders.activities.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.notesreminders.R;
import com.notesreminders.data.AppDatabase;
import com.notesreminders.utils.Constants;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.DB_NAME).build();
    }
}
