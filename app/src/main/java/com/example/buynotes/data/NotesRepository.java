package com.example.buynotes.data;

import java.util.List;

public interface NotesRepository {

    void getAll(Callback<List<Notes>> callback);

    void get(int index, String id, Callback<Notes> callback);

    void add(String name, long date, Callback<Notes> callback);

    void remove(int index, String id, Callback<Object> callback);

    void editNoteList(int index,
                  String id,
                  List<String> list,
                  List<String> listDone,
                  Callback<Object> callback);

    void editNote(int index, Notes note, Callback<Object> callback);
}
