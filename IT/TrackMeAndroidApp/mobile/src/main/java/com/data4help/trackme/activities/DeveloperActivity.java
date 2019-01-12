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

public class DeveloperActivity extends AppCompatActivity {

    EditText serverUrl;

    EditText hbMean;

    EditText hbSD;

    EditText maxBpMean;

    EditText maxBpSD;

    EditText minBpMean;

    EditText minBpSD;

    EditText sleepMean;

    EditText sleepSD;

    EditText stepsMean;

    EditText stepsSD;

    Switch activitySwitch;

    Switch signupSwitch;

    Switch mydataSwitch;

    Switch requestsSwitch;

    MaterialButton confirmButton;

    MaterialButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        bindView();

        setButtons();
    }

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
        confirmButton = findViewById(R.id.dev_confirm);
        resetButton = findViewById(R.id.dev_reset);

    }

    private  void setButtons(){

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("myPrefs",MODE_PRIVATE);

                try {

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


                boolean activityLog = activitySwitch.isChecked();
                boolean signupLog = signupSwitch.isChecked();
                boolean mydataLog = mydataSwitch.isChecked();
                boolean requestLog = requestsSwitch.isChecked();

                preferences.edit().putBoolean("activityLog", activityLog).apply();
                preferences.edit().putBoolean("signupLog",signupLog).apply();
                preferences.edit().putBoolean("mydataLog",mydataLog).apply();
                preferences.edit().putBoolean("requestLog",requestLog).apply();


                String serverString = serverUrl.getText().toString();

                if(serverString.length()>0){
                    if(URLUtil.isHttpUrl(serverString)) {
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
