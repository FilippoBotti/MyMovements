package com.mobdev.mymovements.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdev.mymovements.R;

import db.RouteManager;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    public GridViewAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mData[position]);
        switch(holder.title.getText().toString()){
            case "All":
                holder.text.setText(String.valueOf(RouteManager.getInstance(mContext).getSumFromAllTransport()));
                holder.image.setBackgroundResource(R.drawable.ic_baseline_emoji_transportation_24);
                break;
            case "Time":
                if(RouteManager.getInstance(mContext).getLastRoute()!=null){
                    holder.text.setText(RouteManager.getInstance(mContext).getLastRoute().getDuration());
                }
                holder.image.setBackgroundResource(R.drawable.ic_baseline_timer_24);
                break;
            case "Calories":
                if(RouteManager.getInstance(mContext).getLastRoute()!=null){
                    holder.text.setText(String.valueOf(RouteManager.getInstance(mContext).getLastRoute().getCalories()) + " Kcal");
                }
                holder.image.setBackgroundResource(R.drawable.ic_baseline_local_fire_department_24);
                break;
            case "Driving":
                holder.text.setText(String.valueOf(RouteManager.getInstance(mContext).getSumFromTransport("Car")) + " Km");
                holder.image.setBackgroundResource(R.drawable.ic_baseline_directions_car_24);
                break;
            case "Walking":
                holder.text.setText(String.valueOf(RouteManager.getInstance(mContext).getSumFromTransport("Walking")) + " Km");
                holder.image.setBackgroundResource(R.drawable.ic_shoes);
                break;
            case "Public Transportation":
                holder.text.setText(String.valueOf(RouteManager.getInstance(mContext).getSumFromTransport("Public Transportation")) + "Km");
                holder.image.setBackgroundResource(R.drawable.ic_baseline_directions_bus_24);
                break;


        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView text;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            text = itemView.findViewById(R.id.cardText);
            image = itemView.findViewById(R.id.cardImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}