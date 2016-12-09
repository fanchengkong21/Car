package com.kfc.productcar.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

@SuppressLint("Registered")
public class ScheduledService extends Service {
    public static final String LOG_TAG = "MdmService";
    public static final Object mLock = new Object();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || !AlarmExpirationManager.ACTION_SYNC_BROADCAST.equals(intent.getAction())) {
            return super.onStartCommand(intent, flags, startId);
        }

//        BaseApplication.getInstance().clearCacheIfNecessary();
        return super.onStartCommand(intent, flags, startId);
    }
}
