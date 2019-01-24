package com.vueltap.Models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    private String id;
    private String email;
    private String name;
    private String lastName;
    private String address;
    private String identificationNumber;
    private String phone;
    private Boolean checkPhone;
    private String role;
    private String img;
    private Boolean state;
    private Boolean checkTraining;

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    private Boolean checkInfo;

    public Boolean getState() {
        return state;
    }

    public Boolean getCheckPhone() {
        return checkPhone;
    }

    public Boolean getCheckTraining() {
        return checkTraining;
    }

    public Boolean getCheckInfo() {
        return checkInfo;
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

    public String getPhone() {
        return phone;
    }


    public String getRole() {
        return role;
    }

    public String getImg() {
        return img;
    }




}
