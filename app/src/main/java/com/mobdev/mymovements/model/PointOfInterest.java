package com.mobdev.mymovements.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "poi_table")
public class PointOfInterest {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "latitude")
    private double latitude = 0.0;

    @ColumnInfo(name = "longitude")
    private double longitude = 0.0;

    @ColumnInfo(name = "speed")
    private double speed = 0.0;

    @ColumnInfo(name = "accuracy")
    private double accuracy = 0.0;

    @ColumnInfo(name = "time")
    private String time  = "";

    public PointOfInterest() {

    }

    @Ignore
    public PointOfInterest(String name, double latitude, double longitude, double speed, double accuracy, String time) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.accuracy = accuracy;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", accuracy=" + accuracy +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        PointOfInterest logObj = (PointOfInterest) o;
        if(logObj.name == this.name)
            return true;
        else
            return false;
    }

}
