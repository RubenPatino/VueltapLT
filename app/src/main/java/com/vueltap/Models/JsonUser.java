package com.vueltap.Models;

import com.google.gson.annotations.SerializedName;

public class JsonUser {
   private Boolean checkPhone;
   private String role;
   private Boolean state;
   private String checkIn;
   @SerializedName("_id")
   private String id;
   private String email;
   private String name;
   private String lastName;
   private String address;
   private String urlAddress;
   private String phone;
   private String dniNumber;
   private String urlDniFront;
   private String urlDniBack;

    public Boolean getCheckPhone() {
        return checkPhone;
    }

    public String getRole() {
        return role;
    }

    public Boolean getState() {
        return state;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String getDniNumber() {
        return dniNumber;
    }

    public String getUrlDniFront() {
        return urlDniFront;
    }

    public String getUrlDniBack() {
        return urlDniBack;
    }
}
