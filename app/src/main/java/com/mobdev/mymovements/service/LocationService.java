package com.mobdev.mymovements.service;

import android.Manifest;
import android.app.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.mobdev.mymovements.Activity.MainActivity;
import com.mobdev.mymovements.utility.Utility;
import com.mobdev.mymovements.fragment.MapFragment;
import com.mobdev.mymovements.R;
import com.mobdev.mymovements.model.PointOfInterest;

import db.PointOfInterestManager;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    public static final String NEW_VALUE_INTENT_ACTION = "service_new_value";
    public static final String INTENT_LATITUDE = "latitude";
    public static final String INTENT_LONGITUDE = "longitude";
    public static final String INTENT_SPEED = "speed";
    public static final String INTENT_ACCURACY = "accuracy";
    public String name;
    private static final long TWO_MINUTES = 1000 * 60 * 2;
    private static final long INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 2000;
    private int ONGOING_NOTIFICATION = 1111;

    private Location currentLocation;
    NotificationCompat.Builder builder = null;

    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(LocationService.TAG, "LocationService ---> onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(LocationService.TAG, "LocationService ---> onDestroy()");
        super.onDestroy();
        stopForeground(true);
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }

    /*
        Recuperiamo il nome prima di attivare il servizio
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            name = intent.getStringExtra("name");
            Log.d(LocationService.TAG, "LocationService ---> onStartCommand()");


        }
        startServiceTask();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LocationService.TAG, "LocationService ---> onUnbind()");
        return super.onUnbind(intent);
    }

    /*
        Richiamiamo i metodi per settare correttamente il servizio
     */
    private void startServiceTask() {
        Log.d(LocationService.TAG, "GpsService ---> Starting ...");

        setServiceAsForeground();

        initFusedLocationProvider();

        Log.d(LocationService.TAG, "MyStartedService ---> Starting ...");


    }

    /*
        Inizializziamo il fusedlocationprovider settandone i parametri principali
     */
    private void initFusedLocationProvider() {
        if (Utility.hasLocationPermission(this)) {
            mFusedLocationClient = new FusedLocationProviderClient(this);

            mLocationRequest = new LocationRequest();

            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (currentLocation == null)
                        currentLocation = locationResult.getLastLocation();

                    else if (isBetterLocation(locationResult.getLastLocation(), currentLocation)) {
                        Log.d(TAG, "onLocationChanged(): Updating Location ... " + currentLocation.getProvider());
                        currentLocation = locationResult.getLastLocation();
                    }
                    notifyValueUpdate();
                }
            }, getMainLooper());
        }
    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /*
        Impostiamo il servizio in foreground creando la notifica permanente
     */
    private void setServiceAsForeground() {
        Log.d(LocationService.TAG, "GpsService ---> setServiceAsForeground()");

        // Prepare the intent triggered if the notification is selected
        final Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
        // Build the notification
        // Use NotificationCompat.Builder instead of just Notification.Builder to support older Android versions
        Notification notification = builder.setContentTitle("MyMovements")
                .setSmallIcon(getNotificationIcon())
                .setBadgeIconType(getNotificationIcon())
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(ONGOING_NOTIFICATION, notification, FOREGROUND_SERVICE_TYPE_LOCATION);
        }
        else {
            startForeground(ONGOING_NOTIFICATION,notification);
        }
    }

    /*
        A seconda dell'attività che viene svolta dall'utente verrà cambiata l'icona di notifica
     */
    private int getNotificationIcon(){

        SharedPreferences settings = this.getSharedPreferences(MainActivity.PREFERENCE,0);
        String icon = settings.getString(MapFragment.NOTIFICATION_PREF, " ");
        switch (icon) {
            case "Walking":
                return R.drawable.ic_shoes_black;
            case "Public Transportation":
                return R.drawable.ic_baseline_directions_bus_24_black;
            case "Driving":
                return R.drawable.ic_baseline_directions_car_24_black;
            default:
                return 0;
        }
    }

    /*
        Update del testo presente nella notifica: aggiorniamo le coordinate per mostrarle all'utente anche in foreground
     */
    private void updateNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences settings = this.getSharedPreferences(MainActivity.PREFERENCE,0);
        String icon = settings.getString(MapFragment.NOTIFICATION_PREF, " ");
        String conc = icon  + "\nLat: " + currentLocation.getLatitude() + " " + "Lng: " + currentLocation.getLongitude();
        Notification notification = builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(conc))
                .setContentText(conc).build();
        notificationManager.notify(ONGOING_NOTIFICATION, notification);
    }

    /*
        Quando viene trovato un nuovo punto di localizzazione viene inserito nel db
     */

    private void notifyValueUpdate(){
        Log.d(TAG, "MyStartedService ---> notifyValueUpdate()");

        if(currentLocation != null) {
            updateNotification();
            Log.d(LocationService.TAG, currentLocation.getProvider());
            PointOfInterest poi = new PointOfInterest(name, currentLocation.getLatitude(), currentLocation.getLongitude(),
                                                    currentLocation.getSpeed(), currentLocation.getAccuracy(), "time");

            PointOfInterestManager.getInstance(this).addPointOfInterestToHead(poi);
        }
    }
}
