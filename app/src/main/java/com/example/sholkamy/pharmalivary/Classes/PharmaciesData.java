package com.example.sholkamy.pharmalivary.Classes;

public class PharmaciesData {

    private String id;
    private String name;
    private String lat;
    private String lng;

    public PharmaciesData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public PharmaciesData(String id, String name, String lat, String lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
}
