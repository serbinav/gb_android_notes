package com.example.buynotes.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;
import com.example.buynotes.data.NotesRepository;
import com.example.buynotes.data.NotesRepositoryImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesListFragment extends Fragment {
    public interface onNotesClicked {
        void onNotesClicked(Notes note);
    }

    private NotesRepository notesRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notesRepository = new NotesRepositoryImpl();
    }

    private onNotesClicked onNotesClicked;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onNotesClicked) {
            onNotesClicked = (onNotesClicked) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNotesClicked = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout notesList = view.findViewById(R.id.notes_list_container);
        List<Notes> notes = notesRepository.getNotes();

        for (Notes note : notes) {
            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_notes, notesList, false);
            TextView notesName = itemView.findViewById(R.id.notes_name);
            notesName.setText(note.getName());
            TextView notesDate = itemView.findViewById(R.id.notes_date);
            DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));
            notesDate.setText(df.format(new Date(note.getDate())));

            itemView.setOnClickListener(v -> {
                if (onNotesClicked != null) {
                    onNotesClicked.onNotesClicked(note);
                }
            });

            notesList.addView(itemView);
        }
    }
}
