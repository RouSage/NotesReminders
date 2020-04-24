package com.notesreminders.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.notesreminders.data.DAO.NoteDao;
import com.notesreminders.data.Entities.Note;

@Database(entities = Note.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
