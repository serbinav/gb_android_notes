package com.example.buynotes.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public void getAll(Callback<List<Notes>> callback) {
        firebaseFirestore.collection(NOTES)
                .orderBy(DATE, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Notes> result = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null) {
                                    String name = (String) document.get(NAME);
                                    String memo = (String) document.get(MEMO);
                                    long date = (long) document.get(DATE);
                                    List<String> list = (List<String>) document.get(LIST);
                                    List<String> listDone = (List<String>) document.get(LIST_DONE);

                                    Notes note = new Notes(name, date);
                                    note.setId(document.getId());
                                    note.setMemo(memo);
                                    note.setList(list);
                                    note.setListDone(listDone);
                                    result.add(note);
                                }
                            }

                            callback.onSuccess(result);
                        } else {
                            task.getException();
                        }
                    }
                });
    }

    @Override
    public void get(int index, String id, Callback<Notes> callback) {
        firebaseFirestore.collection(NOTES)
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc != null) {
                                String name = (String) doc.get(NAME);
                                String memo = (String) doc.get(MEMO);
                                long date = (long) doc.get(DATE);
                                List<String> list = (List<String>) doc.get(LIST);
                                List<String> listDone = (List<String>) doc.get(LIST_DONE);

                                Notes note = new Notes(name, date);
                                note.setId(doc.getId());
                                note.setMemo(memo);
                                note.setList(list);
                                note.setListDone(listDone);

                                callback.onSuccess(note);
                            }
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
                        if (task.isSuccessful()) {
                            DocumentReference result = task.getResult();
                            if (result != null) {
                                Notes note = new Notes(name, date);
                                note.setId(result.getId());

                                callback.onSuccess(note);
                            }
                        } else {
                            task.getException();
                        }
                    }
                });
    }

    @Override
    public void remove(int index, String id, Callback<Object> callback) {
        firebaseFirestore.collection(NOTES)
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onSuccess(index);
                    }
                });
    }

    @Override
    public void editNoteList(int index,
                             String id,
                             List<String> list,
                             List<String> listDone,
                             Callback<Object> callback) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(LIST, list);
        data.put(LIST_DONE, listDone);
        firebaseFirestore.collection(NOTES)
                .document(id)
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onSuccess(index);
                    }
                });

    }

    @Override
    public void editNote(int index, Notes note, Callback<Object> callback) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(NAME, note.getName());
        data.put(DATE, note.getDate());
        data.put(MEMO, note.getMemo());
        data.put(LIST, note.getList());
        data.put(LIST_DONE, note.getListDone());
        firebaseFirestore.collection(NOTES)
                .document(note.getId())
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onSuccess(index);
                    }
                });
    }
}
