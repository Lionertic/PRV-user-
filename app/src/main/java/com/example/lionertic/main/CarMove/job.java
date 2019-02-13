package com.example.lionertic.main.CarMove;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class job extends JobIntentService {private static final String TAG = "Worker";
    public static final String RECEIVER = "receiver";
    public static final int SHOW_RESULT = 123;
    /**
     * Result receiver object to send results
     */
    private ResultReceiver mResultReceiver;
    /**
     * Unique job ID for this service.
     */
    static final int DOWNLOAD_JOB_ID = 1000;
    /**
     * Actions download
     */
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, WorkerResultReceiver workerResultReceiver,List<LatLng> l,int no) {
        Intent intent = new Intent(context, job.class);
        intent.putExtra(RECEIVER, workerResultReceiver);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra("lo",(Serializable)l);
        intent.putExtra("no",no);
        enqueueWork(context, job.class, DOWNLOAD_JOB_ID, intent);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork() called with: intent = [" + intent + "]");
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_DOWNLOAD:
                    mResultReceiver = intent.getParcelableExtra(RECEIVER);
                    try {
                        Thread.sleep(2000);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("loca",intent.getSerializableExtra("lo"));
                        bundle.putInt("num",intent.getIntExtra("no",0));
                        mResultReceiver.send(SHOW_RESULT, bundle);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }}
