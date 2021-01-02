package db;

import android.database.Cursor;
import android.graphics.Bitmap;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mobdev.hellotabs.Utility;
import com.mobdev.hellotabs.model.Route;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RouteDao_Impl implements RouteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Route> __insertionAdapterOfRoute;

  private final EntityDeletionOrUpdateAdapter<Route> __deletionAdapterOfRoute;

  public RouteDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRoute = new EntityInsertionAdapter<Route>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `route_table` (`id`,`name`,`speed`,`accuracy`,`transport`,`timestamp`,`duration`,`distance`,`img`,`calories`,`description`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Route value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        stmt.bindDouble(3, value.getSpeed());
        stmt.bindDouble(4, value.getAccuracy());
        if (value.getTransport() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTransport());
        }
        if (value.getTimestamp() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getTimestamp());
        }
        if (value.getDuration() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getDuration());
        }
        stmt.bindDouble(8, value.getDistance());
        final byte[] _tmp;
        _tmp = Utility.fromBitmapToByte(value.getImg());
        if (_tmp == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindBlob(9, _tmp);
        }
        stmt.bindDouble(10, value.getCalories());
        if (value.getDescription() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getDescription());
        }
      }
    };
    this.__deletionAdapterOfRoute = new EntityDeletionOrUpdateAdapter<Route>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `route_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Route value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertAll(final Route... routes) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRoute.insert(routes);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Route route) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRoute.handle(route);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Route>> getAll() {
    final String _sql = "SELECT * FROM route_table ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"route_table"}, false, new Callable<List<Route>>() {
      @Override
      public List<Route> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
          final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<Route> _result = new ArrayList<Route>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Route _item;
            _item = new Route();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item.setName(_tmpName);
            final double _tmpSpeed;
            _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
            _item.setSpeed(_tmpSpeed);
            final double _tmpAccuracy;
            _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
            _item.setAccuracy(_tmpAccuracy);
            final String _tmpTransport;
            _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            _item.setTransport(_tmpTransport);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item.setTimestamp(_tmpTimestamp);
            final String _tmpDuration;
            _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
            _item.setDuration(_tmpDuration);
            final double _tmpDistance;
            _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            _item.setDistance(_tmpDistance);
            final Bitmap _tmpImg;
            final byte[] _tmp;
            _tmp = _cursor.getBlob(_cursorIndexOfImg);
            _tmpImg = Utility.fromByteToBitmap(_tmp);
            _item.setImg(_tmpImg);
            final double _tmpCalories;
            _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
            _item.setCalories(_tmpCalories);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item.setDescription(_tmpDescription);
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

  @Override
  public LiveData<List<Route>> getAllSortByTransport(final String transport) {
    final String _sql = "SELECT * FROM route_table WHERE transport =(?) ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (transport == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, transport);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"route_table"}, false, new Callable<List<Route>>() {
      @Override
      public List<Route> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
          final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<Route> _result = new ArrayList<Route>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Route _item;
            _item = new Route();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item.setName(_tmpName);
            final double _tmpSpeed;
            _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
            _item.setSpeed(_tmpSpeed);
            final double _tmpAccuracy;
            _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
            _item.setAccuracy(_tmpAccuracy);
            final String _tmpTransport;
            _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            _item.setTransport(_tmpTransport);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item.setTimestamp(_tmpTimestamp);
            final String _tmpDuration;
            _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
            _item.setDuration(_tmpDuration);
            final double _tmpDistance;
            _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            _item.setDistance(_tmpDistance);
            final Bitmap _tmpImg;
            final byte[] _tmp;
            _tmp = _cursor.getBlob(_cursorIndexOfImg);
            _tmpImg = Utility.fromByteToBitmap(_tmp);
            _item.setImg(_tmpImg);
            final double _tmpCalories;
            _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
            _item.setCalories(_tmpCalories);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item.setDescription(_tmpDescription);
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

  @Override
  public LiveData<List<Route>> getAllLimited() {
    final String _sql = "SELECT * FROM route_table ORDER BY id DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"route_table"}, false, new Callable<List<Route>>() {
      @Override
      public List<Route> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
          final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<Route> _result = new ArrayList<Route>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Route _item;
            _item = new Route();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item.setName(_tmpName);
            final double _tmpSpeed;
            _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
            _item.setSpeed(_tmpSpeed);
            final double _tmpAccuracy;
            _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
            _item.setAccuracy(_tmpAccuracy);
            final String _tmpTransport;
            _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            _item.setTransport(_tmpTransport);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item.setTimestamp(_tmpTimestamp);
            final String _tmpDuration;
            _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
            _item.setDuration(_tmpDuration);
            final double _tmpDistance;
            _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            _item.setDistance(_tmpDistance);
            final Bitmap _tmpImg;
            final byte[] _tmp;
            _tmp = _cursor.getBlob(_cursorIndexOfImg);
            _tmpImg = Utility.fromByteToBitmap(_tmp);
            _item.setImg(_tmpImg);
            final double _tmpCalories;
            _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
            _item.setCalories(_tmpCalories);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item.setDescription(_tmpDescription);
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

  @Override
  public List<Route> loadAllByIds(final int[] userIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT ");
    _stringBuilder.append("*");
    _stringBuilder.append(" FROM route_table WHERE id IN (");
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
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<Route> _result = new ArrayList<Route>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Route _item_1;
        _item_1 = new Route();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item_1.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item_1.setName(_tmpName);
        final double _tmpSpeed;
        _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
        _item_1.setSpeed(_tmpSpeed);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item_1.setAccuracy(_tmpAccuracy);
        final String _tmpTransport;
        _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
        _item_1.setTransport(_tmpTransport);
        final String _tmpTimestamp;
        _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
        _item_1.setTimestamp(_tmpTimestamp);
        final String _tmpDuration;
        _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
        _item_1.setDuration(_tmpDuration);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _item_1.setDistance(_tmpDistance);
        final Bitmap _tmpImg;
        final byte[] _tmp;
        _tmp = _cursor.getBlob(_cursorIndexOfImg);
        _tmpImg = Utility.fromByteToBitmap(_tmp);
        _item_1.setImg(_tmpImg);
        final double _tmpCalories;
        _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
        _item_1.setCalories(_tmpCalories);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item_1.setDescription(_tmpDescription);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Route> getAllRouteByName(final String searchName) {
    final String _sql = "SELECT * FROM route_table WHERE name = (?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<Route> _result = new ArrayList<Route>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Route _item;
        _item = new Route();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final double _tmpSpeed;
        _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
        _item.setSpeed(_tmpSpeed);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item.setAccuracy(_tmpAccuracy);
        final String _tmpTransport;
        _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
        _item.setTransport(_tmpTransport);
        final String _tmpTimestamp;
        _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        final String _tmpDuration;
        _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _item.setDistance(_tmpDistance);
        final Bitmap _tmpImg;
        final byte[] _tmp;
        _tmp = _cursor.getBlob(_cursorIndexOfImg);
        _tmpImg = Utility.fromByteToBitmap(_tmp);
        _item.setImg(_tmpImg);
        final double _tmpCalories;
        _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
        _item.setCalories(_tmpCalories);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Route loadRouteById(final int id) {
    final String _sql = "SELECT * FROM route_table WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final Route _result;
      if(_cursor.moveToFirst()) {
        _result = new Route();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _result.setName(_tmpName);
        final double _tmpSpeed;
        _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
        _result.setSpeed(_tmpSpeed);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _result.setAccuracy(_tmpAccuracy);
        final String _tmpTransport;
        _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
        _result.setTransport(_tmpTransport);
        final String _tmpTimestamp;
        _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
        _result.setTimestamp(_tmpTimestamp);
        final String _tmpDuration;
        _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _result.setDistance(_tmpDistance);
        final Bitmap _tmpImg;
        final byte[] _tmp;
        _tmp = _cursor.getBlob(_cursorIndexOfImg);
        _tmpImg = Utility.fromByteToBitmap(_tmp);
        _result.setImg(_tmpImg);
        final double _tmpCalories;
        _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
        _result.setCalories(_tmpCalories);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _result.setDescription(_tmpDescription);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public double getSumFromTransport(final String searchTransport) {
    final String _sql = "SELECT SUM(distance) FROM route_table WHERE transport =(?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchTransport == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchTransport);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public double getSumFromAllTransport() {
    final String _sql = "SELECT SUM(distance) FROM route_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Route getLastRoute() {
    final String _sql = "SELECT * FROM route_table ORDER BY id DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final Route _result;
      if(_cursor.moveToFirst()) {
        _result = new Route();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _result.setName(_tmpName);
        final double _tmpSpeed;
        _tmpSpeed = _cursor.getDouble(_cursorIndexOfSpeed);
        _result.setSpeed(_tmpSpeed);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _result.setAccuracy(_tmpAccuracy);
        final String _tmpTransport;
        _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
        _result.setTransport(_tmpTransport);
        final String _tmpTimestamp;
        _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
        _result.setTimestamp(_tmpTimestamp);
        final String _tmpDuration;
        _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _result.setDistance(_tmpDistance);
        final Bitmap _tmpImg;
        final byte[] _tmp;
        _tmp = _cursor.getBlob(_cursorIndexOfImg);
        _tmpImg = Utility.fromByteToBitmap(_tmp);
        _result.setImg(_tmpImg);
        final double _tmpCalories;
        _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
        _result.setCalories(_tmpCalories);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _result.setDescription(_tmpDescription);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
