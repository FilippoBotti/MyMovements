package db;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mobdev.mymovements.utility.Utility;
import com.mobdev.mymovements.model.PointOfInterest;
import com.mobdev.mymovements.model.Route;

@TypeConverters(Utility.class)

@Database(entities = {Route.class, PointOfInterest.class}, version = 1, exportSchema = false)
public abstract class RouteDatabase extends RoomDatabase {
    public abstract  RouteDao routeDao();
    public abstract PointOfInterestDao pointOfInterestDao();
}

