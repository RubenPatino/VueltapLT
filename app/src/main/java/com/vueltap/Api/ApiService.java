package com.vueltap.Api;

import com.vueltap.Models.JsonResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    //LOGIN
//Listar
    @GET("user/check/{email}")
    Call<JsonResponse> EMAIL_CHECK(
            @Path("email") String email
    );

    @POST("user/add")
    @FormUrlEncoded
    Call<JsonResponse>USER_ADD(
            @Field("email") String email,
            @Field("name") String name,
            @Field("lastName") String lastName,
            @Field("address") String address,
            @Field("phone") String phone,
            @Field("identificationNumber") String identificationNumber
    );

    @Multipart
    @POST("user/upload/cedula/{email}")
    Call<Void> USER_UPLOAD_IMAGE(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );
}



