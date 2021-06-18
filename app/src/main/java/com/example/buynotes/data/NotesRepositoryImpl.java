package com.example.buynotes.data;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class NotesRepositoryImpl implements NotesRepository {

    @Override
    public List<Notes> getNotes() {
        ArrayList<Notes> result = new ArrayList<>();

        Notes note2 = new Notes("покупки",
                new GregorianCalendar(2021, 1, 10).getTimeInMillis());
        ArrayList shoppingList = new ArrayList<String>();
        shoppingList.add("чай");
        shoppingList.add("мясо");
        shoppingList.add("сыр");
        note2.setList(shoppingList);

        result.add(new Notes("траты",
                new GregorianCalendar(2021, 0, 5).getTimeInMillis()));
        result.add(note2);
        result.add(new Notes("дела",
                new GregorianCalendar(2021, 3, 20).getTimeInMillis()));

        return result;
    }
}
