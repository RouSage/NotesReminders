package com.notesreminders.activities.EditNote;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.notesreminders.NotesRemindersApp;
import com.notesreminders.R;
import com.notesreminders.activities.Main.MainActivity;
import com.notesreminders.activities.NoteDetail.NoteDetailActivity;
import com.notesreminders.data.AppDatabase;
import com.notesreminders.data.Entities.Note;

public class EditNoteActivity extends AppCompatActivity {

    AppDatabase db;

    TextView labelNoteId;
    TextView labelCreatedAt;
    EditText editHeading;
    EditText editNoteText;
    Button btnCancel;
    Button btnAddNote;

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
            }
        });
    }

    private void bindViews() {
        btnCancel = findViewById(R.id.btnCancel);
        btnAddNote = findViewById(R.id.btnAddNote);

        labelNoteId = findViewById(R.id.labelNoteId);
        labelCreatedAt = findViewById(R.id.labelCreatedAt);
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

        new EditNoteAsyncTask().execute(note);
    }

    public void btnCancel_onClick(View v) {
        finish();
    }

    private class EditNoteAsyncTask extends AsyncTask<Note, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Note... notes) {
            db.noteDao().updateNote(notes[0]);

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
