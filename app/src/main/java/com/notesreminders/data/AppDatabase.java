package com.notesreminders.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.notesreminders.data.DAO.NoteDao;
import com.notesreminders.data.Entities.Note;

@Database(entities = Note.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE note ADD COLUMN type INTEGER");
//            database.execSQL("ALTER TABLE note ADD COLUMN notify_at TEXT");
//        }
//    };
}
