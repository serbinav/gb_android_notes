package com.example.buynotes.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buynotes.R;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    public interface OnItemClickListener {
        void onItemClickListener(@NonNull String str, int index);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(@NonNull String str, int index);
    }

    private Fragment fragment;
    private ArrayList<String> todoList = new ArrayList<>();
    private int colorRes;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDate(List<String> toSet) {
        todoList.clear();
        todoList.addAll(toSet);
    }

    public int add(String str) {
        todoList.add(str);
        return todoList.size() - 1;
    }

    public ArrayList<String> get() {
        return todoList;
    }

    public void remove(int index) {
        todoList.remove(index);
    }

    public void setColor(int color) {
        colorRes = color;
    }

    private OnItemClickListener listener;

    private OnItemLongClickListener longClickListener;

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list,
                parent,
                false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
        holder.title.setText(todoList.get(position));
        holder.title.setBackgroundColor(colorRes);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            fragment.registerForContextMenu(itemView);

            itemView.setOnClickListener(v -> {
                        if (getListener() != null) {
                            int index = getAdapterPosition();
                            getListener().onItemClickListener(todoList.get(index), index);
                        }
                    }
            );

            itemView.setOnLongClickListener(v -> {
                itemView.showContextMenu();

                if (getLongClickListener() != null) {
                    int index = getAdapterPosition();
                    getLongClickListener().onItemLongClickListener(todoList.get(index), index);
                }
                return true;
            });

            title = itemView.findViewById(R.id.elem_name);
        }
    }
}