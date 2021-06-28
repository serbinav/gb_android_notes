package com.example.buynotes.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;
import com.example.buynotes.recycler.NotesAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotesDetailsFragment extends Fragment {

    public interface OnChangeDataInList {
        void onChangeDataInList(int noteNumber, ArrayList<String> list, ArrayList<String> listDone);
    }

    private static final String ARG_NOTES = "ARG_NOTES";
    private static final String ARG_NOTES_NUMBER = "ARG_NOTES_NUMBER";
    private NotesAdapter notesAdapter;
    private NotesAdapter notesDoneAdapter;

    private OnChangeDataInList onChangeDataInList;
    private int currentNoteNumber;

    public static NotesDetailsFragment newInstance(Notes note, int noteNumber) {
        NotesDetailsFragment fragment = new NotesDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTES, note);
        args.putInt(ARG_NOTES_NUMBER, noteNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeDataInList) {
            onChangeDataInList = (OnChangeDataInList) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onChangeDataInList = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesAdapter = new NotesAdapter(this);
        notesDoneAdapter = new NotesAdapter(this);
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

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NOTES)) {
            Notes note = args.getParcelable(ARG_NOTES);
            currentNoteNumber = args.getInt(ARG_NOTES_NUMBER);

            notesName.setText(note.getName());
            DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));
            notesDate.setText(df.format(new Date(note.getDate())));
            notesMemo.setText(note.getMemo());

            RecyclerView containerList = view.findViewById(R.id.list_elem);
            containerList.setLayoutManager(new LinearLayoutManager(requireContext()));

            notesAdapter.setDate(note.getList());
            notesAdapter.setColor(getResources().getColor(R.color.red_200));
            notesAdapter.setListener((str, index) -> {
                        notesAdapter.remove(index);
                        notesAdapter.notifyItemRemoved(index);
                        int position = notesDoneAdapter.add(str);
                        notesDoneAdapter.notifyItemInserted(position);

                        onChangeDataInList.onChangeDataInList(
                                currentNoteNumber,
                                notesAdapter.get(),
                                notesDoneAdapter.get()
                        );
                    }
            );
            containerList.setAdapter(notesAdapter);

            RecyclerView containerListDone = view.findViewById(R.id.list_done_elem);
            containerListDone.setLayoutManager(new LinearLayoutManager(requireContext()));

            notesDoneAdapter.setDate(note.getListDone());
            notesDoneAdapter.setColor(getResources().getColor(R.color.green_200));
            notesDoneAdapter.setListener((str, index) -> {
                    notesDoneAdapter.remove(index);
                    notesDoneAdapter.notifyItemRemoved(index);
                    int position = notesAdapter.add(str);
                    notesAdapter.notifyItemInserted(position);

                    onChangeDataInList.onChangeDataInList(
                        currentNoteNumber,
                        notesAdapter.get(),
                        notesDoneAdapter.get()
                    );
                    }
            );
            containerListDone.setAdapter(notesDoneAdapter);
        }
    }
}