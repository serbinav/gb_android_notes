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
import android.widget.Toast;

import com.example.buynotes.R;
import com.example.buynotes.data.Notes;
import com.example.buynotes.data.NotesRepository;
import com.example.buynotes.data.NotesRepositoryImpl;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesDetailsFragment.OnChangeDataInList {

    private NotesRepository notesRepository = new NotesRepositoryImpl();
    private List<Notes> notes = notesRepository.getNotes();
    private int openNotesNumber = -1;

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

        NavigationView navigationView = findViewById(R.id.navigation_view);
        MenuItem myMoveGroupItem = navigationView.getMenu().findItem(R.id.act_notes);
        SubMenu subMenu = myMoveGroupItem.getSubMenu();
        DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));
        for (int i = 0; i < notes.size(); i++) {
            subMenu.add(
                    Menu.NONE,
                    i,
                    Menu.NONE,
                    notes.get(i).getName() + " - " + df.format(new Date(notes.get(i).getDate()))
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
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.notes_list_fragment,
                                    NotesDetailsFragment.newInstance(
                                            notes.get(item.getItemId()),
                                            item.getItemId()
                                    )
                            )
                            .addToBackStack(null)
                            .commit();
                    return true;
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
        NavigationView navigationView = findViewById(R.id.navigation_view);
        MenuItem myMoveGroupItem = navigationView.getMenu().findItem(R.id.act_notes);
        SubMenu subMenu = myMoveGroupItem.getSubMenu();
        DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));

        switch (item.getItemId()) {
            case R.id.act_add_new:
                Notes notesAdd = notesRepository.add(getString(R.string.empty_note),
                        Calendar.getInstance().getTimeInMillis());
                notes = notesRepository.getNotes();

                subMenu.add(
                        Menu.NONE,
                        notes.size() - 1,
                        Menu.NONE,
                        notesAdd.getName() + " - " + df.format(new Date(notesAdd.getDate()))
                );
                openNotesNumber = notes.size() - 1;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.notes_list_fragment,
                                NotesEditFragment.newInstance(notes.get(openNotesNumber))
                        )
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.act_edit:
                if (openNotesNumber >= 0) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.notes_list_fragment,
                                    NotesEditFragment.newInstance(notes.get(openNotesNumber))
                            )
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            case R.id.act_del:
                if (openNotesNumber >= 0) {
                    notesRepository.delete(openNotesNumber);
                    notes = notesRepository.getNotes();
                    subMenu.clear();
                    for (int i = 0; i < notes.size(); i++) {
                        subMenu.add(
                                Menu.NONE,
                                i,
                                Menu.NONE,
                                notes.get(i).getName() + " - " + df.format(new Date(notes.get(i).getDate()))
                        );
                    }
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    openNotesNumber = openNotesNumber - 1;
                    return true;
                }
                return false;
            default:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    @Override
    public void onChangeDataInList(int noteNumber, ArrayList<String> list, ArrayList<String> listDone) {
        notesRepository.editNotes(noteNumber, list, listDone);
        notes = notesRepository.getNotes();
    }
}