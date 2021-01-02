package db;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mobdev.mymovements.model.PointOfInterest;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PointOfInterestDao_Impl implements PointOfInterestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PointOfInterest> __insertionAdapterOfPointOfInterest;

  private final EntityDeletionOrUpdateAdapter<PointOfInterest> __deletionAdapterOfPointOfInterest;

  public PointOfInterestDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPointOfInterest = new EntityInsertionAdapter<PointOfInterest>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `poi_table` (`id`,`name`,`latitude`,`longitude`,`speed`,`accuracy`,`time`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PointOfInterest value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        stmt.bindDouble(3, value.getLatitude());
        stmt.bindDouble(4, value.getLongitude());
        stmt.bindDouble(5, value.getSpeed());
        stmt.bindDouble(6, value.getAccuracy());
        if (value.getTime() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getTime());
        }
      }
    };
    this.__deletionAdapterOfPointOfInterest = new EntityDeletionOrUpdateAdapter<PointOfInterest>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `poi_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PointOfInterest value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertAll(final PointOfInterest... pointOfInterests) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPointOfInterest.insert(pointOfInterests);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final PointOfInterest pointOfInterest) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPointOfInterest.handle(pointOfInterest);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<PointOfInterest> getAll() {
    final String _sql = "SELECT * FROM poi_table ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final List<PointOfInterest> _result = new ArrayList<PointOfInterest>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final PointOfInterest _item;
        _item = new PointOfInterest();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final double _tmpSpeed;
        _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
        _item.setSpeed(_tmpSpeed);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item.setAccuracy(_tmpAccuracy);
        final String _tmpTime;
        _tmpTime = _cursor.getString(_cursorIndexOfTime);
        _item.setTime(_tmpTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<PointOfInterest> loadAllByIds(final int[] userIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT ");
    _stringBuilder.append("*");
    _stringBuilder.append(" FROM poi_table WHERE id IN (");
    final int _inputSize = userIds.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : userIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final List<PointOfInterest> _result = new ArrayList<PointOfInterest>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final PointOfInterest _item_1;
        _item_1 = new PointOfInterest();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item_1.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item_1.setName(_tmpName);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item_1.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item_1.setLongitude(_tmpLongitude);
        final double _tmpSpeed;
        _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
        _item_1.setSpeed(_tmpSpeed);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item_1.setAccuracy(_tmpAccuracy);
        final String _tmpTime;
        _tmpTime = _cursor.getString(_cursorIndexOfTime);
        _item_1.setTime(_tmpTime);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<PointOfInterest> loadPointOfInterestByName(final String name) {
    final String _sql = "SELECT * FROM poi_table WHERE name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final List<PointOfInterest> _result = new ArrayList<PointOfInterest>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final PointOfInterest _item;
        _item = new PointOfInterest();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final double _tmpSpeed;
        _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
        _item.setSpeed(_tmpSpeed);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item.setAccuracy(_tmpAccuracy);
        final String _tmpTime;
        _tmpTime = _cursor.getString(_cursorIndexOfTime);
        _item.setTime(_tmpTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<PointOfInterest>> loadPointOfInterestByNameLimited(final String name) {
    final String _sql = "SELECT * FROM poi_table WHERE name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"poi_table"}, false, new Callable<List<PointOfInterest>>() {
      @Override
      public List<PointOfInterest> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
          final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final List<PointOfInterest> _result = new ArrayList<PointOfInterest>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PointOfInterest _item;
            _item = new PointOfInterest();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item.setName(_tmpName);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            _item.setLatitude(_tmpLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            _item.setLongitude(_tmpLongitude);
            final double _tmpSpeed;
            _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
            _item.setSpeed(_tmpSpeed);
            final double _tmpAccuracy;
            _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
            _item.setAccuracy(_tmpAccuracy);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            _item.setTime(_tmpTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
