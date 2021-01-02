package db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.mobdev.mymovements.Activity.MainActivity;
import com.mobdev.mymovements.model.Route;


public class RouteManager {


    private Context context = null;
    private RouteDatabase db = null;
    private RouteDao routeDao = null;

    /*
     * The instance is static so it is shared among all instances of the class. It is also private
     * so it is accessible only within the class.
     */
    private static RouteManager instance = null;

    /*
     * The constructor is private so it is accessible only within the class.
     */
    private RouteManager(Context context){
        Log.d(MainActivity.TAG,"Number Manager Created !");

        this.context = context;

        this.db = Room.databaseBuilder(context, RouteDatabase.class, "Routes-database").allowMainThreadQueries().build();
        this.routeDao = this.db.routeDao();
    }

    public static RouteManager getInstance(Context context){
        /*
         * The constructor is called only if the static instance is null, so only the first time
         * that the getInstance() method is invoked.
         * All the other times the same instance object is returned.
         */
        if(instance == null)
            instance = new RouteManager(context);
        return instance;
    }

    public void addRoute(Route route){
        this.routeDao.insertAll(route);
    }

    public void addRouteToHead(Route route){
        this.addRoute(route);
    }

    public void removeRoute(Route route){
        this.routeDao.delete(route);
    }

    public Route getRouteById(int id) {
        return this.routeDao.loadRouteById(id);
    }

    public LiveData<List<Route>> getRouteList(String transport){
        if(transport.equals("All"))
            return (LiveData<List<Route>>) this.routeDao.getAll();
        return (LiveData<List<Route>>) this.routeDao.getAllSortByTransport(transport);
    }

    public LiveData<List<Route>> getRouteListLimited(){
        return (LiveData<List<Route>>) this.routeDao.getAllLimited();
    }

    public ArrayList<Route> getRouteListByName(String name){
        return (ArrayList<Route>) this.routeDao.getAllRouteByName(name);
    }

    public double getSumFromTransport(String transport) {
        return (double) this.routeDao.getSumFromTransport(transport);
    }

    public double getSumFromAllTransport() {
        return (double) this.routeDao.getSumFromAllTransport();
    }

    public Route getLastRoute(){
        return (Route) this.routeDao.getLastRoute();
    }
}



