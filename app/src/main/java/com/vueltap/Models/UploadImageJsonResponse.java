package com.vueltap.Models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadImageJsonResponse implements Serializable {

   private Boolean status;
   private String message;
   private String image_url;


    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
