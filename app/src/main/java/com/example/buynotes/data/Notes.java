package com.example.buynotes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Notes implements Parcelable {

    private String name;
    private String memo;
    private long date;
    private ArrayList<String> list = new ArrayList();
    private ArrayList<String> listDone = new ArrayList();

    public Notes(String name, long date) {
        this.name = name;
        this.date = date;
    }

    protected Notes(Parcel in) {
        name = in.readString();
        memo = in.readString();
        date = in.readLong();
        list = in.createStringArrayList();
        listDone = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(memo);
        dest.writeLong(date);
        dest.writeStringList(list);
        dest.writeStringList(listDone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getListDone() {
        return listDone;
    }

    public void setListDone(ArrayList<String> listDone) {
        this.listDone = listDone;
    }
}
