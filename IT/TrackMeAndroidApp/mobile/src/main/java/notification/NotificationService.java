package notification;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class NotificationService extends JobService {

    private static final String TAG = "Job";

    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters param){

        doBackgroundWork(param);
        return true;
    }

    private void doBackgroundWork(final JobParameters params){

        new Thread(new Runnable() {
            @Override
            public void run() {

                if(jobCancelled){
                    return;
                }

                for(int i = 0; i < 10; i++){
                    Log.d(TAG, "run: " + i);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Job finished");

                jobFinished(params,false);
            }
        }).start();
    }


    @Override
    public boolean onStopJob(JobParameters param){

        Log.d(TAG, "onStopJob: Jobcancelled before completion");

        jobCancelled= true;
        return true;
    }

}
