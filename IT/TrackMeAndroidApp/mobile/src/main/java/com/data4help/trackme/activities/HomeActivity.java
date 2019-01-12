package com.data4help.trackme.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

    private MyDataFragment dataFrag;

    private RequestFragment reqFrag;

    private SharedPreferences preferences;

    private static final String TAG = "Home activity";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = this.getSharedPreferences("myPrefs", MODE_PRIVATE);


        createNavigationDrawer();


        //when the app is reloaded or just opened it goes to my data fragment
        if(savedInstanceState == null) {

            if(preferences.getBoolean("mydataLog",false) ){
                Log.d(TAG, "Restoring view from old state");
            }

            dataFrag = new MyDataFragment();

            reqFrag = new RequestFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    dataFrag).commit();

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
                if(navigationView.getCheckedItem().getItemId() != R.id.nav_tpreq ){

                    if(preferences.getBoolean("mydataLog",false) ){
                        Log.d(TAG, "switching to request view");
                    }


                    if(reqFrag!=null) {
                        FragmentTransaction tpTransaction = getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_container, reqFrag);
                        tpTransaction.addToBackStack(null);
                        tpTransaction.commit();

                    }else{
                        reqFrag= new RequestFragment();
                        FragmentTransaction tpTransaction = getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_container, reqFrag);
                        tpTransaction.addToBackStack(null);
                        tpTransaction.commit();
                    }

                }
                break;

            case R.id.nav_mydata:


                //creating request fragment only if the current isnt already it
                if(navigationView.getCheckedItem().getItemId() != R.id.nav_mydata){

                    if(preferences.getBoolean("requestLog",false) ){
                        Log.d(TAG, "switching to mydata");
                    }

                    if(dataFrag!= null) {
                        FragmentTransaction mydTransaction = getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_container, dataFrag);
                        mydTransaction.addToBackStack(null);
                        mydTransaction.commit();
                    }else{
                        dataFrag = new MyDataFragment();
                        FragmentTransaction mydTransaction = getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_container, dataFrag);
                        mydTransaction.addToBackStack(null);
                        mydTransaction.commit();
                    }
                }
               break;

            case R.id.nav_logout:

                if(preferences.getBoolean("requestLog",false)  ){
                    Log.d(TAG, "logging out");
                }
                if(preferences.getBoolean("requestLog",false) ){
                    Log.d(TAG, "logging out");
                }

                preferences.edit().clear().commit();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

        }


        //closing the drawer and returning
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
                super.onBackPressed();

                Fragment currentFrag =   getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if(currentFrag  instanceof MyDataFragment){
                    //setting the current fragment as checked
                    navigationView.setCheckedItem(R.id.nav_mydata);

                }
                else if(currentFrag instanceof RequestFragment){
                    //setting the current fragment as checked
                    navigationView.setCheckedItem(R.id.nav_tpreq);
                }
            }

    }
}




