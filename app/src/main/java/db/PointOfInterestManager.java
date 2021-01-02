package db;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.mobdev.mymovements.Activity.MainActivity;
import com.mobdev.mymovements.utility.Utility;
import com.mobdev.mymovements.model.PointOfInterest;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PointOfInterestManager {


    private static final String TAG = "PointOfInterestManager";
    private Context context = null;
    private RouteDatabase db = null;
    private PointOfInterestDao pointOfInterestDao = null;

    /*
     * The instance is static so it is shared among all instances of the class. It is also private
     * so it is accessible only within the class.
     */
    private static PointOfInterestManager instance = null;

    /*
     * The constructor is private so it is accessible only within the class.
     */
    private PointOfInterestManager(Context context){
        Log.d(MainActivity.TAG,"Number Manager Created !");

        this.context = context;

        this.db = Room.databaseBuilder(context, RouteDatabase.class, "PointOfInterests-database").allowMainThreadQueries().build();
        this.pointOfInterestDao = this.db.pointOfInterestDao();
    }

    public static PointOfInterestManager getInstance(Context context){
        /*
         * The constructor is called only if the static instance is null, so only the first time
         * that the getInstance() method is invoked.
         * All the other times the same instance object is returned.
         */
        if(instance == null)
            instance = new PointOfInterestManager(context);
        return instance;
    }

    public void addPointOfInterest(PointOfInterest pointOfInterest){
        this.pointOfInterestDao.insertAll(pointOfInterest);
    }

    public void addPointOfInterestToHead(PointOfInterest pointOfInterest){
        this.addPointOfInterest(pointOfInterest);
    }

    public void removePointOfInterest(PointOfInterest pointOfInterest){
        this.pointOfInterestDao.delete(pointOfInterest);
    }

    public ArrayList<PointOfInterest> getPointOfInterestByName(String name) {
        return (ArrayList<PointOfInterest>)this.pointOfInterestDao.loadPointOfInterestByName(name);
    }

    public LiveData<List<PointOfInterest>> getPointOfInterestByNameLimited(String name) {
        return (LiveData<List<PointOfInterest>>)this.pointOfInterestDao.loadPointOfInterestByNameLimited(name);
    }

    public ArrayList<PointOfInterest> getPointOfInterestList(){
        return (ArrayList<PointOfInterest>) this.pointOfInterestDao.getAll();
    }

    public boolean exportOnSharedDocument(Uri uri, String name, ArrayList<PointOfInterest> locations) {
        if (Utility.hasStoragePermission(context)) {
            if (uri == null) {
                Log.e(TAG, "Error Exporting on Shared Storage Document ! Uri = Null !");
                return false;
            }

            String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
            name = "<name>" + name + "</name><trkseg>\n";

            String segments = "";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            for (PointOfInterest location : locations) {
                Log.d(TAG, locations.toString());
                segments += "<trkpt lat=\"" + location.getLatitude() + "\" lon=\"" + location.getLongitude() + "\">" + "</trkpt>\n";
            }

            String footer = "</trkseg></trk></gpx>";

            try {

                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);


                if (outputStream != null) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    writer.append(header);
                    writer.append(name);
                    writer.append(segments);
                    writer.append(footer);
                    writer.flush();


                    return true;
                } else {
                    Log.e(TAG, "Error Exporting on Shared Storage Document ! OutputStream or Json Content = Null !");
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        Toast.makeText(context, "You need to enable permissions", Toast.LENGTH_SHORT).show();
        return false;
    }



}
