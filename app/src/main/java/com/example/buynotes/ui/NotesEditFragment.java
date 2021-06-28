package com.example.buynotes.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;
import com.example.buynotes.recycler.ChooseAdapter;
import com.example.buynotes.recycler.NotesAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotesEditFragment extends Fragment {

    public interface OnChangeDataNotes {
        void onChangeDataNotes(int noteNumber, Notes modifNote);
    }

    private static final String ARG_NOTES = "ARG_NOTES";
    private static final String ARG_NOTES_NUMBER = "ARG_NOTES_NUMBER";
    private TextInputEditText notesName;
    private DatePicker notesDate;
    private TextInputEditText notesMemo;
    private NotesAdapter notesAdapter;
    private NotesAdapter notesDoneAdapter;
    private ChooseAdapter adapter;

    private OnChangeDataNotes onChangeDataNotes;
    private int currentNoteNumber;

    private int longClickIndex;
    private String longClickStr;

    public static NotesEditFragment newInstance(Notes note, int noteNumber) {
        NotesEditFragment fragment = new NotesEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTES, note);
        args.putInt(ARG_NOTES_NUMBER, noteNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeDataNotes) {
            onChangeDataNotes = (OnChangeDataNotes) context;
        }
    }

    @Override
    public void onDetach() {
        onChangeDataNotes = null;
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        notesAdapter = new NotesAdapter(this);
        notesDoneAdapter = new NotesAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_edit, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_appbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (onChangeDataNotes != null) {
            if (item.getItemId() == R.id.act_save) {
                Notes modifNote = new Notes(notesName.getText().toString(),
                        new GregorianCalendar(
                                notesDate.getYear(),
                                notesDate.getMonth(),
                                notesDate.getDayOfMonth()
                        ).getTimeInMillis());
                modifNote.setMemo(notesMemo.getText().toString());
                modifNote.setList(notesAdapter.get());
                modifNote.setListDone(notesDoneAdapter.get());
                onChangeDataNotes.onChangeDataNotes(currentNoteNumber, modifNote);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notesName = view.findViewById(R.id.name);
        notesDate = view.findViewById(R.id.date);
        notesMemo = view.findViewById(R.id.memo);
        TextInputEditText notesList = view.findViewById(R.id.list);
        TextInputEditText notesListDone = view.findViewById(R.id.list_done);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NOTES)) {
            Notes note = args.getParcelable(ARG_NOTES);
            currentNoteNumber = args.getInt(ARG_NOTES_NUMBER);
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
                    }
            );
            notesAdapter.setLongClickListener((str, index) -> {
                        longClickIndex = index;
                        longClickStr = str;
                        adapter = ChooseAdapter.NOTES_ADAPTER;
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
                    }
            );
            notesDoneAdapter.setLongClickListener((str, index) -> {
                        longClickIndex = index;
                        longClickStr = str;
                        adapter = ChooseAdapter.NOTES_DONE_ADAPTER;
                    }
            );
            containerListDone.setAdapter(notesDoneAdapter);
        }
        notesList.setOnClickListener(v -> externalOnClick(v, notesAdapter));
        notesListDone.setOnClickListener(v -> externalOnClick(v, notesDoneAdapter));
    }

    private void externalOnClick(View v, NotesAdapter adapter) {
        PopupMenu menu = new PopupMenu(requireContext(), v);
        getActivity().getMenuInflater().inflate(R.menu.menu_popup, menu.getMenu());
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.act_add:
                    int pos = adapter.add("новое дело");
                    adapter.notifyItemInserted(pos);
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

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu,
                                    @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (adapter == ChooseAdapter.NOTES_ADAPTER) {
            switch (item.getItemId()) {
                case R.id.act_edit:
                    int pos = notesAdapter.add("новое дело");
                    notesAdapter.notifyItemInserted(pos);
                    break;
                case R.id.act_delete:
                    notesAdapter.remove(longClickIndex);
                    notesAdapter.notifyItemRemoved(longClickIndex);
                    break;
            }
            return true;
        }
        if (adapter == ChooseAdapter.NOTES_DONE_ADAPTER) {
            switch (item.getItemId()) {
                case R.id.act_edit:
                    int pos = notesDoneAdapter.add("новое дело");
                    notesDoneAdapter.notifyItemInserted(pos);
                    break;
                case R.id.act_delete:
                    notesDoneAdapter.remove(longClickIndex);
                    notesDoneAdapter.notifyItemRemoved(longClickIndex);
                    break;
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }
}