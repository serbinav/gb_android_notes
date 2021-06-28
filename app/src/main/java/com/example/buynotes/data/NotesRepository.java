package com.example.buynotes.data;

import java.util.ArrayList;
import java.util.List;

public interface NotesRepository {

    List<Notes> getNotes();

    void delete(int index);

    Notes add(String name, long date);

    void editList(int number,
                  ArrayList<String> list,
                  ArrayList<String> listDone);

    void editFull(int number, Notes note);
}
