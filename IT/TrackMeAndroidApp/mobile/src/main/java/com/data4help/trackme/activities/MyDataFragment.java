package com.data4help.trackme.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import java.util.concurrent.ThreadLocalRandom;
import model.IndividualData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;


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
     * Boolean used to store user location permission status
     */
    private boolean locationAllowed;





    private View view;

    /**
     * Button used to send data to the server
     */
    private FloatingActionButton button;



    /**
     * Client used to communicate with the server
     */
    private RetrofitClient rClient;


    @Override
    public void onPause() {

        locationManager.removeUpdates(locationListener);

        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //handling user localization
        locationManager = (LocationManager) inflater.getContext().getSystemService(LOCATION_SERVICE);

        //setting up the listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //     Log.d("Location: ", location.toString());
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



        view = inflater.inflate(R.layout.fragment_mydata, container, false);

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


        bindView();

        //binding menu button
        buttonClick();


        return view;

    }





    private void bindView(){

        //binding the view
        button = view.findViewById(R.id.send_data_button);
        heartbeat = view.findViewById(R.id.heartText);
        sleepTime = view.findViewById(R.id.sleepText);
        bloodPressure = view.findViewById(R.id.bloodText);
        steps = view.findViewById(R.id.stepText);
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
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        data.setUsername(preferences.getString("username", ""));
        String token = "Bearer " + preferences.getString("token", "");

        callApi(data, token);

    }

    private void callApi(final IndividualData data, final String token){
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
                    bloodPressure.setText(String.valueOf(data.getBloodPressureMax()) + "/" +
                            String.valueOf(data.getBloodPressureMin()));
                    bloodPressure.append(" mmHg");
                    steps.setText(String.valueOf(data.getSteps()));
                    steps.append(" steps");
                    sleepTime.setText(data.getSleepTime());
                    sleepTime.append(" time asleep");


                    //notification of success
                    Toast.makeText(getContext(), "Data uploaded",
                            Toast.LENGTH_SHORT).show();

                } else {

                    //since some data has timestamp as part of the primary key, sending data in little time
                    //may cause incompatibility problem with the database
                    Log.i("Response message: ", response.message() + " " + response.code());
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
                        //upload data when button is clicked
                        uploadData();

                    }
                }
        );

    }

}
