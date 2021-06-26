package com.example.buynotes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;
import com.example.buynotes.recycler.NotesAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesDetailsFragment extends Fragment {

    public enum ChooseAdapter {
        NOTES_ADAPTER,
        NOTES_DONE_ADAPTER
    }

    private static final String ARG_NOTES = "ARG_NOTES";
    private static final String ARG_NOTES_NUMBER = "ARG_NOTES_NUMBER";
    private NotesAdapter notesAdapter;
    private NotesAdapter notesDoneAdapter;

    private int longClickIndex;
    private String longClickStr;
    private ChooseAdapter adapter;

    public static NotesDetailsFragment newInstance(Notes note, int noteNumber) {
        NotesDetailsFragment fragment = new NotesDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTES, note);
        args.putInt(ARG_NOTES_NUMBER, noteNumber);
        fragment.setArguments(args);
        return fragment;
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
        TextInputEditText notesList = view.findViewById(R.id.list);
        TextInputEditText notesListDone = view.findViewById(R.id.list_done);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NOTES)) {
            Notes note = args.getParcelable(ARG_NOTES);
            notesName.setText(note.getName());
            DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));
            notesDate.setText(df.format(new Date(note.getDate())));
            notesMemo.setText(note.getMemo());

            RecyclerView containerList = view.findViewById(R.id.list_elem);
            containerList.setLayoutManager(new LinearLayoutManager(requireContext()));

            notesAdapter.setDate(note.getList());
            notesAdapter.setColor(getResources().getColor(R.color.red_200));
            notesAdapter.setListener(str ->
                    Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show()
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
            notesDoneAdapter.setListener(str ->
                    Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show()
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
                case R.id.act_done:
                    Toast.makeText(requireContext(),
                            R.string.welldone,
                            Toast.LENGTH_SHORT).show();
                    return true;
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
                case R.id.act_add:
                    int pos = notesAdapter.add("новое дело");
                    notesAdapter.notifyItemInserted(pos);
                    return true;
                case R.id.act_delete:
                    notesAdapter.remove(longClickIndex);
                    notesAdapter.notifyItemRemoved(longClickIndex);
                    return true;
                case R.id.act_transfer:
                    notesAdapter.remove(longClickIndex);
                    notesAdapter.notifyItemRemoved(longClickIndex);
                    int position = notesDoneAdapter.add(longClickStr);
                    notesDoneAdapter.notifyItemInserted(position);
                    return true;
            }
        } else {
            switch (item.getItemId()) {
                case R.id.act_add:
                    int pos = notesDoneAdapter.add("новое дело");
                    notesDoneAdapter.notifyItemInserted(pos);
                    return true;
                case R.id.act_delete:
                    notesDoneAdapter.remove(longClickIndex);
                    notesDoneAdapter.notifyItemRemoved(longClickIndex);
                    return true;
                case R.id.act_transfer:
                    notesDoneAdapter.remove(longClickIndex);
                    notesDoneAdapter.notifyItemRemoved(longClickIndex);
                    int position = notesAdapter.add(longClickStr);
                    notesAdapter.notifyItemInserted(position);
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }
}