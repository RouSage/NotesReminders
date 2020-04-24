package com.notesreminders.data.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
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

    public Note(@NonNull String heading, String text, @NonNull String createdAt) {
        this.heading = heading;
        this.text = text;
//        this.createdAt = Calendar.getInstance().getTime().toString();
        this.createdAt = createdAt;
    }
}
