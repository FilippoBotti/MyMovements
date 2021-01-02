package com.mobdev.mymovements.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mobdev.mymovements.adapter.AppSectionsPagerAdapter;
import com.mobdev.mymovements.R;


public class MainActivity extends AppCompatActivity {

    public static String TAG = "HelloViewPager";
    public static final String PREFERENCE = "SettingsPreference";
    public static final String STATS_PREF = "StatsPref";
    public static final String FIRST_LAUNCH ="FirstLaunch";
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tabLayout = null;
    private int[] tabIcons = {
            R.drawable.ic_baseline_play_arrow_24,
            R.drawable.ic_baseline_map_24,
            R.drawable.ic_baseline_query_stats_24,
            R.drawable.ic_baseline_settings_24,
    };

    /*
        Questo metodo setta la vista dell'activity e richiama i metodi per settare
        la toolbar, il viewpager e il tablayout.
        Inoltre viene verificato se il lancio dell'app è il primo: in caso positivo viene mostrata
        una prima activity per il settaggio delle impostazioni dell'utente tramite sharedpreferences
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_viewpager_activity);

        setupToolBar();
        setupViewPager();
        setupTabLayout();
        SharedPreferences settings = this.getSharedPreferences(MainActivity.PREFERENCE,0);
        if(settings.getBoolean(FIRST_LAUNCH,true)){
            Intent newIntent = new Intent(new Intent(this, FirstLaunchActivity.class));
            this.startActivity(newIntent);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(MainActivity.TAG, "OnDestroy");
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        Log.d(MainActivity.TAG, "OnLowMemory");
        super.onLowMemory();
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

    private void setupViewPager(){

        mAppSectionsPagerAdapter  = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager  = (ViewPager)findViewById(R.id.pager);

        //Set the number of pages that should be retained to either side of the current page in the view hierarchy in an idle state.
        //Pages beyond this limit will be recreated from the adapter when needed.
        mViewPager.setOffscreenPageLimit(4);

        mViewPager.setAdapter(mAppSectionsPagerAdapter);

        mViewPager.setOnTouchListener(new ViewPager.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(MainActivity.TAG, "ViewPager Page Selected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });

        mViewPager.setCurrentItem(0);
    }

    private void setupTabLayout(){


        //TabLayout with ViewPager
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }



}