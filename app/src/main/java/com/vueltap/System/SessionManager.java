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

    public void clearDataConfig(){
        objShared=objContext.getSharedPreferences(INFORMATION,Context.MODE_PRIVATE);
        objShared.edit().clear().commit();
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
}
