package com.data4help.trackme.activities;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.data4help.trackme.activities.HomeActivity.TAG;
import static com.data4help.trackme.activities.HomeActivity.mClient;

public class VerifyDataTask extends AsyncTask<Void, Void, Void> {



    protected Void doInBackground(Void... params) {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        Log.i("TASK", "\tStart: " + new Date(startTime).toString());
        Log.i("TASK", "\tEnd: " + new Date(endTime).toString());

        /*Calendar cal = Calendar.getInstance();
        Date now = new Date();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        long endTime = cal.getTimeInMillis();

        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long startTime = cal.getTimeInMillis();*/


        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataSource ESTIMATED_BP = new DataSource.Builder()
                .setDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("blood_pressure")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readReq = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .read(HealthDataTypes.TYPE_BLOOD_PRESSURE)
                .setTimeRange(startTime,endTime,TimeUnit.MILLISECONDS)
                .build();






        PendingResult<DataReadResult> pendingResult = Fitness.HistoryApi.readData(mClient,readReq);

        pendingResult.setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(@NonNull DataReadResult dataReadResult) {


                if(dataReadResult.getBuckets().size()>0){
                    for(Bucket bucket : dataReadResult.getBuckets()){
                        List<DataSet> dataSets = bucket.getDataSets();
                        for(DataSet dataSet: dataSets){
                            Log.i("TASK", "result received");

                            processDataSet(dataSet);
                        }
                    }
                }
            }
        });






        return null;
    }


    private void processDataSet(DataSet dataSet){

        for(DataPoint dp: dataSet.getDataPoints()){

            long dpStart = dp.getStartTime(TimeUnit.MINUTES);
            long dpEnd = dp.getEndTime(TimeUnit.MINUTES);

            Log.i("TASK", "Data point:");
            Log.i("TASK", "\tType: " + dp.getDataType().getName());
            Log.i("TASK", "\tStart: " + new Date(dpStart).toString());
            Log.i("TASK", "\tEnd: " + new Date(dpEnd).toString());

            for(Field field: dp.getDataType().getFields()){
                String fieldName = field.getName();
                Log.i("TASK", "\tField: " + fieldName + " Value: " + dp.getValue(field));
            }

        }
    }
}