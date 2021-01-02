package com.mobdev.mymovements.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobdev.mymovements.Activity.MainActivity;
import com.mobdev.mymovements.utility.MapSnapshotThread;
import com.mobdev.mymovements.model.PointOfInterest;
import com.mobdev.mymovements.R;
import com.mobdev.mymovements.model.Route;
import com.mobdev.mymovements.utility.Utility;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import db.PointOfInterestManager;
import com.mobdev.mymovements.service.LocationService;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final String TAG = "MapFragment";
    public static final String NOTIFICATION_PREF = "Notification_Icon";
    private GoogleMap mMap;
    private Button startButton;
    private Polyline gpsTrack;
    private boolean isTracking = false;
    private SupportMapFragment mapFragment = null;
    private Date time;
    private String transport;
    private String description;
    private String name;


    /*
        In questo metodo settiamo gli elementi della vista e richiamiamo il metodo per la
        richiesta dei permessi. Se la vista viene creata quando erà già presente un'attività dell'utente, la recuperiamo
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map_layout, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestPermission();
        startButton = (Button) view.findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isTracking){
                    Log.d(MapFragment.TAG, "Premuto");
                    if(checkLocationEnabled()){
                        setRouteTransport();
                    }
                    else {
                        Toast.makeText(getContext(), "You need to enabled your location", Toast.LENGTH_SHORT).show();
                        requestPermission();
                    }
                }
                else {
                    setRouteDescription();
                }
            }
        });

        if(savedInstanceState!=null && savedInstanceState.getBoolean("tracking")){
            Log.d(MapFragment.TAG, "Savedinstancestate");
            name = savedInstanceState.getString("name");
            transport = savedInstanceState.getString("transport");
            startRoute();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLocationService();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapFragment!=null) {
            mapFragment.onResume();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mapFragment!=null){
            mapFragment.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mapFragment!=null){
            mapFragment.onStop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapFragment!=null){
            mapFragment.onPause();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mapFragment!=null){
            mapFragment.onLowMemory();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapFragment!=null){
            mapFragment.onDestroy();
        }
    }

    /*
        Se è presente un'attività dell'utente ne salviamo nome e trasporto
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isTracking) {
            mapFragment.onSaveInstanceState(outState);
            outState.putBoolean("tracking", isTracking);
            outState.putString("name", name);
            outState.putString("transport", transport);
        }
    }


    /*
        Questo metodo viene chiamato appena la mappa è pronta, viene quindi inizializzata quest'ultima con la polyline per
        tracciare il percorso
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(10);
        gpsTrack = mMap.addPolyline(polylineOptions);
        initMap();
    }

    /*
      Questo metodo rimanda ai metodi di inizializzazione quali la richiesta dei permessi e la selezione
      del tipo di mappa
     */

    private void initMap(){
        if(mMap != null){
            requestPermission();
            selectMapType();
        }
    }

    /*
        Questo metodo attraverso uno switch sulle sharedpreferrences permette di mostrare il tipo di mappa
        selezionata dall'utente
     */

    private void selectMapType(){
        SharedPreferences settings = getContext().getSharedPreferences(MainActivity.PREFERENCE,0);
        String type = settings.getString(SettingsFragment.MAP_PREF, " ");
        Log.d(MapFragment.TAG, "sel" + type);
        switch(type) {
            case "Satellite Map":
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Log.d(MapFragment.TAG, "satellite");
                break;
            case "Terrain Map":
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                Log.d(MapFragment.TAG, "Terrain");
                break;
            case "Normal Map":
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Log.d(MapFragment.TAG, "Normal");
                break;
            case "Hybrid Map":
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                Log.d(MapFragment.TAG, "Hybrid");
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }
    }

    /*
        Questo metodo viene richiamato all'inizio dell'attività (button start) e permette, attraverso un alertdialog,
        di selezionare la tipologia di trasporto. Successivamente richiama il metodo StartRoute().
        Inoltre salva il timestamp (usato come nome per la Route e i POI)
     */

    private void setRouteTransport(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Get the layout inflater

        builder.setTitle("Select Transport type");
        builder.setSingleChoiceItems(R.array.transport,0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ListView lv = ((AlertDialog)dialog).getListView();
                name = new SimpleDateFormat("d.MM.yy HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
                name = Utility.newName(name,getContext());
                transport = lv.getAdapter().getItem(lv.getCheckedItemPosition()).toString();
                setNotificationIcon();
                startRoute();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                });
        builder.show();

    }

    /*
        Utilizzando le sharedPreferences salviamo la tipologia di trasporto che ci servirà
        per determinare l'icona da mostrare tra le notifiche
     */

    private void setNotificationIcon(){
        SharedPreferences settings = getContext().getSharedPreferences(MainActivity.PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(NOTIFICATION_PREF, transport);
        editor.commit();
    }

    /*
        Questo metodo permette di verificare se l'utente ha la localizzazione attiva e può iniziare una
        attività. In caso negativo viene mostrato un toast che avverte l'utente di attivarla
     */
    private boolean checkLocationEnabled(){
        if(Utility.hasLocationPermission(getContext())){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // This is new method provided in API 28
                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Log.d(MapFragment.TAG, String.valueOf(lm.isLocationEnabled()));
                return lm.isLocationEnabled();
            } else {
                // This is Deprecated in API 28
                int mode = Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE,
                        Settings.Secure.LOCATION_MODE_OFF);
                Log.d(MapFragment.TAG, String.valueOf((mode != Settings.Secure.LOCATION_MODE_OFF)));
                return (mode != Settings.Secure.LOCATION_MODE_OFF);
            }
        }
        else {
            Log.d(MapFragment.TAG, "falso");
            return false;
        }
    }
    /*
        Questo metodo viene invocato una volta selezionato il trasporto e pulisce la mappa, cattura il timestamp
        e richiama il servizio di localizzazione in background
     */

    private void startRoute(){
        isTracking = true;
        clearMap();
        startButton.setText(R.string.stop);
        startButton.setBackgroundResource(R.drawable.red_background);
        time = new java.util.Date(System.currentTimeMillis());
        startLocationService();
    }

    /*
        Questo metodo ripulisce tutti i dati della mappa, come il percorso e le liste dove sono salvati
        i valori di velocità ed accuratezza
     */

    private void clearMap(){
        if(mMap!=null) {
            List<LatLng> lastRoute = gpsTrack.getPoints();
            lastRoute.clear();
            Log.d(MapFragment.TAG, lastRoute.toString());
            gpsTrack.setPoints(lastRoute);
            Log.d(MapFragment.TAG, gpsTrack.getPoints().toString());
        }
    }

    /*
        Questo metodo permette di aggiungere un punto al percorso sulla mappa
        Ogni 20 punti inoltre la camera verrà spostata per garantire all'utente una visione migliore
     */

    private void addPointToRoute(LatLng point){
        List<LatLng> trackPoints = gpsTrack.getPoints();
        trackPoints.add(point);
        gpsTrack.setPoints(trackPoints);
        if(gpsTrack.getPoints().size()%3==1)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 18));
    }





    /*
        Questo metodo permette di inquadrare tutto il percorso sulla mappa
     */

    private void zoomToRoute(){
        if(gpsTrack.getPoints().size() > 0) {
            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            for (LatLng point : gpsTrack.getPoints()) {
                bounds.include(point);
            }
            LatLngBounds bound = bounds.build();
            LatLng center = bound.getCenter();
            LatLng northEast = Utility.move(center, 709/3, 709/3);
            LatLng southWest = Utility.move(center, -709/3, -709/3);
            bounds.include(southWest);
            bounds.include(northEast);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));

        }
    }

    /*
    Questo metodo viene richiamato a fine attività (stop button) e permette di aggiungere
    una descrizione alla Route
 */
    private void setRouteDescription(){
        //final LocationListener lc = this;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        builder.setTitle("Are you sure?");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout, (ViewGroup) getView(), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                description = input.getText().toString();
                stopRoute();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                });
        builder.show();
    }

      /*
        Questo metodo viene richiamato dopo la descrizione della Route e stoppa il servizio in background
        e richiama i metodi di salvataggio della Route
     */

    private void stopRoute(){
        isTracking = false;
        startButton.setText(R.string.start);
        startButton.setBackgroundResource(R.drawable.black_background);
        stopLocationService();
        zoomToRoute();
        saveRoute();
    }

    /*
        Questo metodo permette di salvare una Route nel DB.
        Inizialmente viene riportata la mappa alla tipologia normale, ne viene fatto uno schreenshot
        e vengono calcolati gli attributi da aggiungere alla route attraverso un nuovo thread per evitare di
        imballare il main thread.
     */
    private void saveRoute(){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        Route route = new Route(name, transport, description , new SimpleDateFormat("HH:mm:ss").format(time),
                                new SimpleDateFormat("HH:mm:ss").format(Utility.getRouteDuration(time)));
                        MapSnapshotThread runnable = new MapSnapshotThread(bitmap,route, getContext());
                        new Thread(runnable).start();
                    }
                });
            }
        });

    }

    /*
        Questo metodo permette di richiedere i permessi di localizzazione nel caso in cui non fossero stati
        approvati dall'utente
     */
    private void requestPermission(){
        if(Utility.hasLocationPermission(getContext())){
            return;
        }
        String myPermission = Manifest.permission.ACCESS_FINE_LOCATION;


        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),myPermission)) {

            Log.d(TAG,"checkForLocationPermissions() -> shouldShowRequestPermissionRationale(): true");

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            Toast.makeText(getContext(),"The Application needs the access to your location to properly work ! Check System Setting to grant access !",Toast.LENGTH_LONG).show();
            requestPermissions(new String[]{myPermission}, REQUEST_CODE_LOCATION_PERMISSION);

        } else {

            // No explanation needed, we can request the permission.

            Log.d(TAG,"checkForLocationPermissions() -> shouldShowRequestPermissionRationale(): false");

            ActivityCompat.requestPermissions(getActivity(),new String[]{myPermission},REQUEST_CODE_LOCATION_PERMISSION);

            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() -> requestCode:" + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0	&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"onRequestPermissionsResult() -> Permission GRANTED !");
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Log.e(TAG,"onRequestPermissionsResult() -> Permission DENIED !");
                }
                return;
            }
        }
    }

    /*
        Questo metodo permette di attivare il servizio foreground di localizzazione e
        viene passato il nome della route nell'attivazione tramite un intent
     */
    private void startLocationService(){
        Intent intent = new Intent(getContext(), LocationService.class);
        intent.putExtra("name",name);
        Log.d(MapFragment.TAG, "Start");
        getContext().startService(intent);
        observePointData();
    }

    /*
        Questo metodo stoppa il servizio di localizzazione
     */
    private void stopLocationService(){
        Intent intent = new Intent(getContext(), LocationService.class);
        getContext().stopService(intent);
    }

    /*
        Attraverso livedata andiamo ad aggiornare l'interfaccia utente se presente una nuova coordinata (se l'utente si è quindi spostato).
        Dato che è chiamato solo se la schermata è attiva occorre verificare tutte le coordinate presente all'interno della route,
        al fine di aggiungere al percorso anche quelle ricevute mentre era attiva un'altra schermata per creare correttamente il percorso
     */
    private void observePointData(){
        Log.d(MapFragment.TAG, "A" + name);
        PointOfInterestManager.getInstance(getContext()).getPointOfInterestByNameLimited(name).observe(getViewLifecycleOwner(), new Observer<List<PointOfInterest>>() {
            @Override
            public void onChanged(List<PointOfInterest> pointsList) {
                Log.d(MapFragment.TAG, "B" + name);
                Log.d(MapFragment.TAG, pointsList.toString());
                if(!pointsList.isEmpty()) {
                    List<LatLng> trackPoints = gpsTrack.getPoints();
                    for (PointOfInterest p : pointsList) {
                        if (p.getName().equals(name)) {
                            LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
                            if (!trackPoints.contains(latLng)) {
                                trackPoints.add(latLng);
                                Log.d(MapFragment.TAG, name + "   " + p.getName());
                            }
                        }
                    }
                    gpsTrack.setPoints(trackPoints);
                    if (gpsTrack.getPoints().size() % 3 == 1)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trackPoints.get(trackPoints.size()-1), 18));

                }

            }
        });
    }

    /*
        Questo metodo ci permette di aggiornare il fragment ogni qual volta venga mostrato. Viene utilizzato
        per cambiare il tipo di mappa dopo una modifica delle impostazioni da parte dell'utente
     */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initMap();
            Log.d(MapFragment.TAG, "Yes");
        }
    }

}

