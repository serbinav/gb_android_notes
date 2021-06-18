package com.example.buynotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;

public class NotesDetailsActivity extends AppCompatActivity {

    public static final String ARG_NOTES = "ARG_NOTES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        if(savedInstanceState == null){

            Notes note = getIntent().getParcelableExtra(ARG_NOTES);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, NotesDetailsFragment.newInstance(note))
                    .commit();
        }
    }
}