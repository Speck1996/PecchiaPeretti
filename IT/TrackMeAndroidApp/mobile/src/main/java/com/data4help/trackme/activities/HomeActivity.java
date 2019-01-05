package com.data4help.trackme.activities;

import android.Manifest;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.data4help.trackme.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import model.IndividualData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;

/**
 * Class in charge of handling home page activity: sending data to the server.
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Location object returned by the manager through the listener
     */
    private Location mLocation;

    /**
     * Object needed to access location
     */
    private LocationManager locationManager;

    /**
     * Object needed to listen to location changes
     */
    private LocationListener locationListener;


    /**
     * TextView showing the last registered heartbeat value
     */
    private TextView heartbeat;

    /**
     * TextView showing the last registered sleep time
     */
    private TextView sleepTime;

    /**
     * TextView showing the last registered blood pressure
     */
    private TextView bloodPressure;

    /**
     * Text showing the last registered steps
     */
    private TextView steps;

    /**
     * Drawer layout
     */
    private DrawerLayout mDrawerLayout;

    /**
     * Button used to send data to the server
     */
    private FloatingActionButton button;



    /**
     * Client used to communicate with the server
     */
    private RetrofitClient rClient;

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
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        //handling user localization
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //setting up the listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location: ", location.toString());
               mLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //asking for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }else{
            //setting up the providers
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        //this repetition of code is needed to handle change in permission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        //first upload
        uploadData();


        //binding menu button
        buttonClick();


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method retrieves eHealthData, endorse it with the position, if accessible, and then
     * sends it to the server
     */
    public void uploadData() {


        //binding the view
        heartbeat = findViewById(R.id.heartText);
        sleepTime = findViewById(R.id.sleepText);
        bloodPressure = findViewById(R.id.bloodText);
        steps = findViewById(R.id.stepText);

        //initializing the client
        rClient = RetrofitClient.getInstance();

        //MOCKUP VALUES: this part of the code will be substituted with wearable
        //data download, eventually
        Random rand = new Random();


        //Generating some random data
        int heartBeatValue = rand.nextInt((220 - 30) + 1) + 30;
        Time sleepTimeValue = new Time(ThreadLocalRandom.current().nextLong(86400000L));
        int bloodPressureMinValue = rand.nextInt((160 - 70) + 1) + 70;
        int bloodPressureMaxValue = rand.nextInt((160 - bloodPressureMinValue) + 1) + bloodPressureMinValue;
        int stepsValue = rand.nextInt(70000);

        //filling the object that will be sent  to the server
        final IndividualData data = new IndividualData();

        data.setBloodPressureMin(bloodPressureMinValue);
        data.setBloodPressureMax(bloodPressureMaxValue);
        data.setHeartRate(heartBeatValue);
        data.setSteps(stepsValue);
        data.setSleepTime(sleepTimeValue.toString());

        //ok location found, filling the object with it
        if(mLocation != null) {
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();
            data.setLatitude(String.valueOf(latitude));
            data.setLongitude(String.valueOf(longitude));
        }

        //retrieving the token and the username from the shared preferences
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        data.setUsername(preferences.getString("username", ""));
        String token = "Bearer " + preferences.getString("token", "");

        //calling the server
        Call call = rClient.getApi().sendData(data, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {

                //successful response obtained
                if (response.isSuccessful()) {

                    //visualizing data
                    heartbeat.setText(String.valueOf(data.getHeartRate()));
                    heartbeat.append(" bpm");
                    bloodPressure.setText(String.valueOf(data.getBloodPressureMax()) + "/" + String.valueOf(data.getBloodPressureMin()));
                    bloodPressure.append(" mmHg");
                    steps.setText(String.valueOf(data.getSteps()));
                    steps.append(" steps");
                    sleepTime.setText(data.getSleepTime());
                    sleepTime.append(" time asleep");


                    //notification of success
                    Toast.makeText(HomeActivity.this, "Data uploaded",
                            Toast.LENGTH_SHORT).show();

                } else {

                    //server made some mess while handling data
                    Log.i("Response message: ", response.message() + " " + response.code());
                    if(response.code()==500) {
                        Toast.makeText(HomeActivity.this, "Too many attempt in little time, please wait" +
                                        "a second before clicking again",
                                Toast.LENGTH_SHORT).show();
                    }
                }


            }

            //something went wrong when calling the server
            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Server not reachable",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * This method set the button press action for the button in the view
     */
    private void buttonClick() {

        //binding the view
        button = findViewById(R.id.send_data_button);

        //setting the action on click
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //upload data when button is clicked
                        uploadData();

                    }
                }
        );

    }


}




