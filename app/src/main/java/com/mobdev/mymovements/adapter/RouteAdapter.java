package com.mobdev.mymovements.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.mobdev.mymovements.Activity.InfoActivity;
import com.mobdev.mymovements.R;
import com.mobdev.mymovements.model.PointOfInterest;
import com.mobdev.mymovements.model.Route;

import java.util.ArrayList;
import java.util.List;

import db.PointOfInterestManager;
import db.RouteManager;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private static final String TAG = "RouteAdapter";
    public static final String INFO_ROUTE = "Route";
    private List<Route> mDataset;
    private Context mContext = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View v = null;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            v.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    showDeleteConfirmation(getLayoutPosition());
                    return false;
                }
            });


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
                    Bundle bundle = new Bundle();
                    bundle.putInt(INFO_ROUTE, mDataset.get(position).getId());


                    Intent newIntent = new Intent(new Intent(mContext, InfoActivity.class));
                    newIntent.putExtras(bundle);
                    mContext.startActivity(newIntent);
                }

            });


        }

        private void showDeleteConfirmation(final int position){

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.ShortAlertDialogCustom);
            builder.setMessage(R.string.remove_element_message);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    for(PointOfInterest poi : PointOfInterestManager.getInstance(mContext).getPointOfInterestByName(mDataset.get(position).getName())){
                        PointOfInterestManager.getInstance(mContext).removePointOfInterest(poi);
                    }
                    RouteManager.getInstance(mContext).removeRoute(mDataset.get(position));
                    notifyItemRemoved(position);
                    mDataset.remove(mDataset.get(position));
                    Log.d(RouteAdapter.TAG, "Removed");
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
        public void setAllText(String distance, String speed,  String duration, String name, String transport){
            MaterialTextView tView = (MaterialTextView) v.findViewById(R.id.tvDistance);
            tView.setText(distance);
            MaterialTextView t2View = (MaterialTextView) v.findViewById(R.id.tvAvgSpeed);
            t2View.setText(speed);
            MaterialTextView t5View = (MaterialTextView) v.findViewById(R.id.tvTime);
            t5View.setText(duration);
            TextView t6View = (TextView) v.findViewById(R.id.tvName);
            t6View.setText(name);
            TextView t7View = (TextView) v.findViewById(R.id.tvTransport);
            t7View.setText(transport);

        }

        public void setImage(String transport){
            int image=0;
            if(transport.equals("Walking"))
                image =R.drawable.ic_shoes;
            if(transport.equals("Driving"))
                image =R.drawable.ic_baseline_directions_car_24;
            if(transport.equals("Public Transportation"))
                image =R.drawable.ic_baseline_directions_bus_24;
            ImageView imageView = (ImageView) v.findViewById(R.id.tvImage);
            imageView.setBackgroundResource(image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RouteAdapter(List<Route> myDataset, Context context) {
        mDataset = myDataset;
        mContext  = context;
    }

    public RouteAdapter(Context context) {
        mDataset = new ArrayList<>();
        mContext  = context;
    }

    @NonNull
    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.routelistview_element, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.ViewHolder holder, int position) {
        Route route = mDataset.get(position);

        holder.setAllText(String.valueOf(route.getDistance()) + "Km", String.valueOf(route.getSpeed()) + "Km/h" ,
                  String.valueOf(route.getDuration()), route.getName(), route.getTransport());
        Log.d("RouteAdapter", route.getDistance() + " sp" + route.getSpeed() + " cal" + route.getCalories() +
                " dur" + route.getDuration() + " tim" + route.getTimestamp());
        holder.setImage(route.getTransport());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public List<Route> getDataset() {
        return mDataset;
    }

    public void setDataset(List<Route> mDataset) {
        this.mDataset = mDataset;
    }



}
