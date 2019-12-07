package com.example.sholkamy.pharmalivary.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class RequestMessage {

    private String name;
    private String OrderID;
    private String PharmaciesID;
    private String ID;
    private String image;
    private String phone;
    private double lat;
    private double lng;
    private int response;
    private int web;
    private ArrayList<String> med_name = new ArrayList<String>();
    private ArrayList<String> quantity = new ArrayList<String>();

    public RequestMessage() {
    }

    public RequestMessage(int response, int web, String name, String OrderID, String ID, String PharmaciesID,
                          String image, String phone, double lng, double lat ,ArrayList<String> med_name ,
                          ArrayList<String> quantity) {
        this.response = response;
        this.web = web;
        this.name = name;
        this.ID = ID;
        this.OrderID = OrderID;
        this.PharmaciesID = PharmaciesID;
        this.image = image;
        this.phone = phone;
        this.lng = lng;
        this.lat = lat;
        this.med_name = med_name;
        this.quantity = quantity;

    }


    //Response
    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }


    //Web
    public int getWeb() {
        return web;
    }

    public void setWeb(int web) {
        this.web = web;
    }


    //Name
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    //OrderID
    public String getOrderID() { return OrderID; }

    public void setOrderID(String OrderID) { this.OrderID = OrderID; }

    //OrderID
    public String getPharmaciesID() { return PharmaciesID; }

    public void setPharmaciesID(String PharmaciesID) { this.PharmaciesID = PharmaciesID; }

    //User_id
    public String getUser_id() { return ID; }

    public void setUser_id(String User_id) { this.ID = User_id; }

    //Image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Lng
    public double getLng() {
        return lng;
    }

    public void setLng(double Lng) {
        this.lng = Lng;
    }

    //Lat
    public double getLat() {
        return lat;
    }

    public void setLat(double Lat) {
        this.lat = Lat;
    }

    //Med_name
    public ArrayList<String> getMed_name() {
        return med_name;
    }

    public void setMed_name(ArrayList<String> Med_name) {
        this.med_name = Med_name;
    }

    //Quantity
    public ArrayList<String> getQuantity(){return quantity;}

    public void setQuantity(ArrayList<String> quantity) {this.quantity = quantity;}

}


