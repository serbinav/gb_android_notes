package com.example.buynotes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Notes implements Parcelable {

    private String id;
    private String name;
    private String memo;
    private long date;
    private List<String> list = new ArrayList();
    private List<String> listDone = new ArrayList();

    public Notes(String name, long date) {
        this.name = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
    }

    protected Notes(Parcel in) {
        id = in.readString();
        name = in.readString();
        memo = in.readString();
        date = in.readLong();
        list = in.createStringArrayList();
        listDone = in.createStringArrayList();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(memo);
        dest.writeLong(date);
        dest.writeStringList(list);
        dest.writeStringList(listDone);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getListDone() {
        return listDone;
    }

    public void setListDone(List<String> listDone) {
        this.listDone = listDone;
    }
}
