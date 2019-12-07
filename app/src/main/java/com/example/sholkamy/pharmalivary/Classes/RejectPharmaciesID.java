package com.example.sholkamy.pharmalivary.Classes;

public class RejectPharmaciesID {
    String CurrentUser;
    String PharmaciesID;
    String OrderID;

    public RejectPharmaciesID() { }

    public RejectPharmaciesID(String currentUser, String pharmaciesID, String OrderID) {
        this.CurrentUser = currentUser;
        this.PharmaciesID = pharmaciesID;
        this.OrderID = OrderID;
    }

    //CurrentUser
    public String getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(String CurrentUser) {
        this.CurrentUser = CurrentUser;
    }


    //Name
    public String getPharmaciesID() {
        return PharmaciesID;
    }

    public void setPharmaciesID(String PharmaciesID) {
        this.PharmaciesID = PharmaciesID;
    }


    //OrderID
    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String OrderID) {
        this.OrderID = OrderID;
    }

}
