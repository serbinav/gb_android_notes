package com.example.buynotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListFragment.onNotesClicked {

    private NotesRepository notesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesRepository = new NotesRepositoryImpl();

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
        List<Notes> notes = notesRepository.getNotes();
        for (int i = 0; i < notes.size(); i++) {
            MenuItem myMoveGroupItem = navigationView.getMenu().findItem(R.id.act_add_new);
            SubMenu subMenu = myMoveGroupItem.getSubMenu();
            DateFormat df = new SimpleDateFormat(getString(R.string.simple_date_format));
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
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.notes_list_fragment,
                                    NotesDetailsFragment.newInstance(notes.get(item.getItemId())))
                            .commit();
                    return true;
            }
        });
    }

    @Override
    public void onNotesClicked(Notes note) {
        if (getResources().getBoolean(R.bool.isLandscape)) {

        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.notes_list_fragment, NotesDetailsFragment.newInstance(note))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_account:
                Toast.makeText(getApplicationContext(), R.string.icon_on_toolbar, Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
        }
    }
}