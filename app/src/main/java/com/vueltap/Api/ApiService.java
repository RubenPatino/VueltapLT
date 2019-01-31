package com.vueltap.Api;

import com.vueltap.Models.JsonResponse;
import com.vueltap.Transport.Model.ModelTransport;

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
    //user
    @GET("user/check/{email}")
    Call<JsonResponse> EMAIL_CHECK(
            @Path("email") String email
    );

    @POST("user/add")
    @FormUrlEncoded
    Call<JsonResponse>USER_ADD(
            @Field("email") String email,
            @Field("dniNumber") String dniNumber,
            @Field("name") String name,
            @Field("lastName") String lastName,
            @Field("address") String address,
            @Field("phone") String phone,
            @Field("urlDniFront") String urlDniFront,
            @Field("urlDniBack") String urlDniBack,
            @Field("urlAddress") String urlAddress
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

    @Multipart
    @POST("user/upload/img/property")
    Call<JsonResponse> UPLOAD_PROPERTY(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );
    @Multipart
    @POST("user/upload/img/licence")
    Call<JsonResponse> UPLOAD_LICENCE(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );
    @Multipart
    @POST("user/upload/img/soat")
    Call<JsonResponse> UPLOAD_SOAT(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );
    @GET("type/transport")
    Call<ModelTransport> GET_TRANSPORT();

  /*  //LOGIN
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
    @POST("user/upload/cedula")
    Call<Void> USER_UPLOAD_IMAGE(
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );*/
}



