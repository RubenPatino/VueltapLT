package com.vueltap.Api.ApiService;

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

public interface ApiUser {
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
            @Field("urlAddress") String urlAddress,
            @Field("phone") String phone,
            @Field("dniNumber") String dniNumber,
            @Field("urlDniFront") String urlDniFront,
            @Field("urlDniBack") String urlDniBack
    );

    @Multipart
    @POST("user/upload/dni/front")
    Call<JsonResponse> UPLOAD_DNI_FRONT(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );
    @Multipart
    @POST("user/upload/dni/back")
    Call<JsonResponse> UPLOAD_DNI_BACK(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );
    @Multipart
    @POST("user/upload/domicile")
    Call<JsonResponse> UPLOAD_DOMICILE(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );
}