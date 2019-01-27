package com.vueltap.Api;

import com.vueltap.Api.ApiService.ApiService;
import com.vueltap.Api.ApiService.ApiUser;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiAdapter {

    private static ApiService API_SERVICE;
    private static ApiUser API_USER;

    //String baseUrl = "http://192.168.100.2:3000/";
    private static String baseUrl="https://vueltap.herokuapp.com/";

    public static ApiUser getApiUser(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (API_USER == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            API_USER = retrofit.create(ApiUser.class);
        }
        return API_USER;
    }
    public static ApiService getApiService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            API_SERVICE = retrofit.create(ApiService.class);
        }
        return API_SERVICE;
    }

}

