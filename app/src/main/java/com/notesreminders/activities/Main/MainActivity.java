package com.notesreminders.activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.notesreminders.NotesRemindersApp;
import com.notesreminders.R;
import com.notesreminders.activities.AddNote.AddNoteActivity;
import com.notesreminders.activities.Main.adapters.NotesListViewAdapter;
import com.notesreminders.data.AppDatabase;
import com.notesreminders.data.Entities.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;

    boolean fabExpanded = false;
    FloatingActionButton fab;

    FloatingActionButton fabAddNote;
    LinearLayout layoutFabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = ((NotesRemindersApp)getApplication()).db;

        bindViews();

        ArrayList<Note> notes = new ArrayList<Note>();
        notes.add(new Note(1, "Heading 1", "Some note Text", "02.02.2020"));
        notes.add(new Note(2, "Heading 2", "Some note Text", "02.03.2020"));
        notes.add(new Note(3, "Longer Heading 3", "Some note Text", "03.03.2020"));
        ListView listMain = findViewById(R.id.list_main);
        listMain.setAdapter(new NotesListViewAdapter(this, notes));

        closeFabSubMenu();
    }

    private void bindViews() {
        fab = findViewById(R.id.fab);

        layoutFabAddNote = findViewById(R.id.layoutFabAddNote);
        fabAddNote = findViewById(R.id.fabAddNote);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    public void fab_onClick(View v) {
        if(fabExpanded) {
            closeFabSubMenu();
        } else {
            openFabSubMenu();
        }
    }

    private void openFabSubMenu() {
        layoutFabAddNote.setVisibility(View.VISIBLE);

        fab.setImageResource(R.drawable.floatingactionbutton_ic_close_normal);
        fabExpanded = true;
    }

    private void closeFabSubMenu() {
        layoutFabAddNote.setVisibility(View.INVISIBLE);

        fab.setImageResource(R.drawable.floatingactionbutton_ic_add_normal);
        fabExpanded = false;
    }
}
