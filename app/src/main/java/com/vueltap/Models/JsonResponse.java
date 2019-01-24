package com.vueltap.Models;

public class JsonResponse {
   private Boolean status;
   private String message;
   private User user;


    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}
