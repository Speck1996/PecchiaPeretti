package com.data4help.trackme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.data4help.trackme.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import activityhelpers.DatePicker;
import activityhelpers.Encryptor;
import model.Individual;
import model.Sex;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;

import static retrofitclient.RetrofitClient.BASE_URL;

/**
 * Activity containing the form for individual's registration
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * Spinner containing the sex choice
     */
    private Spinner sexSpin;
    /**
     * Field where the individual will insert his name
     */
    private EditText nameText;
    /**
     * Field where the individual will insert his surname
     */
    private EditText surnameText;
    /**
     * Field where the individual will insert his birth date
     */
    private EditText birthDateText;
    /**
     * Spinner containing the sex countries
     */
    private Spinner countrySpin;
    /**
     * Field where the individual will insert his username
     */
    private EditText usernameText;
    /**
     * Field where the individual will insert his email
     */
    private EditText emailText;
    /**
     * Field where the individual will insert his taxcode
     */
    private EditText taxCodeText;
    /**
     * Field where the individual will insert his password
     */
    private EditText passwordText;
    /**
     * Field where the individual will reinsert his password
     */
    private EditText confirmPasswordText;

    /**
     * Button to register
     */
    private CardView button;

    /**
     * Client used to make server calls
     */
    private RetrofitClient rClient;

    /**
     * Preferences used to get the url of the server
     */
    private SharedPreferences preferences;

    /**
     * Tag for the signup activity
     */
    private static final String TAG = "SignUpActivity";

    /**
     * boolean set to true if a necessary form field is empty
     */
    boolean missingField;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_actvity);


        preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);


        //initializing the client
        rClient = RetrofitClient.getInstance(preferences.getString("url",BASE_URL));


        //binding the view items with the attributes
        bindView();


        //action associated to the button tap
        registerButton();
    }

    /**
     * Method used to bind the view with activity attributes
     */
    private void bindView(){


        //view binding with the attributes
        sexSpin = findViewById(R.id.signup_sex);
        nameText = findViewById(R.id.signup_name);
        surnameText = findViewById(R.id.signup_surname);
        birthDateText = findViewById(R.id.signup_dateofbirth);
        countrySpin = findViewById(R.id.signup_country);
        usernameText = findViewById(R.id.signup_username);
        emailText = findViewById(R.id.signup_email);
        taxCodeText = findViewById(R.id.signup_taxcode);
        passwordText = findViewById(R.id.signup_password);
        confirmPasswordText = findViewById(R.id.signup_confirmPassword);
        button = findViewById(R.id.signup_register);

        //date picker is used to have better date selection
        DatePicker picker = new DatePicker(this,R.id.signup_dateofbirth);

    }


    /**
     * Method that takes care of the button tap, sending the given data to the server
     */
    public  void registerButton() {

        //setting the action
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //this boolean will be later used to warn that there is
                        //a mandatory field that is empty
                        missingField = false;

                        Individual individual = fillIndividual();

                        //individual filled
                        if(!missingField && individual!= null) {
                            callApi(individual);
                        }
                    }
                });

    }

    /**
     * Method used to call the server api for the signup process
     * @param individual Object containing all necessary user information
     */
    private void callApi(final Individual individual){
        Call call = rClient.getApi().signup(individual);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {

                //successful response obtained
                if (response.isSuccessful()) {

                    if(preferences.getBoolean("signupLog",false) ){
                        Log.d(TAG, "callApi: user " + individual.toString()+ " successfully registed");
                    }

                    //notification to the user
                    Toast.makeText(SignUpActivity.this, "User registered",
                            Toast.LENGTH_SHORT).show();

                    //user redirected to the login page
                    Intent openStartingPoint = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(openStartingPoint);
                } else {

                    if(preferences.getBoolean("signupLog",false) ){
                        Log.d(TAG, "callApi: error Response message: "+ response.message() + " " + response.code());
                    }

                    //some error occurred
                    Toast.makeText(SignUpActivity.this, "username or taxcode already taken",
                            Toast.LENGTH_SHORT).show();

                }
            }

            //call ailed
            @Override
            public void onFailure(Call call, Throwable t) {

                if(preferences.getBoolean("signupLog",false) ){
                    Log.d(TAG, "callApi: error Response message: " + t.toString());
                }
                Toast.makeText(SignUpActivity.this, "Server not reachable",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Method in charge of taking the input data from the view and creating
     * an {@link Individual}
     * @return the individual created
     */
    private Individual fillIndividual(){

        //filling individual's attribute. Once the filling is terminated
        //the object is sent to the server (if no field is empty)
        Individual individual = new Individual();

        if(!sexSpin.getSelectedItem().toString().equals(R.string.form_sex)) {
            Sex sexValue = Sex.getEnum(sexSpin.getSelectedItem().toString());
            individual.setSex(sexValue);
        }


        individual.setName(nameText.getText().toString());
        individual.setSurname(surnameText.getText().toString());



        if(!countrySpin.getSelectedItem().toString().equals(getString(R.string.insert_country))) {
            individual.setCountry(countrySpin.getSelectedItem().toString());
        }


        if(usernameText.getText().toString().length()> 0) {
            individual.setUsername(usernameText.getText().toString());
        }
        else {
            usernameText.setError("Obligatory field");
            missingField=true;
        }

        if(emailText.getText().toString().length()>0) {
            individual.setEmail(emailText.getText().toString());
        }else{
            emailText.setError("Obligatory field");
            missingField=true;
        }

        if(taxCodeText.getText().toString().length()> 0) {
            individual.setTaxcode(taxCodeText.getText().toString());
        }else{
            taxCodeText.setError("Obligatory field");
            missingField = true;
        }

        //special care for password: checking if the two password
        // fields are filled with the same string
        if(passwordText.getText().toString().length()>0 && confirmPasswordText.getText().toString().length()>0) {

            String password = passwordText.getText().toString();
            String confirmPassword = confirmPasswordText.getText().toString();


            //password matches
            if (password.equals(confirmPassword)) {

                Encryptor encryptor = Encryptor.getInstance();

                String digest =  encryptor.encrypt(password);

                individual.setPassword(digest);

            } else {

                //passwords don't match
                Toast.makeText(SignUpActivity.this, "Passwords do not match!",
                        Toast.LENGTH_SHORT).show();
                missingField= true;
            }
        }else{
            passwordText.setError("Obligatory field");
            confirmPasswordText.setError("Obligatory field");
            missingField = true;
        }



        if(birthDateText.getText().toString().length()>0) {
            //special care needed for the date, since there is some mess in json
            //handling the sql format, for the communication a string is used
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {

                //getting date object from the first string
                Date date = formatter.parse(birthDateText.getText().toString());

                //transforming it in a sql compatible date
                java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());

                //associating it as a string to the individual
                individual.setBirthDate(sqlStartDate.toString());

            } catch (ParseException e) {

                e.printStackTrace();
                return null;
            }
        }
        return individual;
    }
}
