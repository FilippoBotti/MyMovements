package com.mobdev.mymovements.utility;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.mobdev.mymovements.Activity.MainActivity;
import com.mobdev.mymovements.fragment.SettingsFragment;
import com.mobdev.mymovements.model.Route;

import db.RouteManager;

public class MapSnapshotThread extends Thread {
    private static final String TAG = "DatabaseThread";
    private Bitmap bitmap;
    private Route route;
    private Context context;

    public MapSnapshotThread(Bitmap bitmap, Route route, Context context) {
        this.bitmap = bitmap;
        this.route = route;
        this.context = context;
    }

    @Override
    public void run() {
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFERENCE,0);
        int weight = settings.getInt(SettingsFragment.WEIGHT_PREF, 0);
        String name = route.getName();
        double distance = Utility.calculateDistance(name,context);
        route.setDistance(distance);
        route.setAccuracy(Utility.getAverageAccuracy(name, context));
        route.setSpeed(Utility.getAverageSpeed(name,context));
        double calories = 0.0;
        if(route.getTransport().equals("Walking"))
            calories = Utility.calculateCalories(distance, weight);
        route.setCalories(calories);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.7), true);
        route.setImg(bitmap);
        RouteManager.getInstance(context).addRouteToHead(route);
        Log.d("MapFragment", "Stiamo lavoando bene");

    }

}
