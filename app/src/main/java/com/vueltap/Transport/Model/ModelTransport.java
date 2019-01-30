package com.vueltap.Transport.Model;

import com.google.gson.annotations.SerializedName;

public class ModelTransport {
    @SerializedName("_id")
    private Boolean status;
    private String message;
    private String id;
    private String name;
    private String description;

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
