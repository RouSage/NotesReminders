package com.notesreminders.activities.AddNote;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.notesreminders.NotesRemindersApp;
import com.notesreminders.R;
import com.notesreminders.activities.Main.MainActivity;
import com.notesreminders.data.AppDatabase;
import com.notesreminders.data.Entities.Note;
import com.notesreminders.utils.Utils;

public class AddNoteActivity extends AppCompatActivity {

    AppDatabase db;

    EditText editHeading;
    EditText editNoteText;
    Button btnCancel;
    Button btnAddNote;

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
    }

    private boolean isValid(EditText editText) {
        String text = editText.getText().toString();

        if(text.trim().isEmpty()) {
            editText.setError("This field cannot be empty!");

            return false;
        }

        return true;
    }

    public void btnAddNote_onClick(View v) {
        if(!isValid(editHeading) || !isValid(editNoteText)) {
            return;
        }

        String heading = editHeading.getText().toString();
        String noteText = editNoteText.getText().toString();
        String createdAt = Utils.getDateTimeUTC();

        Note newNote = new Note(heading, noteText, createdAt);

        new AddNoteAsyncTask().execute(newNote);
    }

    public void btnCancel_onClick(View v) {
        finish();
    }

    private class AddNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Note... notes) {
            db.noteDao().insertNote(notes[0]);

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
