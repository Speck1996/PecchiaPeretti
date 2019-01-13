package com.data4help.trackme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.data4help.trackme.R;


import activityhelpers.Encryptor;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;

import static retrofitclient.RetrofitClient.BASE_URL;


/**
 * Class of the main screen, from which the user can login or access the sign up form
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Field where the user will insert his username
     */
    private EditText usernameText;

    /**
     * Field where the user will insert his password
     */
    private EditText passwordText;

    /**
     * Text representing the door for the sign up form
     */
    private TextView registerText;

    /**
     * Button used to send the data to the server
     */
    private CardView login_btn;

    /**
     * Logo of the screen, used to access the developer screend
     */
    private ImageView logoView;

    /**
     * Preferences used to store individual's data and set the url for the server
     */
    private SharedPreferences preferences;

    /**
     * Counter for logo taps
     */
    private int logoTaps = 0;

    /**
     * Threshold used to access the developer screen
     */
    private final static int DEVTAP = 10;


    /**
     * Client used to communicate with server API
     */
    private RetrofitClient rClient;

    /**
     * Tag for the log
     */
    private static final String TAG = "Main activity";


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        if (preferences.getBoolean("logged", false)) {


            if(preferences.getBoolean("activityLog",false) ){
                Log.d(TAG, "onCreate: user already logged switching immediatly to homepage");
            }

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);


            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            /* The user has already login, so start the dashboard */
            startActivity(intent);

            return;
        }


        //initializing the client
        rClient = RetrofitClient.getInstance(preferences.getString("url",BASE_URL));

        setContentView(R.layout.activity_main);


        bindView();

        logoTap();

        loginButton();
        signUpTap();
    }



    /**
     * Method that takes care of the action bound to the button click
     */
    public  void loginButton() {


        String customUrl = preferences.getString("url",BASE_URL);
        if(!customUrl.equals("http://10.0.2.2:8080/trackme/rest/")){

            rClient.clearClient();
            rClient = RetrofitClient.getInstance(customUrl);
        }


        //setting the click action
        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //taking the fields value
                        final String username = usernameText.getText().toString();


                        String password = passwordText.getText().toString();

                        //fields filled
                        if(!username.isEmpty() && !password.isEmpty()){

                            boolean encryption = preferences.getBoolean("encryption", false);





                            //object passed to the server
                            User user = new User();
                            user.setUsername(username);

                            if(encryption) {

                                if(preferences.getBoolean("activityLog",false) ){
                                    Log.d(TAG, "loginButton: password encrypted");
                                }

                                Encryptor encryptor = Encryptor.getInstance();

                                String digest = encryptor.encrypt(password);
                                user.setPassword(digest);

                            }else{
                                user.setPassword(password);
                            }

                            //calling the server
                            callApi(user);


                        } else {
                            //User clicked login without filling the fields
                            Toast.makeText(MainActivity.this, "Please fill the fields ",
                                    Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
        );
    }


    /**
     * Method used to bind the view with activity attributes
     */
    private void bindView(){


        //binding view element with class attributes
        logoView = findViewById(R.id.main_appLogo);
        registerText = findViewById(R.id.main_signup);
        usernameText = findViewById(R.id.main_username);
        passwordText = findViewById(R.id.main_password);
        login_btn = findViewById(R.id.main_login);
    }


    /**
     * Method in charge of handling the server api call, in this particular case
     * it handles the registration of new users
     * @param user needed to register the user
     */
    private void callApi(final User user){


        //setting the call to the corresponding api
        Call call = rClient.getApi().login(user);

        //calling
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call call, Response response) {

                //successful response
                if (response.isSuccessful() && response.body() != null) {

                    //taking the token
                    String token = response.body().toString();

                    //storing it in the shared preferences in order to take it
                    //when needed
                    preferences.edit().putString("token", token).apply();
                    preferences.edit().putString("username", user.getUsername()).apply();
                    preferences.edit().putBoolean("logged",true).apply();



                    if(preferences.getBoolean("activityLog",false) ){
                        Log.d(TAG, "CallApi: user.getUsername() "+ "with token: " + token);
                    }



                    //notification of success
                    Toast.makeText(MainActivity.this, "User and Password is correct",
                            Toast.LENGTH_SHORT).show();

                    //next screen
                    Intent openStartingPoint = new Intent(MainActivity.this, HomeActivity.class);
                    openStartingPoint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(openStartingPoint);
                }
                else{

                    if(preferences.getBoolean("activityLog",false) ){
                        Log.d(TAG, "CallApi: error response" + response.message() + " "+ response.code());
                    }


                    if(response.code() == 403){
                        Toast.makeText(MainActivity.this, "User or Password is not correct",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(response.code() == 404){
                        Toast.makeText(MainActivity.this, "Service not reachable",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Something went wrong",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            //something went wrong when calling the server
            @Override
            public void onFailure(Call call, Throwable t) {

                if(preferences.getBoolean("activityLog",false) ){
                    Log.d(TAG, "CallApi: error contacting server" + t.toString() + " with url" +
                            preferences.getString("url",""));
                }

                Toast.makeText(MainActivity.this, R.string.error_server,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method used to set up the logo tap functionality
     */
    private void logoTap(){

        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoTaps ++;

                if(logoTaps > DEVTAP){

                    logoTaps = 0;
                    //notification of success
                    Toast.makeText(MainActivity.this, "Developer Screen Activated",
                            Toast.LENGTH_SHORT).show();

                    //next screen
                    Intent openStartingPoint = new Intent(MainActivity.this, DeveloperActivity.class);
                    startActivity(openStartingPoint);
                }
            }
        });

    }


    /**
     * Method that takes care of the action bound to the text click
     */
    public void signUpTap(){

        //going to the signup screen
        registerText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent openStartingPoint = new Intent(MainActivity.this, SignUpActivity.class);
                        startActivity(openStartingPoint);
                    }
                }
        );
    }

}


