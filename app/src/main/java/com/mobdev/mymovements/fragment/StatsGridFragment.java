package com.mobdev.mymovements.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdev.mymovements.Activity.FilterActivity;
import com.mobdev.mymovements.Activity.InfoActivity;
import com.mobdev.mymovements.Activity.MainActivity;
import com.mobdev.mymovements.R;
import com.mobdev.mymovements.adapter.GridViewAdapter;

import db.RouteManager;


public class StatsGridFragment  extends Fragment implements GridViewAdapter.ItemClickListener {

    private GridViewAdapter adapter;
    private static final String TAG = "StatsFragment";

    public static final String ROUTE_CATEGORY = "RouteCategory";
    public static final String ROUTE_INFO = "Route";
    private RecyclerView recyclerView;
    private TextView nameTextView;
    private String[] data = {"Walking","Driving","Public Transportation","All","Time","Calories"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_grid_layout, container, false);
        nameTextView = (TextView) view.findViewById(R.id.welcomeTextView);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new GridViewAdapter(getContext(), data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        setupTextView();
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG,adapter.getItem(position));
        switch(adapter.getItem(position)){

            case "Time":
                if (RouteManager.getInstance(getContext()).getLastRoute()!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ROUTE_INFO, RouteManager.getInstance(getContext()).getLastRoute().getId());
                    Intent newIntent = new Intent(new Intent(getContext(), InfoActivity.class));
                    newIntent.putExtras(bundle);
                    getContext().startActivity(newIntent);
                }
                break;
            case "Calories":
                if (RouteManager.getInstance(getContext()).getLastRoute()!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ROUTE_INFO, RouteManager.getInstance(getContext()).getLastRoute().getId());
                    Intent newIntent = new Intent(new Intent(getContext(), InfoActivity.class));
                    newIntent.putExtras(bundle);
                    getContext().startActivity(newIntent);
                }
                break;
            default:
                Bundle bundle = new Bundle();
                bundle.putString(ROUTE_CATEGORY,adapter.getItem(position));
                Intent newIntent = new Intent(new Intent(getContext(), FilterActivity.class));
                newIntent.putExtras(bundle);
                getContext().startActivity(newIntent);
        }
    }

    public void setupTextView() {
        String name = getContext().getSharedPreferences(MainActivity.PREFERENCE,0)
                .getString(SettingsFragment.NAME_PREF,"");
        String text = ",\nHere's your stats:";
        String welcomeMessage = "Welcome " + name + text;
        SpannableStringBuilder spannable = new SpannableStringBuilder(welcomeMessage);
        spannable.setSpan(
                new ForegroundColorSpan(getResources().getColor((R.color.nameColor))),
                8, 8+name.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameTextView.setText(spannable);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            recyclerView.setAdapter(adapter);
            setupTextView();
        }
    }
}