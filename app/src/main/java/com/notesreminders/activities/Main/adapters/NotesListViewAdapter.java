package com.notesreminders.activities.Main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.notesreminders.R;
import com.notesreminders.activities.NoteDetail.NoteDetailActivity;
import com.notesreminders.data.Entities.Note;
import com.notesreminders.utils.Constants;
import com.notesreminders.utils.Utils;

import java.util.List;

public class NotesListViewAdapter extends ArrayAdapter<Note> {
    public NotesListViewAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Note note = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notes_list_item, parent, false);
        }

        ImageView ivIcon = convertView.findViewById(R.id.ivIcon);
        TextView tvNoteId = convertView.findViewById(R.id.tvNoteId);
        TextView tvHeading = convertView.findViewById(R.id.tvHeading);
        TextView tvText = convertView.findViewById(R.id.tvText);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTime = convertView.findViewById(R.id.tvTime);

        if(note.type == Constants.NOTE_TYPE_NOTIFICATION) {
            ivIcon.setImageResource(R.drawable.list_ic_notification_normal);

            // Get UTC datetime from note
            String datetime = Utils.parseDateTimeToLocal(note.notifyAt);
            // Convert to local datetime
//            String localDatetime = Utils.convertToLocal(datetime);
            // Get date and time separately
            String localDate = datetime.split(" ")[0];
            String localTime = datetime.split(" ")[1];

            tvDate.setText(localDate);
            tvDate.setVisibility(View.VISIBLE);
            tvTime.setText(localTime);
            tvTime.setVisibility(View.VISIBLE);
        } else {
            ivIcon.setImageResource(R.drawable.list_ic_note_normal);
        }
        tvNoteId.setText(String.valueOf(note.id));
        tvHeading.setText(note.heading);
        tvText.setText(note.text);

        RelativeLayout listItem = convertView.findViewById(R.id.listItem);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoteDetailActivity.class);
                intent.putExtra("noteId", note.id);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
