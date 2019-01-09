package com.data4help.trackme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.data4help.trackme.R;


import activityhelpers.Encryptor;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;


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
     * Client used to communicate with server API
     */
    private RetrofitClient rClient;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        if (preferences.getBoolean("logged", false)) {
            /* The user has already login, so start the dashboard */
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

            return;
        }



        setContentView(R.layout.activity_main);



        loginButton();
        signUpTap();
    }


    /**
     * Method that takes care of the action bound to the button click
     */
    public  void loginButton() {

        //initializing the client
        rClient = RetrofitClient.getInstance();

        //binding view element with class attributes
        usernameText = findViewById(R.id.username);

        passwordText = findViewById(R.id.password);

        login_btn = findViewById(R.id.loginButton);

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


                            Encryptor encryptor = Encryptor.getInstance();

                            String digest = encryptor.encrypt(password);

                            //object passed to the server
                            User user = new User();
                            user.setUsername(username);
                            user.setPassword(digest);

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
                                        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                                        preferences.edit().putString("token", token).apply();
                                        preferences.edit().putString("username",username).apply();
                                        preferences.edit().putBoolean("logged",true).apply();

                                        Log.d("Loggin: ", username + " with token: " + token);


                                        //notification of success
                                        Toast.makeText(MainActivity.this, "User and Password is correct",
                                                Toast.LENGTH_SHORT).show();

                                        //next screen
                                        Intent openStartingPoint = new Intent(MainActivity.this, HomeActivity.class);
                                        openStartingPoint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(openStartingPoint);
                                    }
                                    else{
                                        //something went wrong when taking the response
                                        Log.i("Response message: ", response.message() + " "+ response.code());

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

                                    Toast.makeText(MainActivity.this, "Server not reachable",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });


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
     * Method that takes care of the action bound to the text click
     */
    public void signUpTap(){
        //binding the view with the attributes
        registerText = findViewById(R.id.signup);

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


