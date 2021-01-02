package com.mobdev.mymovements.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.mobdev.mymovements.R;
import com.mobdev.mymovements.model.Route;


public  class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";
    public static final String INFO_POI = "POI";
    private Route route;
    private MaterialTextView transportTextView;
    private MaterialTextView speedTextView;
    private MaterialTextView accuracyTextView;
    private MaterialTextView distanceTextView;
    private MaterialTextView timestampTextView;
    private MaterialTextView durationTextView;
    private MaterialTextView caloriesTextView;
    private MaterialTextView descriptionTextView;
    private Button infoButton;

    private ImageView routeImageView;

    public InfoFragment() {
    }

    public Route getRoute() {
        return route;
    }

    /*
        Questo metodo ci permette di settare la Route corrispondente al fragment
     */

    public void setRoute(Route route) {
        this.route = route;
    }


    /*
        Questo metodo ci permette di settare la vista del fragment e, nel caso in cui la Route
        selezionata non sia nulla, richiama il metodo per impostare il testo nelle textviews
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_fragment, container, false);
        routeImageView = (ImageView)rootView.findViewById(R.id.ivRunImage);

        transportTextView = (MaterialTextView)rootView.findViewById(R.id.transport);
        speedTextView = (MaterialTextView)rootView.findViewById(R.id.speed);
        accuracyTextView = (MaterialTextView)rootView.findViewById(R.id.accuracy);
        distanceTextView = (MaterialTextView)rootView.findViewById(R.id.distance);
        timestampTextView = (MaterialTextView)rootView.findViewById(R.id.timestamp);
        durationTextView = (MaterialTextView)rootView.findViewById(R.id.duration);
        caloriesTextView = (MaterialTextView)rootView.findViewById(R.id.calories);
        descriptionTextView = (MaterialTextView)rootView.findViewById(R.id.description);



        if(route != null){
            Log.d(InfoFragment.TAG, route.toString() + route.getId());
            setAllText();
        }


        return rootView;
    }

    /*
        Questo metodo permette di impostare il testo e l'immagine corrispondenti alla ROute selezionata
     */
    private void setAllText(){
        if(route!=null){
            routeImageView.setImageBitmap(route.getImg());
            transportTextView.setText("Transport: " + route.getTransport());
            speedTextView.setText(String.valueOf("Speed: " + route.getSpeed() + " Km/h  "));
            accuracyTextView.setText(String.valueOf("Accuracy: " + route.getAccuracy()));
            distanceTextView.setText(String.valueOf("Distance: " + route.getDistance() + " Km"));
            timestampTextView.setText("Start: " + route.getTimestamp());
            durationTextView.setText("  Duration: " + route.getDuration());
            caloriesTextView.setText("Calories: " + route.getCalories() + " Kcal");
            if(!route.getTransport().equals("Walking")){
                caloriesTextView.setText("Calories: -.- Kcal");
            }
            descriptionTextView.setText("Description: " + route.getDescription());
            if(route.getDescription().isEmpty()){
                descriptionTextView.setText("");
            }

        }

    }

}