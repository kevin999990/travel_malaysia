package com.example.travelmalaysia.model;

import com.google.android.gms.maps.model.LatLng;

public class MyPlace {
    int id;
    String name;
    String desc;
    double lat;
    double lng;
    int points;


    public MyPlace() {

    }

    public MyPlace(int id, String name, String desc, double lat, double lng, int points) {

        this.id = id;
        this.name = name;
        this.desc = desc;
        this.lat = lat;
        this.lng = lng;
        this.points = points;
    }

    public MyPlace(String name, String desc, double lat, double lng, int points) {

        this.name = name;
        this.desc = desc;
        this.lat = lat;
        this.lng = lng;
        this.points = points;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }
}
