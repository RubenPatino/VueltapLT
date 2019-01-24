package com.vueltap.util;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {

    public static boolean isJSON(String jsonString){
        try{
            new JSONObject(jsonString);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            try{
                new JSONArray(jsonString);
                return true;
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
