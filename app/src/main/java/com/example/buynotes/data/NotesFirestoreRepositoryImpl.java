package com.example.buynotes.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotesFirestoreRepositoryImpl implements NotesRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final static String NOTES = "notes";

    private final static String NAME = "name";
    private final static String DATE = "date";
    private final static String MEMO = "memo";
    private final static String LIST = "list";
    private final static String LIST_DONE = "listDone";

    @Override
    public void getNotes(Callback<List<Notes>> callback) {
        firebaseFirestore.collection(NOTES)
                .orderBy(DATE, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Notes> result = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String) document.get(NAME);
                                String memo = (String) document.get(MEMO);
                                long date = (long) document.get(DATE);
                                List<String> list = (List<String>) document.get(LIST);
                                List<String> listDone = (List<String>) document.get(LIST_DONE);

                                Notes note = new Notes(name, date);
                                note.setList(list);
                                note.setListDone(listDone);
                                note.setMemo(memo);
                                result.add(note);
                            }

                            callback.onSuccess(result);
                        } else {
                            task.getException();
                        }
                    }
                });
    }

    @Override
    public void add(String name, long date, Callback<Notes> callback) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(NAME, name);
        data.put(DATE, date);
        data.put(LIST, new ArrayList<>());
        data.put(LIST_DONE, new ArrayList<>());
        firebaseFirestore.collection(NOTES)
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Notes note = new Notes(name, date);

                        callback.onSuccess(note);
                    }
                });
    }

    @Override
    public void remove(int index, long date, Callback<Object> callback) {
        firebaseFirestore.collection(NOTES)
                .whereEqualTo(DATE, date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                firebaseFirestore.collection(NOTES)
                                        .document(doc.getId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                callback.onSuccess(index);
                                            }
                                        });
                            }

                        } else {
                            task.getException();
                        }
                    }
                });
    }

    @Override
    public void editList(int number, List<String> list, List<String> listDone) {

    }

    @Override
    public void editFull(int number, Notes note) {

    }
}
