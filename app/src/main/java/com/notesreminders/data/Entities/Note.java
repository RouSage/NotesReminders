package com.notesreminders.data.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "heading")
    @NonNull
    private String heading;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "created_at")
    @NonNull
    private String createdAt;

    public Note(int id, @NonNull String heading, String text, @NonNull String createdAt) {
        this.id = id;
        this.heading = heading;
        this.text = text;
//        this.createdAt = Calendar.getInstance().getTime().toString();
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getHeading() {
        return heading;
    }

    public String getText() {
        return text;
    }

    @NonNull
    public String getCreatedAt() {
        return createdAt;
    }
}
