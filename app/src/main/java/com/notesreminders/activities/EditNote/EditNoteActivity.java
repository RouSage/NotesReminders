package com.notesreminders.activities.EditNote;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.notesreminders.NotesRemindersApp;
import com.notesreminders.R;
import com.notesreminders.activities.Main.MainActivity;
import com.notesreminders.activities.NoteDetail.NoteDetailActivity;
import com.notesreminders.data.AppDatabase;
import com.notesreminders.data.Entities.Note;
import com.notesreminders.notifications.NotificationHelper;
import com.notesreminders.utils.Constants;
import com.notesreminders.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {

    AppDatabase db;

    TextView labelNoteId;
    TextView labelCreatedAt;
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
        setContentView(R.layout.activity_edit_note);

        db = ((NotesRemindersApp) getApplication()).db;

        bindViews();

        final int noteId = getIntent().getIntExtra("noteId", 0);
        db.noteDao().getById(noteId).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                if(note == null) {
                    Toast.makeText(EditNoteActivity.this,
                            "There was an error getting the note. Maybe is was deleted.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                // If note != null set TextView end EditText values
                labelNoteId.setText(String.valueOf(noteId));
                labelCreatedAt.setText(note.createdAt);
                editHeading.setText(note.heading);
                editNoteText.setText(note.text);
                if(note.type == Constants.NOTE_TYPE_NOTIFICATION) {
                    checkboxAddNotification.performClick();

                    String datetime = Utils.parseDateTimeToLocal(note.notifyAt);
                    editDate.setText(datetime.split(" ")[0]);
                    editTime.setText(datetime.split(" ")[1]);
                }
            }
        });
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

    public void btnEditNote_onClick(View v) {
        if(!isValid(editHeading) || !isValid(editNoteText)) {
            return;
        }

        int noteId = Integer.parseInt(labelNoteId.getText().toString());
        String createdAt = labelCreatedAt.getText().toString();
        String heading = editHeading.getText().toString();
        String noteText = editNoteText.getText().toString();

        Note note = new Note();
        note.id = noteId;
        note.heading = heading;
        note.text = noteText;
        note.createdAt = createdAt;
        note.type = Constants.NOTE_TYPE_NOTE;

        if(checkboxAddNotification.isChecked()) {
            if(!isValid(editDate) || !isValid(editTime)) {
                return;
            }

            String notifyAtUTC = Utils.convertToUTC(calendar.getTime());

            note.type = Constants.NOTE_TYPE_NOTIFICATION;
            note.notifyAt = notifyAtUTC;
        }

        new EditNoteAsyncTask().execute(note);
    }

    public void btnCancel_onClick(View v) {
        finish();
    }

    private void bindViews() {
        btnCancel = findViewById(R.id.btnCancel);
        btnAddNote = findViewById(R.id.btnAddNote);

        labelNoteId = findViewById(R.id.labelNoteId);
        labelCreatedAt = findViewById(R.id.labelCreatedAt);
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
        new DatePickerDialog(EditNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        new TimePickerDialog(EditNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);
                editTime.setText(sdf.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private boolean isValid(EditText editText) {
        String text = editText.getText().toString();

        if(text.trim().isEmpty()) {
            editText.setError("This field cannot be empty!");

            return false;
        }

        return true;
    }

    private class EditNoteAsyncTask extends AsyncTask<Note, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Note... notes) {
            Note note = notes[0];
            db.noteDao().updateNote(note);

            if(note.type == Constants.NOTE_TYPE_NOTIFICATION) {
                NotificationHelper.setAlarm(getApplicationContext(), note.id, note.heading, note.text, calendar.getTimeInMillis());
            }

            return notes[0].id;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            Intent intent = new Intent(EditNoteActivity.this, NoteDetailActivity.class);
            intent.putExtra("noteId", integer);
            startActivity(intent);
        }
    }
}
