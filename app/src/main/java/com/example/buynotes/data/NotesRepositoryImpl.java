package com.example.buynotes.data;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesRepositoryImpl implements NotesRepository {

    private final ArrayList<Notes> notes = new ArrayList<>();

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private Handler handler = new Handler(Looper.getMainLooper());

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

    //Thread.sleep здесь добавлен для наглядности
    @Override
    public void getNotes(Callback<List<Notes>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(notes);
                    }
                });
            }
        });
    }

    //Здесь date не используется, добавлена для Firebase
    @Override
    public void remove(int index, long date, Callback<Object> callback) {
        notes.remove(index);
        callback.onSuccess(index);
    }

    @Override
    public void add(String name, long date, Callback<Notes> callback) {
        //UUID.randomUUID().toString()
        Notes notesAdd = new Notes(name, date);
        notes.add(notesAdd);
        callback.onSuccess(notesAdd);
    }

    @Override
    public void editList(int number, List<String> list, List<String> listDone) {
        Notes notesGet = notes.get(number);
        notesGet.setList(list);
        notesGet.setListDone(listDone);
    }

    @Override
    public void editFull(int number, Notes note) {
        notes.remove(number);
        notes.add(number, note);
    }
}
