package com.mobdev.mymovements.Activity;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobdev.mymovements.R;
import com.mobdev.mymovements.fragment.SettingsFragment;


public class FirstLaunchActivity extends AppCompatActivity {

    private static final String TAG = "FirstLaunchActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_fragment_activity);

        getSupportFragmentManager().beginTransaction().add(R.id.infofragment, new SettingsFragment()).commit();

        setupToolBar();

    }

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




}