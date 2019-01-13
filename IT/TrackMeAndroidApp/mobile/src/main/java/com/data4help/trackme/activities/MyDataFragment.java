package com.data4help.trackme.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.data4help.trackme.R;
import java.sql.Time;
import java.util.Random;

import model.IndividualData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static retrofitclient.RetrofitClient.BASE_URL;


/**
 * Fragment in charge of handling individual data uploading to the server and its visualization
 */
public class MyDataFragment extends Fragment  {

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
    private TextView heartBeatView;



    /**
     * TextView showing the last registered sleep time
     */
    private TextView sleepTimeView;


    /**
     * TextView showing the last registered blood pressure
     */
    private TextView bloodPressureView;

    /**
     * String to restore the blood pressure last value
     */
    private static final String BP_KEY = "blood";

    /**
     * String to restore the heart rate last value
     */
    private static final String HEART_KEY = "heart";

    /**
     * String to restore  sleep time last value
     */
    private static final String SLEEP_KEY = "sleep";

    /**
     * String to restore steps taken last value
     */
    private static final String STEPS_KEY = "steps";


    /**
     * Text showing the last registered steps
     */
    private TextView stepsView;


    /**
     * Boolean used to store user location permission status
     */
    private boolean locationAllowed;


    private View view;

    /**
     * Button used to send data to the server
     */
    private FloatingActionButton button;

    /**
     * Preference needed to communicate with the server
     */
    private SharedPreferences preferences;


    /**
     * Client used to communicate with the server
     */
    private RetrofitClient rClient;


    /**
     * Tag for the data fragment
     */
    private final static  String TAG = "MyData Fragment";


    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {

        //stopping location updates
        locationManager.removeUpdates(locationListener);

        super.onPause();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);

        //handling user localization
        locationManager = (LocationManager) inflater.getContext().getSystemService(LOCATION_SERVICE);

