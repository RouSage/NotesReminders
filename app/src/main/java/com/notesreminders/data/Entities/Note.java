package com.notesreminders.data.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "heading")
    @NonNull
    public String heading;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "created_at")
    @NonNull
    public String createdAt;

    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "notify_at")
    public String notifyAt;

    @Ignore
    public Note() {
    }

    public Note(@NonNull String heading, String text, @NonNull String createdAt, int type, String notifyAt) {
        this.heading = heading;
        this.text = text;
//        this.createdAt = Calendar.getInstance().getTime().toString();
        this.createdAt = createdAt;
        this.type = type;
        this.notifyAt = notifyAt;
    }
}

