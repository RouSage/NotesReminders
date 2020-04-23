package com.notesreminders.activities.AddNote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.notesreminders.R;

public class AddNoteActivity extends AppCompatActivity {

    EditText editHeading;
    EditText editNoteText;
    Button btnCancel;
    Button btnAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        bindViews();
    }

    private void bindViews() {
        editHeading = findViewById(R.id.editHeading);
        editNoteText = findViewById(R.id.editNoteText);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddNote = findViewById(R.id.btnAddNote);
    }

    public void btnAddNote_onClick(View v) {

    }

    public void btnCancel_onClick(View v) {
        finish();
    }
}
