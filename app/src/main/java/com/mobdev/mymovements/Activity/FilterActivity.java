package com.mobdev.mymovements.Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobdev.mymovements.fragment.HistoryFragment;
import com.mobdev.mymovements.R;
import com.mobdev.mymovements.fragment.StatsGridFragment;
import com.mobdev.mymovements.model.Route;

public class FilterActivity extends AppCompatActivity {

    private static final String TAG = "FilterActivity";
    private Context mContext = null;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_fragment_activity);

        setupToolBar();

        if (savedInstanceState == null) {

            HistoryFragment historyFragment= null;

            Bundle bundle = this.getIntent().getExtras();

            if(bundle != null){

                String id = bundle.getString(StatsGridFragment.ROUTE_CATEGORY," ");
                historyFragment = new HistoryFragment();
                historyFragment.setTransport(id);

            }
            else{
                historyFragment = new HistoryFragment();

            }

            getSupportFragmentManager().beginTransaction().add(R.id.infofragment, historyFragment).commit();
        }
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
