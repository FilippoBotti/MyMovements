package db;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mobdev.mymovements.model.Route;

import java.util.List;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM route_table ORDER BY id DESC")
    LiveData<List<Route>> getAll();

    @Query("SELECT * FROM route_table WHERE transport =(:transport) ORDER BY id DESC")
    LiveData<List<Route>> getAllSortByTransport(String transport);

    @Query("SELECT * FROM route_table ORDER BY id DESC LIMIT 5")
    LiveData<List<Route>> getAllLimited();

    @Query("SELECT * FROM route_table WHERE id IN (:userIds)")
    List<Route> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM route_table WHERE name = (:searchName)")
    List<Route> getAllRouteByName(String searchName);

    @Query("SELECT * FROM route_table WHERE id = :id")
    Route loadRouteById(int id);

    @Query("SELECT SUM(distance) FROM route_table WHERE transport =(:searchTransport)")
    double getSumFromTransport(String searchTransport);

    @Query("SELECT SUM(distance) FROM route_table")
    double getSumFromAllTransport();

    @Query("SELECT * FROM route_table ORDER BY id DESC LIMIT 1")
    Route getLastRoute();

    @Insert
    void insertAll(Route... routes);

    @Delete
    void delete(Route route);

}

