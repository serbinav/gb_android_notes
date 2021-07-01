package com.example.buynotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.buynotes.R;
import com.example.buynotes.data.Callback;
import com.example.buynotes.data.Notes;
import com.example.buynotes.data.NotesFirestoreRepositoryImpl;
import com.example.buynotes.data.NotesRepository;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesDetailsFragment.OnChangeDataInList,
        NotesEditFragment.OnChangeDataNotes {

    private final NotesRepository notesRepository = new NotesFirestoreRepositoryImpl();
    private List<Notes> notes;
    private int openNotesNumber = -1;
    private long openNotesDate = -1L;

    ProgressBar progressBar;
    NavigationView navigationView;
    MenuItem myMoveGroupItem;
    SubMenu subMenu;
    DateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        progressBar = findViewById(R.id.progress);
        navigationView = findViewById(R.id.navigation_view);
        myMoveGroupItem = navigationView.getMenu().findItem(R.id.act_notes);
        subMenu = myMoveGroupItem.getSubMenu();
        df = new SimpleDateFormat(getString(R.string.simple_date_format));

        progressBar.setVisibility(View.VISIBLE);
        notesRepository.getNotes(new Callback<List<Notes>>() {
            @Override
            public void onSuccess(List<Notes> result) {
                notes = result;
                for (int i = 0; i < result.size(); i++) {
                    subMenu.add(
                            Menu.NONE,
                            i,
                            Menu.NONE,
                            result.get(i).getName() + " - " + df.format(new Date(notes.get(i).getDate()))
                    );
                }
                navigationView.setNavigationItemSelectedListener(item -> {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    switch (item.getItemId()) {
                        case R.id.act_feedback:
                        case R.id.act_settings:
                        case R.id.act_help:
                            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            openNotesNumber = item.getItemId();
                            openNotesDate = result.get(openNotesNumber).getDate();

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.notes_list_fragment,
                                            NotesDetailsFragment.newInstance(
                                                    result.get(openNotesNumber),
                                                    openNotesNumber
                                            )
                                    )
                                    .addToBackStack(null)
                                    .commit();
                            return true;
                    }
                });
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_add_new:
                progressBar.setVisibility(View.VISIBLE);
                notesRepository.add(getString(R.string.empty_note),
                        Calendar.getInstance().getTimeInMillis(), new Callback<Notes>() {
                            @Override
                            public void onSuccess(Notes resultNote) {
                                notesRepository.getNotes(new Callback<List<Notes>>() {
                                    @Override
                                    public void onSuccess(List<Notes> resultList) {
                                        subMenu.add(
                                                Menu.NONE,
                                                resultList.size() - 1,
                                                Menu.NONE,
                                                resultNote.getName() + " - " + df.format(new Date(resultNote.getDate()))
                                        );
                                        openNotesNumber = resultList.size() - 1;
                                        openNotesDate = resultList.get(openNotesNumber).getDate();

                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.notes_list_fragment,
                                                        NotesEditFragment.newInstance(
                                                                resultList.get(openNotesNumber),
                                                                openNotesNumber
                                                        )
                                                )
                                                .addToBackStack(null)
                                                .commit();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });
                return true;
            case R.id.act_edit:
                if (openNotesNumber >= 0 && openNotesDate > 0) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.notes_list_fragment,
                                    NotesEditFragment.newInstance(
                                            notes.get(openNotesNumber),
                                            openNotesNumber
                                    )
                            )
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            case R.id.act_del:
                if (openNotesNumber >= 0 && openNotesDate > 0 ) {
                    progressBar.setVisibility(View.VISIBLE);
                    notesRepository.remove(openNotesNumber, openNotesDate, new Callback<Object>() {
                        @Override
                        public void onSuccess(Object result) {
                            subMenu.clear();
                            notesRepository.getNotes(new Callback<List<Notes>>() {
                                @Override
                                public void onSuccess(List<Notes> result) {
                                    for (int i = 0; i < result.size(); i++) {
                                        subMenu.add(
                                                Menu.NONE,
                                                i,
                                                Menu.NONE,
                                                result.get(i).getName() + " - " + df.format(new Date(result.get(i).getDate()))
                                        );
                                    }
                                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    openNotesNumber = -1;
                                    openNotesDate = -1L;
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void onChangeDataInList(int noteNumber, List<String> list, List<String> listDone) {
        notesRepository.editList(noteNumber, list, listDone);
    }

    @Override
    public void onChangeDataNotes(int noteNumber, Notes modifNote) {
        notesRepository.editFull(noteNumber, modifNote);
        subMenu.getItem(noteNumber).setTitle(modifNote.getName() + " - " + df.format(new Date(modifNote.getDate())));
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}