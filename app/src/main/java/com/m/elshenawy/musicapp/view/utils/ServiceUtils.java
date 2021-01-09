package com.m.elshenawy.musicapp.view.utils;

import android.app.ActivityManager;
import android.content.Context;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ServiceUtils {

    public ServiceUtils() {
    }

    public static boolean isAudioPlayerServiceRunning(Class<?> serviceClass, Context context) {
        boolean serviceState = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                serviceState = true;
            }
        }
        return serviceState;
    }
}
