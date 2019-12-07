package com.example.sholkamy.pharmalivary.Classes;

import java.util.ArrayList;

/**
 * Created by Sholkamy on 3/11/2019.
 */

public class UserData {

    private int web;
    private int has_request;
    private String name;
    private String email;
    private String id;
    private String password;
    private String phoneNumber;
    private String request_id;

    public UserData() {
    }

    public UserData(int web, int has_request, String request_id,String name,  String id, String email, String password, String phoneNumber) {
        this.web = web;
        this.has_request = has_request;
        this.name = name;
        this.email = email;
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.request_id = request_id;
    }

    //Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //hasRequest
    public int getHas_request() {
        return has_request;
    }

    public void setHas_request(int hasRequest) {
        this.has_request = hasRequest;
    }

    //Web
    public int getWeb() {
        return web;
    }

    public void setWeb(int web) {
        this.web = web;
    }


    //ID
    public String getId() {return id;}

    public void setId(String id) { this.id = id; }

    //Email
    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    //Password
    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    //PhoneNumber
    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    //RequestId
    public String getRequest_id() { return request_id; }

    public void setRequest_id(String Request_id) { this.request_id = Request_id; }
}
