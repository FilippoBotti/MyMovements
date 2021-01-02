package com.mobdev.mymovements.model;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Marco Picone (picone.m@gmail.com) 20/03/2020
 * Data Structure integrate with Room Annotation
 */
@Entity(tableName = "route_table")
public class Route  {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "speed")
    private double speed = 0.0;

    @ColumnInfo(name = "accuracy")
    private double accuracy = 0.0;

    @ColumnInfo(name = "transport")
    private String transport = null;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "duration")
    private String duration;

    @ColumnInfo(name ="distance")
    private double distance = 0.0;

    @ColumnInfo(name = "img")
    private Bitmap img;

    @ColumnInfo(name = "calories")
    private double calories = 0;

    @ColumnInfo(name = "description")
    private String description = "";

    public Route() {
    }

    @Ignore
    public Route(String name, double speed, double accuracy, String transport,
                 String description, String timestamp, String duration, double distance, Bitmap bitmap,
                 double calories) {
        super();
        this.name = name;
        this.speed = speed;
        this.accuracy = accuracy;
        this.transport = transport;
        this.description = description;
        this.distance = distance;
        this.timestamp = timestamp;
        this.duration = duration;
        this.calories = calories;
        this.img = bitmap;
    }

    @Ignore
    public Route(String name, String transport,
                 String description, String timestamp, String duration) {
        super();
        this.name = name;
        this.transport = transport;
        this.description = description;
        this.timestamp = timestamp;
        this.duration = duration;
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

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        String toReturn = "Name: " + name +
                        " Speed: " + speed +
                        " Accuracy: " + accuracy +
                        " Timestamp: " + timestamp +
                        " Duration: " + duration +
                        " Calories: " + calories +
                        " Transport: " + transport +
                        " Description: " + description;
        return toReturn;
    }

    @Override
    public boolean equals(Object o) {
        Route logObj = (Route)o;
        if(logObj.name == this.name)
            return true;
        else
            return false;
    }

}