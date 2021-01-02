package com.mobdev.mymovements.utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;
import com.mobdev.mymovements.model.PointOfInterest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import db.PointOfInterestManager;
import db.RouteManager;

public final class Utility {

    /*
        Questo metodo permette di verificare se sono stati accettati i permessi di localizzazione
     */
    public static boolean hasLocationPermission(Context context){
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)){
            return true;
        }
       return false;
    }

    /*
       Questo metodo permette di verificare se sono stati accettati i permessi di scrittura
       sulla memoria
    */

    public static boolean hasStoragePermission(Context context){
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        return false;
    }

    /*
        Questo metodo permette di convertire file bitmap in byte
     */
    @TypeConverter
    public static byte[] fromBitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    /*
        Questo metodo permette di creare una bitmap da bytes
     */

    @TypeConverter
    public static Bitmap fromByteToBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    /*
        Questo metodo permette di calcolare la distanza in km di una route
     */
    public static double calculateDistance(String name, Context context){
        ArrayList<PointOfInterest> myPois = PointOfInterestManager.getInstance(context).getPointOfInterestByName(name);
        double sum =0;
        ArrayList<Double> results = new ArrayList<Double>();
        for (int i = 1; i < myPois.size()-1; i++) {
            double lat1 = myPois.get(i).getLatitude();
            double lng1 = myPois.get(i).getLongitude();

            double lat2 = myPois.get(i+1).getLatitude();
            double lng2 = myPois.get(i+1).getLongitude();

            double degreesToRadians = (Math.PI / 180.0);

            double latrad1 = lat1 * degreesToRadians;
            double latrad2 = lat2 * degreesToRadians;
            double dlat = (lat2 - lat1) * degreesToRadians;
            double dlng = (lng2 - lng1) * degreesToRadians;

            double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) + Math.cos(latrad1) *
                    Math.cos(latrad2) * Math.sin(dlng / 2) * Math.sin(dlng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double r = 6371000;

            results.add(r * c);
        }

        for (double i : results) {
            sum += i;
        }


        sum = sum / 1000.00;
        sum = (double) Math.round(sum * 100d) / 100d;
        return sum;
    }


    /*
        Questo metodo permette di ottenere la velocità media in km/h di una route
     */
    public static double getAverageSpeed(String name, Context context){
        ArrayList<PointOfInterest> pointOfInterests = PointOfInterestManager.getInstance(context).getPointOfInterestByName(name);
        double sum = 0;
        for(PointOfInterest p : pointOfInterests){
            sum += p.getSpeed();
        }
        sum = sum/pointOfInterests.size();
        sum = sum * 3.6;
        sum = (double) Math.round(sum * 100d) / 100d;
        return sum;
    }

    /*
        Questo metodo permette di ottenere l'accuratezza media di una route
     */
    public static double getAverageAccuracy(String name, Context context){
        ArrayList<PointOfInterest> pointOfInterests = PointOfInterestManager.getInstance(context).getPointOfInterestByName(name);
        double sum = 0;
        for(PointOfInterest p : pointOfInterests){
            sum += p.getAccuracy();
        }
        sum = sum/pointOfInterests.size();
        sum = (double) Math.round(sum * 100d) / 100d;
        return sum;
    }

    /*
        Questo metodo permette di ottenere la durata di una route
     */
    public static long getRouteDuration(Date begin){
        Date end = new java.util.Date(System.currentTimeMillis());
        long duration = end.getTime() - begin.getTime() - 3600000;
        return duration;
    }

    /*
        Questo metodo permette di calcolare le calorie consumate durante una route
     */
    public static double calculateCalories(double distance, int weight){
        double calories = 0.5*distance*weight;
        Log.d("MapFragment", calories + " " +distance + " " +weight);
        calories = (double) Math.round(calories * 100d) / 100d;
        return calories;
    }

    /*
        Questo metodo permette di ottenere uno zoom sulla mappa in modo da poter
        scattare la foto del percorso centrando quest'ultimo
     */

    public static LatLng move(LatLng startLL, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, startLL.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(startLL.latitude + latDiff, startLL.longitude
                + lonDiff);
    }

    /*
        Questo metodo permette di ottenere la longitudine dato uno spostamento
     */
    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * 6366198.00;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }


    /*
        Questo metodo permette di ottenere la latitudine dato uno spostamento
     */
    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / 6366198.00;
        return Math.toDegrees(rad);
    }

    /*
        Questo metodo permette di evitare la creazione di una route con lo stesso nome
        (Non viene utilizzato dall'ultima modifica su come è ottenuto il nome, da rimuovere)
     */
    public static String newName(String name, Context context) {
        if (RouteManager.getInstance(context).getRouteListByName(name).size() > 0) {
            name+="1";
            newName(name, context);
        }
        return name;
    }
}
