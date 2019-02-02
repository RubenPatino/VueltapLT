package com.vueltap.Api;

import com.vueltap.Models.JsonResponse;
import com.vueltap.Models.ImageUpload;
import com.vueltap.Transport.Model.ModelTransport;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    //user

    @GET("http://msaq.vueltap.com.co/api_1.0/Messengers/Check/Exist/{email}")
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
    @PUT("Messengers/Add/Image")
    Call<ImageUpload> UPLOAD_IMAGE(
            @Part("email") RequestBody email,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part image
    );
}



