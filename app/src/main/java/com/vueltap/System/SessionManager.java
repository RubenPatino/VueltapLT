package com.vueltap.System;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.vueltap.System.Constant.ADDRESS;
import static com.vueltap.System.Constant.DNI_NUMBER;
import static com.vueltap.System.Constant.EMAIL;
import static com.vueltap.System.Constant.INFORMATION;
import static com.vueltap.System.Constant.LAST_NAME;
import static com.vueltap.System.Constant.NAMES;
import static com.vueltap.System.Constant.PHONE;
import static com.vueltap.System.Constant.UID;
import static com.vueltap.System.Constant.URL_DNI_BACK;
import static com.vueltap.System.Constant.URL_DNI_FRONT;
import static com.vueltap.System.Constant.URL_DOMICILE;
import static com.vueltap.System.Constant.URL_INFORMATION;
import static com.vueltap.System.Constant.VALUE_ZERO;

public class SessionManager {
    private Context objContext;
    private SharedPreferences objShared;
    private SharedPreferences.Editor objEditor;

    public SessionManager(Context context) {
        this.objContext = context;
    }

    public void setPersonalInformation(String uid,String email,String dniNumber,String names,String lastName,String address,String phone ){
        objShared = objContext.getSharedPreferences(INFORMATION,Context.MODE_PRIVATE);
        objEditor = objShared.edit();
        objEditor.putString(UID,uid);
        objEditor.putString(EMAIL,email);
        objEditor.putString(DNI_NUMBER,dniNumber);
        objEditor.putString(NAMES,names);
        objEditor.putString(LAST_NAME,lastName);
        objEditor.putString(ADDRESS,address);
        objEditor.putString(PHONE,phone);
        objEditor.commit();
    }

    public JSONObject getDataConfig(){
        objShared=objContext.getSharedPreferences(INFORMATION,Context.MODE_PRIVATE);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(EMAIL,objShared.getString(EMAIL, VALUE_ZERO));
            jsonObject.put(DNI_NUMBER,objShared.getString(DNI_NUMBER, VALUE_ZERO));
            jsonObject.put(NAMES,objShared.getString(NAMES, VALUE_ZERO));
            jsonObject.put(LAST_NAME,objShared.getString(LAST_NAME, VALUE_ZERO));
            jsonObject.put(ADDRESS,objShared.getString(ADDRESS, VALUE_ZERO));
            jsonObject.put(PHONE,objShared.getString(PHONE, VALUE_ZERO));
        } catch (JSONException e) {
            Toast.makeText(objContext,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return jsonObject;
    }

    public void clearInformation(){
        objShared=objContext.getSharedPreferences(INFORMATION,Context.MODE_PRIVATE);
        objShared.edit().clear().commit();
    }



    public void setUrlInformation(String urlDniFront,String urlDniBack,String urlDomicile){
        objShared = objContext.getSharedPreferences(URL_INFORMATION,Context.MODE_PRIVATE);
        objEditor = objShared.edit();
        objEditor.putString(URL_DNI_FRONT,urlDniFront);
        objEditor.putString(URL_DNI_BACK,urlDniBack);
        objEditor.putString(URL_DOMICILE,urlDomicile);
        objEditor.commit();
    }

    public JSONObject getUrlInformation(){
        objShared=objContext.getSharedPreferences(URL_INFORMATION,Context.MODE_PRIVATE);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(URL_DNI_FRONT,objShared.getString(URL_DNI_FRONT, VALUE_ZERO));
            jsonObject.put(URL_DNI_BACK,objShared.getString(URL_DNI_BACK, VALUE_ZERO));
            jsonObject.put(URL_DOMICILE,objShared.getString(URL_DOMICILE, VALUE_ZERO));
        } catch (JSONException e) {
            Toast.makeText(objContext,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return jsonObject;
    }
    public void clearUrlInformation(){
        objShared=objContext.getSharedPreferences(URL_INFORMATION,Context.MODE_PRIVATE);
        objShared.edit().clear().commit();
    }

}
