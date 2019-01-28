package com.vueltap.Models;

public class JsonResponse {
   private Boolean status;
   private String message;
   private JsonUser user;


    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    public JsonUser getUser() {
        return user;
    }
}
