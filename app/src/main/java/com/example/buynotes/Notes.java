package com.example.buynotes;

import java.util.ArrayList;
import java.util.Calendar;

public class Notes {

    private String name;
    private String memo;
    private Calendar date;
    private ArrayList<String> shoppingList;
    private ArrayList<String> shoppingListDone;

    public Notes(String name, Calendar date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public ArrayList<String> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ArrayList<String> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ArrayList<String> getShoppingListDone() {
        return shoppingListDone;
    }

    public void setShoppingListDone(ArrayList<String> shoppingListDone) {
        this.shoppingListDone = shoppingListDone;
    }





}
