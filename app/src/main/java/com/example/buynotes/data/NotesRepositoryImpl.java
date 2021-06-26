package com.example.buynotes.data;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class NotesRepositoryImpl implements NotesRepository {

    private final ArrayList<Notes> notes = new ArrayList<>();

    public NotesRepositoryImpl(){
        Notes note2 = new Notes("покупки",
                new GregorianCalendar(2021, 1, 10).getTimeInMillis());
        ArrayList shoppingList = new ArrayList<String>();
        shoppingList.add("чай");
        shoppingList.add("мясо");
        shoppingList.add("сыр");
        note2.setList(shoppingList);

        Notes note3 = new Notes("дела",
                new GregorianCalendar(2021, 3, 20).getTimeInMillis());
        ArrayList shoppingDone= new ArrayList<String>();
        shoppingDone.add("парикмахерская");
        shoppingDone.add("ржд билеты");
        shoppingDone.add("собрать чемодан");
        note3.setListDone(shoppingDone);

        notes.add(new Notes("траты",
                new GregorianCalendar(2021, 0, 5).getTimeInMillis()));
        notes.add(note2);
        notes.add(note3);
    }

    @Override
    public List<Notes> getNotes() {
        return notes;
    }

    @Override
    public void clear() {
        notes.clear();
    }

    @Override
    public void delete(int index) {
        notes.remove(index);
    }

    @Override
    public Notes add(String name, long date) {
        //UUID.randomUUID().toString()
        Notes notesAdd = new Notes(name, date);
        notes.add(notesAdd);
        return notesAdd;
    }

    @Override
    public Notes addFull(String name, String memo, long date, ArrayList<String> list, ArrayList<String> listDone) {
        Notes notesAdd = new Notes(name, date);
        notesAdd.setMemo(memo);
        notesAdd.setList(list);
        notesAdd.setListDone(listDone);
        notes.add(notesAdd);
        return notesAdd;
    }

    @Override
    public void editNotes(int number, ArrayList<String> list, ArrayList<String> listDone) {
        Notes notesGet = notes.get(number);
        notesGet.setList(list);
        notesGet.setListDone(listDone);
    }
}
