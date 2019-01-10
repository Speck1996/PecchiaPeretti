package com.data4help.trackme.activities;


import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.data4help.trackme.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.data.Session;

import java.sql.Date;
import java.util.ArrayList;


/**
 * Class in charge of handling home activity: from this activity the user
 * have access to the data uploaded in the server and the third party data access request
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Drawer used to access the fragments of the app
     */
    private DrawerLayout drawer;

    private NavigationView navigationView;

    private TextView navSubTitle;

    private static final String TAG = "Home activity";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        createNavigationDrawer();

        //when the app is reloaded or just opened it goes to my data fragment
        if(savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyDataFragment()).commit();

            //setting the current fragment as checked
            navigationView.setCheckedItem(R.id.nav_mydata);
        }

    }

    private void createNavigationDrawer(){
        //setting up the slide menu and the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //binding the navigation view
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getting the username for the header subtitle
        SharedPreferences preferences = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String individualUsername = preferences.getString("username", "");

        //getting the header view and then the text field
        View headerView = navigationView.getHeaderView(0);
        navSubTitle= headerView.findViewById(R.id.nav_subtitle);
        navSubTitle.setText("Welcome, " + individualUsername);


        //setting up the listener for opening and closing action
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    /**
     * {@inheritDoc}
     * @param menuItem the item selected in the navigation drawer
     * @return true after the drawer is closed and the right fragment is picked
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavigationView navigationView = findViewById(R.id.nav_view);


        //check which is the menu item selected
        switch (menuItem.getItemId()){

            //third party requests fragment
            case R.id.nav_tpreq:

                //creating request fragment only if the current isnt already it
                if(navigationView.getCheckedItem().getItemId() != R.id.nav_tpreq){


                    FragmentTransaction tpTransaction= getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, new RequestFragment());

                    tpTransaction.addToBackStack(null);
                    tpTransaction.commit();

                    navigationView.setCheckedItem(R.id.nav_tpreq);

                }


                break;

            case R.id.nav_mydata:

                //creating mydata fragment only if the current isnt already it
                if(navigationView.getCheckedItem().getItemId() != R.id.nav_mydata){

                    FragmentTransaction mydTransaction= getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new MyDataFragment());

                    mydTransaction.addToBackStack(null);
                    mydTransaction.commit();

                    navigationView.setCheckedItem(R.id.nav_mydata);
                }
               break;

            case R.id.nav_logout:

                //cancelJob();

                SharedPreferences myPrefs = getSharedPreferences("myPrefs",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


        }


        //closing the drawer and returning
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    
    private void cancelJob(){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "cancelJob: JobCancelled");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed(){

        //if the drawer is opened the back closes it otherwise it does the
        //predefined back action

        if(drawer.isDrawerOpen(GravityCompat.START)){

            //drawer open + back -> drawer close
            drawer.closeDrawer(GravityCompat.START);
        }else{

            //setting the back action both for returning back to a fragment
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {

                //no fragment to go back to
                super.onBackPressed();
            }
        }
    }
}




