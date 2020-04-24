package com.notesreminders.activities.Main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.notesreminders.R;
import com.notesreminders.data.Entities.Note;

import java.util.List;

public class NotesListViewAdapter extends ArrayAdapter<Note> {
    public NotesListViewAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notes_list_item, parent, false);
        }

        TextView tvHeading = convertView.findViewById(R.id.tvHeading);
        TextView tvText = convertView.findViewById(R.id.tvText);

        tvHeading.setText(note.heading);
        tvText.setText(note.text);

        return convertView;
    }
}
