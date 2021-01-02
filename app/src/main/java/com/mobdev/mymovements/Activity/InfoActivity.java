package com.mobdev.mymovements.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mobdev.mymovements.utility.Utility;
import com.mobdev.mymovements.adapter.RouteAdapter;
import com.mobdev.mymovements.fragment.InfoFragment;
import com.mobdev.mymovements.R;
import com.mobdev.mymovements.model.PointOfInterest;
import com.mobdev.mymovements.model.Route;

import java.util.ArrayList;

import db.PointOfInterestManager;
import db.RouteManager;


public class InfoActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE = 54;

    private static final String EXTERNAL_DOCUMENT_FILENAME = "myRoute.gpx";

    private static final String DOCUMENT_APPLICATION_TYPE = "application/gpx+xml";

    private static final int EXTERNAL_FILE_CREATE_REQUEST_ID = 1818;

    private static final String TAG = "InfoActivity";
    private Context mContext = null;
    private Route route;

    /*
        Questo metodo setta la vista del fragment e recupera dal bundle la route da mostrare
        che gli verrà passata in fase di inizializzazione
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_fragment_activity);


        if (savedInstanceState == null) {

            InfoFragment infoFragment= null;

            Bundle bundle = this.getIntent().getExtras();

            if(bundle != null){
                infoFragment = new InfoFragment();
                int id = bundle.getInt(RouteAdapter.INFO_ROUTE);
                route  = RouteManager.getInstance(this).getRouteById(id);
                Log.d(InfoActivity.TAG, route.toString() + id);
                infoFragment.setRoute(route);

            }
            else{
                infoFragment = new InfoFragment();

            }

           getSupportFragmentManager().beginTransaction().add(R.id.infofragment, infoFragment).commit();
        }
        setupToolBar();
        checkForWriteExternalStoragePermissions();
    }

    /*
        Setup per la toolbar
     */
    private void setupToolBar(){
        //ToolBar and ActionBar Settings
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    /*
        Setup del menu per la toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.export){
            if(Utility.hasStoragePermission(this)) {
                exportOnSharedDocument();
            }
            else {
                showStoragePermissionDeniedMessage();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Send the request to create a file on the external shared memory (both internal or external)
     */
    public void exportOnSharedDocument(){
        try{

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(DOCUMENT_APPLICATION_TYPE);
                intent.putExtra(Intent.EXTRA_TITLE, EXTERNAL_DOCUMENT_FILENAME);

                startActivityForResult(intent, EXTERNAL_FILE_CREATE_REQUEST_ID);
            }else {
                Toast.makeText(getApplicationContext(), "Function not available !", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Send the request to create a file on the external shared memory (both internal or external)
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXTERNAL_FILE_CREATE_REQUEST_ID) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null && data.getData() != null) {
                        new Thread(new Runnable() {


                            @Override
                            public void run() {

                                ArrayList<PointOfInterest> l = PointOfInterestManager.getInstance(getApplicationContext()).getPointOfInterestByName(route.getName());
                                PointOfInterestManager.getInstance(getApplicationContext()).exportOnSharedDocument(data.getData(), route.getName(), l);

                            }
                        }).start();
                          }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }

    }

    private void showStoragePermissionDeniedMessage() {
        Toast.makeText(this,"Write External Storage Permission Not Granted !",Toast.LENGTH_LONG).show();
    }

    private void checkForWriteExternalStoragePermissions(){

        String myPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        int permissionCheck = ContextCompat.checkSelfPermission(this,myPermission);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG,"checkForWriteExternalStoragePermissions() -> WRITE_EXTERNAL_STORAGE Not Granted !");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,myPermission)) {

                Log.d(TAG,"checkForWriteExternalStoragePermissions() -> shouldShowRequestPermissionRationale(): true");

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Toast.makeText(this,"The Application needs the access to your external storage to properly work ! Check System Setting to grant access !",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{myPermission}, MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE);

            } else {

                // No explanation needed, we can request the permission.

                Log.d(TAG,"checkForWriteExternalStoragePermissions() -> shouldShowRequestPermissionRationale(): false");

                ActivityCompat.requestPermissions(this,new String[]{myPermission}, MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else{
            Log.d(TAG,"checkForWriteExternalStoragePermissions() -> WRITE_EXTERNAL_STORAGE GRANTED !");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.d(TAG,"onRequestPermissionsResult() -> requestCode:"+requestCode);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0	&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"onRequestPermissionsResult() -> Permission GRANTED !");
                    // permission was granted, yay! Do the
                    Toast.makeText(this,"Permission Granted !",Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Log.e(TAG,"onRequestPermissionsResult() -> Permission DENIED !");
                    showStoragePermissionDeniedMessage();
                }
                return;
            }
        }
    }

}