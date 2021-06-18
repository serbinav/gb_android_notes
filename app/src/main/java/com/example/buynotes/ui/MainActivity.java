package com.example.buynotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;

public class MainActivity extends AppCompatActivity implements NotesListFragment.onNotesClicked {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onNotesClicked(Notes note) {
        if (getResources().getBoolean(R.bool.isLandscape)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.notes_details_fragment, NotesDetailsFragment.newInstance(note))
                    .commit();
        } else {
            Intent intent = new Intent(this, NotesDetailsActivity.class);
            intent.putExtra(NotesDetailsActivity.ARG_NOTES, note);
            startActivity(intent);
        }
    }
}
