package com.example.buynotes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesDetailsFragment extends Fragment {

    private static final String ARG_NOTES = "ARG_NOTES";

    public static NotesDetailsFragment newInstance(Notes note) {
        NotesDetailsFragment fragment = new NotesDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTES, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText notesName = view.findViewById(R.id.name);
        TextInputEditText notesDate = view.findViewById(R.id.date);
        TextInputEditText notesMemo = view.findViewById(R.id.memo);
        TextInputEditText notesList = view.findViewById(R.id.list);
        TextInputEditText notesListDone = view.findViewById(R.id.list_done);

        Notes note = getArguments().getParcelable(ARG_NOTES);
        notesName.setText(note.getName());
        DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));
        notesDate.setText(df.format(new Date(note.getDate())));
        notesMemo.setText(note.getMemo());
        notesList.setText(note.getList().toString());
        notesListDone.setText(note.getListDone().toString());
    }
}