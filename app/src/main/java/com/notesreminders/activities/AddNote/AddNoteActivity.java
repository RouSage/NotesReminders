package com.notesreminders.activities.AddNote;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.notesreminders.NotesRemindersApp;
import com.notesreminders.R;
import com.notesreminders.activities.Main.MainActivity;
import com.notesreminders.data.AppDatabase;
import com.notesreminders.data.Entities.Note;
import com.notesreminders.notifications.NotificationHelper;
import com.notesreminders.utils.Constants;
import com.notesreminders.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    AppDatabase db;

    EditText editHeading;
    EditText editNoteText;
    CheckBox checkboxAddNotification;
    TextView labelNotifyDate;
    EditText editDate;
    TextView labelNotifyTime;
    EditText editTime;
    Button btnCancel;
    Button btnAddNote;

    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        db = ((NotesRemindersApp) getApplication()).db;

        bindViews();
    }

    private void bindViews() {
        btnCancel = findViewById(R.id.btnCancel);
        btnAddNote = findViewById(R.id.btnAddNote);

        editHeading = findViewById(R.id.editHeading);
        editNoteText = findViewById(R.id.editNoteText);
        checkboxAddNotification = findViewById(R.id.checkboxAddNotification);
        labelNotifyDate = findViewById(R.id.labelNotifyDate);
        editDate = findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        labelNotifyTime = findViewById(R.id.labelNotifyTime);
        editTime = findViewById(R.id.editTime);
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(AddNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
                editDate.setText(sdf.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog() {
        new TimePickerDialog(AddNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);
                editTime.setText(sdf.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    public void checkboxAddNotification_onClick(View v) {
        CheckBox checkBox = (CheckBox) v;
        if(checkBox.isChecked()) {
            labelNotifyDate.setVisibility(View.VISIBLE);
            editDate.setVisibility(View.VISIBLE);
            labelNotifyTime.setVisibility(View.VISIBLE);
            editTime.setVisibility(View.VISIBLE);
        } else {
            labelNotifyDate.setVisibility(View.GONE);
            editDate.setVisibility(View.GONE);
            labelNotifyTime.setVisibility(View.GONE);
            editTime.setVisibility(View.GONE);
        }
    }

    public void btnAddNote_onClick(View v) {
        if(!isValid(editHeading) || !isValid(editNoteText)) {
            return;
        }

        String heading = editHeading.getText().toString();
        String noteText = editNoteText.getText().toString();
        String createdAt = Utils.getCurrentDateTimeUTC();

        Note newNote = new Note(heading, noteText, createdAt, Constants.NOTE_TYPE_NOTE, null);

        if(checkboxAddNotification.isChecked()) {
            if(!isValid(editDate) || !isValid(editTime)) {
                return;
            }

            String notifyAtUTC = Utils.convertToUTC(calendar.getTime());

            newNote.type = Constants.NOTE_TYPE_NOTIFICATION;
            newNote.notifyAt = notifyAtUTC;
        }

        new AddNoteAsyncTask().execute(newNote);
    }

    public void btnCancel_onClick(View v) {
        finish();
    }

    private boolean isValid(EditText editText) {
        String text = editText.getText().toString();

        if(text.trim().isEmpty()) {
            editText.setError("This field cannot be empty!");

            return false;
        }

        return true;
    }

    private class AddNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected Void doInBackground(Note... notes) {
            Note note = notes[0];
            int id = (int)db.noteDao().insertNote(note);

            if(note.type == Constants.NOTE_TYPE_NOTIFICATION) {
                NotificationHelper.setAlarm(getApplicationContext(), id, note.heading, note.text, calendar.getTimeInMillis());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