        //setting up the listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(preferences.getBoolean("activityLog",false) ){
                    Log.d(TAG, "onLocationChanged: location" +  location.toString());
                }
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
        if (ActivityCompat.checkSelfPermission(inflater.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            locationAllowed = false;
        }else{

            //setting up the providers
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        if(locationAllowed) {

            //this repetition of code is needed to handle change in status provider
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }


        setRetainInstance(true);

        //initializing the client
        rClient = RetrofitClient.getInstance(preferences.getString("url",BASE_URL));


        if(view == null) {
            view = inflater.inflate(R.layout.fragment_mydata, container, false);

            bindView();

            //binding menu button
            buttonClick();
        }

        if(savedInstanceState!=null){
            restoreView(savedInstanceState);
        }


        return view;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        String heartString = heartBeatView.getText().toString();
        outState.putString(HEART_KEY, heartString);

        String bpString = bloodPressureView.getText().toString();
        outState.putString(BP_KEY, bpString);

        String sleepString = sleepTimeView.getText().toString();
        outState.putString(SLEEP_KEY, sleepString);

        String stepsString = stepsView.getText().toString();
        outState.putString(STEPS_KEY, stepsString);


    }

    /**
     * Method used to restore the view from saved instances
     * @param savedInstanceState the instance from wich the view will be restored
     */
    private void restoreView(Bundle savedInstanceState){

        String heartString = savedInstanceState.getString(HEART_KEY);
        heartBeatView.setText(heartString);


        String bpString = savedInstanceState.getString(BP_KEY);
        bloodPressureView.setText(bpString);


        String sleepString = savedInstanceState.getString(SLEEP_KEY);
        sleepTimeView.setText(sleepString);

        String stepsString = savedInstanceState.getString(STEPS_KEY);
        sleepTimeView.setText(stepsString);

    }



    /**
     * Method used to bind the view with activity attributes
     */
    private void bindView(){

        //binding the view
        button = view.findViewById(R.id.frag_send_data_button);
        heartBeatView = view.findViewById(R.id.frag_heartText);
        sleepTimeView = view.findViewById(R.id.frag_sleepText);
        bloodPressureView = view.findViewById(R.id.frag_bloodtext);
        stepsView = view.findViewById(R.id.frag_stepText);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationAllowed = true;

            }
        }
    }


    /**
     * This method retrieves eHealthData, endorse it with the position, if accessible, and then
     * sends it to the server
     */
    public void uploadData() {


        //MOCKUP VALUES: this part of the code will be substituted with wearable
        //data download, eventually
        Random rand = new Random();

        int hbMean = preferences.getInt("hbMean",70);
        int hbSD = preferences.getInt("hbSD", 10);

        //Generating some random data
        int heartBeatValue = (int) Math.round( rand.nextGaussian() * hbSD + hbMean);

        long sleepMean = preferences.getLong("sleepMean",25200000);
        long sleepSD = preferences.getLong("sleepSD", 7200000);

        Time sleepTimeValue = new Time( Math.round( rand.nextGaussian() *sleepSD+ sleepMean));


        int bpMinMean = preferences.getInt("bpMinMean",80);
        int bpMinSD = preferences.getInt("bpMinSD", 10);

        int bloodPressureMinValue = (int) Math.round( rand.nextGaussian() * bpMinSD + bpMinMean);

        int bpMaxMean = preferences.getInt("bpMaxMean",120);
        int bpMaxSD = preferences.getInt("bpMaxSD", 10);

        int bloodPressureMaxValue = (int) Math.round( rand.nextGaussian() * bpMaxSD + bpMaxMean);


        int stepMean = preferences.getInt("stepMean",10000);
        int stepSD = preferences.getInt("stepSD", 2000);

        int stepsValue = (int) Math.round( rand.nextGaussian() *stepSD + stepMean);

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
        data.setUsername(preferences.getString("username", ""));
        String token = "Bearer " + preferences.getString("token", "");


        if(preferences.getBoolean("activityLog",false) ){
            Log.d(TAG, "uploadData: sending " +  data.toString() + " with token: " + token);
        }

        callApi(data, token);

    }

    /**
     * Method used to call the api of the server, in order to send the data
     * @param data Data sent to the server
     * @param token the token needed to access protected resources
     */
    private void callApi(final IndividualData data, final String token){


        //calling the server
        Call call = rClient.getApi().sendData(data, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {

                //successful response obtained
                if (response.isSuccessful()) {


                    if(preferences.getBoolean("activityLog",false) ){
                        Log.d(TAG, "callApi: data successfully sent");
                    }

                    //visualizing data
                    heartBeatView.setText(String.valueOf(data.getHeartRate()));
                    heartBeatView.append(" bpm");
                    bloodPressureView.setText(String.valueOf(data.getBloodPressureMax()) + "/" +
                            String.valueOf(data.getBloodPressureMin()));
                    bloodPressureView.append(" mmHg");
                    stepsView.setText(String.valueOf(data.getSteps()));
                    stepsView.append(" steps");

                    sleepTimeView.setText(data.getSleepTime());
                    sleepTimeView.append(" time asleep");


                    //notification of success
                    Toast.makeText(getContext(), "Data uploaded",
                            Toast.LENGTH_SHORT).show();

                } else {

                    if(preferences.getBoolean("activityLog",false) ){
                        Log.d(TAG, "callApi: error response " + response.message() + " " + response.code());
                    }

                    //since some data has timestamp as part of the primary key, sending data in little time
                    //may cause incompatibility problem with the database
                    if(response.code()==500) {
                        Toast.makeText(getContext(), "Too many attempt in little time, please wait " +
                                        "a second before clicking again",
                                Toast.LENGTH_SHORT).show();
                    }
                }


            }

            //something went wrong when calling the server
            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getContext(), "Server not reachable",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * This method set the button press action for the button in the view
     */
    private void buttonClick() {


        //setting the action on click
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        button.setEnabled(false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                button.setEnabled(true);
                            }
                        }, 999);
                        //upload data when button is clicked
                        uploadData();

                    }
                }
        );

    }
}
