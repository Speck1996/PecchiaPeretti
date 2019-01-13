package com.data4help.trackme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.data4help.trackme.R;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Class for the developer screen, in which the user will set some parameters
 */
public class DeveloperActivity extends AppCompatActivity {

    /**
     * Edit text for the server url
     */
   private EditText serverUrl;

    /**
     * Edit text for the heart beat mean value
     */
    private EditText hbMean;

    /**
     * Edit text for the heart beat standard deviation value
     */
    private EditText hbSD;

    /**
     * Edit text for the systolic mean value of blood pressure
     */
    private EditText maxBpMean;

    /**
     * Edit text for the systolic standard deviation value of blood pressure
     */
    private EditText maxBpSD;

    /**
     * Edit text for the diastolic mean value of blood pressure
     */
    private EditText minBpMean;

    /**
     * Edit text for the diastolic standard deviation value of blood pressure
     */
    private EditText minBpSD;


    /**
     * Edit text for the sleep mean value
     */
    private EditText sleepMean;

    /**
     * Edit text for the sleep standard deviation value
     */
    private EditText sleepSD;


    /**
     * Edit text for the steps mean value
     */
    private EditText stepsMean;

    /**
     * Edit text for the steps standard deviation value
     */
    private EditText stepsSD;

    /**
     * Switch used to enable MainActivity Log
     */
    private Switch activitySwitch;

    /**
     * Switch used to enable SignUpActivity Log
     */
    private Switch signupSwitch;

    /**
     * Switch used to enable MyDataActivity Log
     */
    private Switch mydataSwitch;

    /**
     * Switch used to enable RequestActivity Log
     */
    private Switch requestsSwitch;

    /**
     * Switched used to enable password encryption
     */
    private Switch encryptionSwitch;

    /**
     * Button used to apply user choices
     */
    private MaterialButton confirmButton;

    /**
     * Button used to reset the configuration to the standard one
     */
    private MaterialButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        bindView();

        setButtons();
    }

    /**
     * Method used to bind the view elements with activity attributes
     */
    private void bindView(){

        serverUrl = findViewById(R.id.dev_serv);
        hbMean = findViewById(R.id.dev_hb_mean);
        hbSD = findViewById(R.id.dev_hb_sd);
        maxBpMean = findViewById(R.id.dev_bpmax_mean);
        maxBpSD = findViewById(R.id.dev_bpmax_sd);
        minBpMean = findViewById(R.id.dev_bpmin_mean);
        minBpSD = findViewById(R.id.dev_bpmin_sd);
        sleepMean = findViewById(R.id.dev_sleep_mean);
        sleepSD = findViewById(R.id.dev_sleep_sd);
        stepsMean = findViewById(R.id.dev_steps_mean);
        stepsSD = findViewById(R.id.dev_step_sd);
        activitySwitch = findViewById(R.id.main_log);
        signupSwitch = findViewById(R.id.signup_log);
        mydataSwitch = findViewById(R.id.mydata_log);
        requestsSwitch = findViewById(R.id.req_log);
        encryptionSwitch = findViewById(R.id.dev_encrswitch);
        confirmButton = findViewById(R.id.dev_confirm);
        resetButton = findViewById(R.id.dev_reset);

    }

    /**
     * Method used to set click actions for buttons
     */
    private  void setButtons(){

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("myPrefs",MODE_PRIVATE);

                try {

                    //retrieving and setting values from the edit texts
                    if(hbMean.getText().toString().trim().length() > 0) {
                        int hbMeanValue = Integer.parseInt(hbMean.getText().toString());
                        preferences.edit().putInt("hbMean",hbMeanValue).apply();
                    }

                    if(hbSD.getText().toString().trim().length()>0) {
                        int hbSdValue = Integer.parseInt(hbSD.getText().toString());
                        preferences.edit().putInt("hbSd", hbSdValue).apply();
                    }

                    if(maxBpMean.getText().toString().trim().length()>0) {
                        int maxBpMeanValue = Integer.parseInt(maxBpMean.getText().toString());
                        preferences.edit().putInt("bpMaxMean", maxBpMeanValue).apply();
                    }

                    if(maxBpSD.getText().toString().trim().length()>0) {
                        int maxBpSdValue = Integer.parseInt(maxBpSD.getText().toString());
                        preferences.edit().putInt("bpMaxSD",maxBpSdValue).apply();
                    }

                    if(minBpMean.getText().toString().trim().length()>0) {
                        int minBpMeanValue = Integer.parseInt(minBpMean.getText().toString());
                        preferences.edit().putInt("bpMinMean", minBpMeanValue).apply();
                    }

                    if(minBpSD.getText().toString().trim().length()>0) {
                        int minBpSdValue = Integer.parseInt(minBpSD.getText().toString());
                        preferences.edit().putInt("bpMinSD", minBpSdValue).apply();
                    }

                    if(sleepMean.getText().toString().trim().length()>0) {
                        int sleepMeanValue = Integer.parseInt(sleepMean.getText().toString());
                        preferences.edit().putLong("sleepMean", sleepMeanValue).apply();

                    }

                    if(sleepSD.getText().toString().trim().length()>0) {
                        int sleepSdValue = Integer.parseInt(sleepSD.getText().toString());
                        preferences.edit().putLong("sleepSD",sleepSdValue).apply();
                    }

                    if(stepsMean.getText().toString().trim().length()>0) {
                        int stepMeanValue = Integer.parseInt(stepsMean.getText().toString());
                        preferences.edit().putLong("stepMean",stepMeanValue).apply();
                    }

                    if(stepsSD.getText().toString().trim().length()>0) {
                        int stepSd = Integer.parseInt(stepsSD.getText().toString());
                        preferences.edit().putLong("stepSD", stepSd).apply();
                    }

                }catch (Exception e){
                    //notification of success
                    Toast.makeText(DeveloperActivity.this, "Please put integer values",
                            Toast.LENGTH_SHORT).show();
                }

                //retrieving and setting values for the switchs
                boolean activityLog = activitySwitch.isChecked();
                boolean signupLog = signupSwitch.isChecked();
                boolean mydataLog = mydataSwitch.isChecked();
                boolean requestLog = requestsSwitch.isChecked();
                boolean encryption = encryptionSwitch.isChecked();

                preferences.edit().putBoolean("activityLog", activityLog).apply();
                preferences.edit().putBoolean("signupLog",signupLog).apply();
                preferences.edit().putBoolean("mydataLog",mydataLog).apply();
                preferences.edit().putBoolean("requestLog",requestLog).apply();
                preferences.edit().putBoolean("encryption", encryption).apply();


                //setting the serverl url
                String serverString = serverUrl.getText().toString();

                if(serverString.length()>0){

                    UrlValidator urlValidator = new UrlValidator();

                    if(urlValidator.isValid(serverString)) {
                        preferences.edit().putString("url", serverString).apply();
                    }else{
                        Toast.makeText(DeveloperActivity.this, "Please  insert a valid url",
                                Toast.LENGTH_SHORT).show();

                        return;
                    }
                }

                Intent intent = new Intent(DeveloperActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("myPrefs",MODE_PRIVATE);

                preferences.edit().clear().apply();

            }
        });
    }
}
