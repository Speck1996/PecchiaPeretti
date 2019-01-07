package com.data4help.trackme.activities;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.data4help.trackme.R;


/**
 * Class in charge of handling home page activity: sending data to the server.
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private TextView navSubTitle;



    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //setting up the slide menu and the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navSubTitle = findViewById(R.id.nav_subtitle);

        SharedPreferences preferences = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String individualUsername = preferences.getString("username", "");
        navSubTitle.setText("Welcome: " + individualUsername);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyDataFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_mydata);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_tpreq:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                      new RequestFragment()).commit();
                break;

            case R.id.nav_mydata:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyDataFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}




