package com.notesreminders.activities.NoteDetail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.notesreminders.NotesRemindersApp;
import com.notesreminders.R;
import com.notesreminders.activities.EditNote.EditNoteActivity;
import com.notesreminders.activities.Main.MainActivity;
import com.notesreminders.activities.dialogs.DeleteNoteDialogFragment;
import com.notesreminders.data.AppDatabase;
import com.notesreminders.data.Entities.Note;
import com.notesreminders.notifications.NotificationHelper;
import com.notesreminders.utils.Constants;

public class NoteDetailActivity extends AppCompatActivity implements DeleteNoteDialogFragment.DeleteNoteDialogListener {

    AppDatabase db;
    Note noteDetail;

    TextView tvToolbarHeading;
    TextView tvNoteId;
    TextView tvCreatedAt;
    TextView tvText;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        db = ((NotesRemindersApp) getApplication()).db;
        final int noteId = getIntent().getIntExtra("noteId", 0);

        bindViews();

        db.noteDao().getById(noteId).observe(this, observer);
    }

    private Observer<Note> observer = new Observer<Note>() {
        @Override
        public void onChanged(Note note) {
            if(note == null) {
                Intent intent = new Intent(NoteDetailActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                noteDetail = note;

                tvToolbarHeading.setText(note.heading);
                tvNoteId.setText(String.valueOf(note.id));
                tvCreatedAt.setText(note.createdAt);
                tvText.setText(note.text);
            }
        }
    };

    private void bindViews() {
        tvToolbarHeading = findViewById(R.id.tvToolbarHeading);
        tvNoteId = findViewById(R.id.tvNoteId);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvText = findViewById(R.id.tvText);

        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.note_detail_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.actionEditNote:
                        Intent intent = new Intent(NoteDetailActivity.this, EditNoteActivity.class);
                        intent.putExtra("noteId", noteDetail.id);
                        startActivity(intent);
                        return true;
                    case R.id.actionDeleteNote:
                        final DeleteNoteDialogFragment dialog = new DeleteNoteDialogFragment();
                        dialog.show(getSupportFragmentManager(), "delete_note_dialog");
                        return true;
                    default:
                        return false;
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        new DeleteNoteAsyncTask().execute(noteDetail);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    private class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected Void doInBackground(Note... notes) {
            db.noteDao().deleteNote(notes[0]);

            if(notes[0].type == Constants.NOTE_TYPE_NOTIFICATION) {
                NotificationHelper.cancelAlarm(getApplicationContext(), notes[0].id);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "Note was successfully deleted.", Toast.LENGTH_SHORT).show();
        }
    }
}
