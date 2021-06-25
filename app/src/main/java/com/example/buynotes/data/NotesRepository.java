package com.example.buynotes.data;

import java.util.ArrayList;
import java.util.List;

public interface NotesRepository {

    List<Notes> getNotes();

    void clear();

    void delete(int index);

    Notes add(String name, long date);

    Notes addFull(String name,
                  String memo,
                  long date,
                  ArrayList<String> list,
                  ArrayList<String> listDone);

}
