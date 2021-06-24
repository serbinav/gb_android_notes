package com.example.buynotes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class NotesEditFragment extends Fragment {

    private static final String ARG_NOTES = "ARG_NOTES";

    public static NotesEditFragment newInstance(Notes note) {
        NotesEditFragment fragment = new NotesEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTES, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText notesName = view.findViewById(R.id.name);
        DatePicker notesDate = view.findViewById(R.id.date);
        TextInputEditText notesMemo = view.findViewById(R.id.memo);
        TextInputEditText notesList = view.findViewById(R.id.list);
        TextInputEditText notesListDone = view.findViewById(R.id.list_done);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NOTES)) {
            Notes note = args.getParcelable(ARG_NOTES);
            notesName.setText(note.getName());

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(note.getDate());
            notesDate.init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        }
                    }
            );

            //DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));
            //notesDate.setText(df.format(new Date(note.getDate())));
            notesMemo.setText(note.getMemo());

            RecyclerView containerList = view.findViewById(R.id.list_elem);
            containerList.setLayoutManager(new LinearLayoutManager(requireContext()));
            NotesAdapter notesAdapter = new NotesAdapter();
            notesAdapter.setDate(note.getList());
            notesAdapter.setColor(getResources().getColor(R.color.red_200));
            notesAdapter.setListener(str ->
                    Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show()
            );
            containerList.setAdapter(notesAdapter);

            RecyclerView containerListDone = view.findViewById(R.id.list_done_elem);
            containerListDone.setLayoutManager(new LinearLayoutManager(requireContext()));
            NotesAdapter notesDoneAdapter = new NotesAdapter();
            notesDoneAdapter.setDate(note.getListDone());
            notesDoneAdapter.setColor(getResources().getColor(R.color.green_200));
            notesDoneAdapter.setListener(str ->
                    Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show()
            );
            containerListDone.setAdapter(notesDoneAdapter);
        }
        notesList.setOnClickListener(v -> externalOnClick(v));
        notesListDone.setOnClickListener(v -> externalOnClick(v));
    }

    private void externalOnClick(View v) {
        PopupMenu menu = new PopupMenu(requireContext(), v);
        getActivity().getMenuInflater().inflate(R.menu.menu_popup, menu.getMenu());
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.act_done:
                    Toast.makeText(requireContext(),
                            R.string.welldone,
                            Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    Toast.makeText(requireContext(),
                            item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    return true;
            }
        });
        menu.show();
    }
}