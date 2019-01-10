package com.data4help.trackme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    boolean missingField;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_actvity);


        //initializing the client
        rClient = RetrofitClient.getInstance();

        //view binding with the attributes
        sexSpin = findViewById(R.id.sex);
        nameText = findViewById(R.id.name);
        surnameText = findViewById(R.id.surname);
        birthDateText = findViewById(R.id.dateofbirth);
        countrySpin = findViewById(R.id.country);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        taxCodeText = findViewById(R.id.taxcode);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.confirmPassword);
        button = findViewById(R.id.register);



        DatePicker picker = new DatePicker(this,R.id.dateofbirth);


        //action associated to the button tap
        registerButton();
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

                        missingField = false;
                        
                        Individual individual = fillIndividual();

                        if(!missingField && individual!=null) {
                            //time to call the api
                            Call call = rClient.getApi().signup(individual);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call call, Response response) {

                                    //successful response obtained
                                    if (response.isSuccessful()) {

                                        //notification to the user
                                        Toast.makeText(SignUpActivity.this, "User registered",
                                                Toast.LENGTH_SHORT).show();

                                        //user redirected to the login page
                                        Intent openStartingPoint = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(openStartingPoint);
                                    } else {

                                        //some error occurred
                                        Log.i("Response message: ", response.message() + " " + response.code());
                                        Toast.makeText(SignUpActivity.this, "username or taxcode already taken",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }

                                //call ailed
                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Toast.makeText(SignUpActivity.this, "Server not reachable",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });

    }


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
