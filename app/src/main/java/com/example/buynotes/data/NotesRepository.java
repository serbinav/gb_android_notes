package com.example.buynotes.data;

import java.util.List;

public interface NotesRepository {

    void getNotes(Callback<List<Notes>> callback);

    void remove(int index, long date, Callback<Object> callback);

    void add(String name, long date, Callback<Notes> callback);

    void editList(int number,
                  List<String> list,
                  List<String> listDone);

    void editFull(int number, Notes note);
}
