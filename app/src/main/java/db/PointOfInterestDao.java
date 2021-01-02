package db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mobdev.mymovements.model.PointOfInterest;

import java.util.List;

@Dao
public interface PointOfInterestDao {

    @Query("SELECT * FROM poi_table ORDER BY id DESC")
    List<PointOfInterest> getAll();

    @Query("SELECT * FROM poi_table WHERE id IN (:userIds)")
    List<PointOfInterest> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM poi_table WHERE name = :name")
    List<PointOfInterest> loadPointOfInterestByName(String name);

    @Query("SELECT * FROM poi_table WHERE name LIKE :name")
    LiveData<List<PointOfInterest>> loadPointOfInterestByNameLimited(String name);

    @Insert
    void insertAll(PointOfInterest... pointOfInterests);

    @Delete
    void delete(PointOfInterest pointOfInterest);

}
