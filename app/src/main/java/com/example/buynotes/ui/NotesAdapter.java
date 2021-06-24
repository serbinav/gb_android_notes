package com.example.buynotes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buynotes.R;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    public interface OnItemClickListener {
        void onItemClickListener(@NonNull String str);
    }

    private ArrayList<String> todoList = new ArrayList<>();
    private int colorRes;

    public void setDate(List<String> toSet) {
        todoList.clear();
        todoList.addAll(toSet);
    }

    public void setColor(int color) {
        colorRes = color;
    }

    private OnItemClickListener listener;

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
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

            itemView.setOnClickListener(v -> {
                        if (getListener() != null) {
                            getListener().onItemClickListener(todoList.get(getAdapterPosition()));
                        }
                    }
            );

            title = itemView.findViewById(R.id.elem_name);
        }
    }
}