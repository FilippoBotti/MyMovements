package com.mobdev.mymovements.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdev.mymovements.R;
import com.mobdev.mymovements.adapter.RouteAdapter;
import com.mobdev.mymovements.model.Route;

import java.util.List;

import db.RouteManager;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLayoutManager = null;
    private RouteAdapter mAdapter = null;
    private Context context = null;
    private RouteManager routeManager = null;
    private String transport = null;
    private TextView emptyTextView = null;

    /*
        Questo metodo permette di settare la vista del fragment.
        Inoltre recupera il Context e il routeManager.
        Successivamente richiama i metodi di inizializzazione e di observe per i livedata
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        context = getContext();
        routeManager = RouteManager.getInstance(context);

        init(rootView);
        observeRouteData();

        return rootView;


    }

    /*
        Questo metodo permette di inizializzare la recyclerview presente nel fragment
        con il corrispettivo adapter
     */
    private void init(View rootView){
        emptyTextView = (TextView) rootView.findViewById(R.id.historyTextView);
        mRecyclerView  = (RecyclerView)rootView.findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager  = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.scrollToPosition(0);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter  = new RouteAdapter(context);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
        Questo metodo permette di registrarsi ai livedata per quanto riguarda le Route.
        Ogni qualvolta sia presente una nuova Route essa verrà automaticamente aggiunta in cima alla
        recyclerview. E' suddiviso in due casi: il primo nel caso in cui la stringa transport non sia nulla
        ed è il caso in cui il fragment è richiamato dalla filterActivity e deve filtrare le Route da mostrare
        (es. dopo aver cliccato su una cardview nelle stats);
        l'altro è nel caso in cui il fragment sia richiamato all'interno dell'activity principale e deve mostrare
        tutte le Route
     */

    private void observeRouteData(){
        if(transport!=null) {
            routeManager.getRouteList(transport).observe(getViewLifecycleOwner(), new Observer<List<Route>>() {
                @Override
                public void onChanged(List<Route> routesList) {
                    if (routesList != null) {
                        Log.d(TAG, "Update Route List Received ! List Size: " + routesList.size());
                        refreshRecyclerView(routesList, 0);
                    } else
                        Log.e(TAG, "Error observing Route List ! Received a null Object !");
                }
            });
        }
        else {
            routeManager.getRouteList("All").observe(getViewLifecycleOwner(), new Observer<List<Route>>() {
                @Override
                public void onChanged(List<Route> routesList) {
                    if (routesList != null) {
                        Log.d(TAG, "Update Route List Received ! List Size: " + routesList.size());
                        refreshRecyclerView(routesList, 0);
                    } else
                        Log.e(TAG, "Error observing Route List ! Received a null Object !");
                }
            });

        }
    }

    /*
        Questo metodo permette di aggiornare la recyclerview quando arriva un nuovo dato
     */
    private void refreshRecyclerView(List<Route> updatedList, int scrollPosition){
        mAdapter.setDataset(updatedList);
        mAdapter.notifyDataSetChanged();
        if(scrollPosition >= 0)
            mLayoutManager.scrollToPosition(scrollPosition);
        if(updatedList.isEmpty())
            emptyTextView.setText("Your routes will be displayed here");
        else
            emptyTextView.setText("");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            //mAdapter.notifyDataSetChanged();
            Log.i("IsRefresh", "Yes");
        }
    }

    /*
        Questo metodo permette di settare la stringa transport ed è utilizzato nel caso in cui
        il fragment appartenga alla filterActivity
     */
    public void setTransport(String transport) {
        this.transport = transport;
    }
}





